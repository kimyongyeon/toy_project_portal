package com.simple.portal.biz.v1.user.service;

import com.amazonaws.services.s3.model.MultipartUpload;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.simple.portal.biz.v1.user.ApiConst;
import com.simple.portal.biz.v1.user.UserConst;
import com.simple.portal.biz.v1.user.dto.*;
import com.simple.portal.biz.v1.user.entity.UserEntity;
import com.simple.portal.biz.v1.user.exception.*;
import com.simple.portal.biz.v1.user.repository.UserRepository;
import com.simple.portal.common.Interceptor.JwtUtil;
import com.simple.portal.util.ApiHelper;
import com.simple.portal.util.CustomMailSender;
import com.simple.portal.util.DateFormatUtil;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class UserService {
    private UserRepository userRepository;
    private CustomMailSender mailSender;
    private JwtUtil jwtUtil;
    private RedisTemplate<String, String> redisTemplate;
    private JPAQueryFactory jpaQueryFactory;
    private S3Service s3Service;

    @PersistenceContext
    private EntityManager entityManager; // native query를 위해 entityManger 추가

    @Autowired
    public void UserController(UserRepository userRepository,
                               CustomMailSender mailSender,
                               JwtUtil jwtUtil,
                               RedisTemplate redisTemplate,
                               JPAQueryFactory jpaQueryFactory,
                               S3Service s3Service) {
        this.userRepository = userRepository;
        this.mailSender = mailSender;
        this.jwtUtil = jwtUtil;
        this.redisTemplate = redisTemplate;
        this.jpaQueryFactory = jpaQueryFactory;
        this.s3Service = s3Service;
    }

    public List<UserReadDto> userFindAllService( ) {
        try {
            List<UserEntity> userEntityList = userRepository.findAll();
            List<UserReadDto> userReadDtoList = new ArrayList<>();
            for(int i=0; i<userEntityList.size(); i++) {

               FollowedList followedList = getFollowerIdService(userEntityList.get(i).getId());
               FollowingList followingList = getFollowingIdService(userEntityList.get(i).getId());

               userReadDtoList.add(UserReadDto.builder()
                       .id(userEntityList.get(i).getId())
                       .userId(userEntityList.get(i).getUserId())
                       .email(userEntityList.get(i).getEmail())
                       .nickname(userEntityList.get(i).getNickname())
                       .gitAddr(userEntityList.get(i).getGitAddr())
                       .profileImg(userEntityList.get(i).getProfileImg())
                       .activityScore(userEntityList.get(i).getActivityScore())
                       .authority(userEntityList.get(i).getAuthority())
                       .created(userEntityList.get(i).getCreated())
                       .updated(userEntityList.get(i).getUpdated())
                       .followedList(followedList)
                       .followingList(followingList)
                       .build());
            }
            return userReadDtoList;
        }
        catch(Exception e) {
            log.info("[UserService] userFindAllService Error : " + e.getMessage());
            throw new SelectUserFailedException();
        }
    }

     // 유저의 기본키로 유저 조회
    @Transactional
    public UserReadDto userFineOneService(Long id) {
        try {
            FollowedList followedList = getFollowerIdService(id);
            FollowingList followingList = getFollowingIdService(id);

            UserEntity userEntity = userRepository.findById(id).get();
            UserReadDto userReadDto = UserReadDto.builder()
                    .id(userEntity.getId())
                    .userId(userEntity.getUserId())
                    .email(userEntity.getEmail())
                    .nickname(userEntity.getNickname())
                    .gitAddr(userEntity.getGitAddr())
                    .profileImg(userEntity.getProfileImg())
                    .activityScore(userEntity.getActivityScore())
                    .authority(userEntity.getAuthority())
                    .created(userEntity.getCreated())
                    .updated(userEntity.getUpdated())
                    .followedList(followedList)
                    .followingList(followingList)
                    .build();

            return userReadDto;
        } catch (Exception e) {
            log.info("[UserService] userFindOneService Error : " + e.getMessage());
            throw new SelectUserFailedException();
        }
    }

    // 유저id로 pk id 조회
    public Long userFindPkService(String userId) {
        try {
            UserEntity userEntity = userRepository.findByUserId(userId);
            return userEntity.getId();
        } catch (Exception e) {
            log.info("[UserService] userFindPkService Error : " + e.getMessage());
            throw new SelectUserPkFailedException();
        }
    }

    @Transactional
    public void createUserService(UserCreateDto user) {
        try {
            // 빌더 패턴 적용
            UserEntity insertUser = UserEntity.builder()
                    .userId(user.getUserId())
                    .nickname(user.getNickname())
                    .email(user.getEmail())
                    .password(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt())) // 비밀번호
                    .gitAddr("https://github.com") // default 깃 주소
                    .profileImg(ApiConst.baseImgUrl)
                    .activityScore(0) // 초기점수 0 점
                    .authority('N') // 초기 권한 N
                    .created(DateFormatUtil.makeNowTimeStamp())
                    .updated(DateFormatUtil.makeNowTimeStamp())
                    .build();

            userRepository.save(insertUser);
            try {
                mailSender.sendJoinMail("Okky 회원 가입 완료 메일 !", user.getUserId(), user.getEmail()); // 회원가입 후 해당 이메일로 인증 메일보냄
            } catch (Exception e) {
                log.info("[UserService] emailSend Error : " + e.getMessage());
                throw new EmailSendFailedException();
            }
        } catch (Exception e) {

            log.info("[UserService] createUserService Error : " + e.getMessage());
            throw new CreateUserFailedException();
        }
    };

    public void updateUserService(UserUpdateDto user, MultipartFile file) {
        try {
            String imgPath = s3Service.upload(user.getUserId(), file);
            UserEntity originUser = userRepository.findById(user.getId()).get();

            // 빌더 패턴 적용
            UserEntity updateUser = UserEntity.builder()
                    .id(originUser.getId()) // 변경 불가
                    .userId(originUser.getUserId()) // 변경 불가
                    .email(originUser.getEmail()) // 변경 불가
                    .nickname(user.getNickname()) // 변경 가능
                    .password(originUser.getPassword()) // 변경 불가 ( 비밀번호 변경 api 따로 존재 )
                    .gitAddr(user.getGitAddr()) // 변경 가능
                    .profileImg(imgPath) // 변경 가능
                    .activityScore(originUser.getActivityScore()) // 변경 불가
                    .authority(originUser.getAuthority()) // 변경 불가
                    .created(originUser.getCreated()) // 변경 불가
                    .updated(DateFormatUtil.makeNowTimeStamp()) // 현재시간으로 변경
                    .build();

            userRepository.save(updateUser);
        } catch (Exception e) {
            log.info("[UserService] updateUserService Error : " + e.getMessage());
            throw new UpdateUserFailedException();
        }
    };

    @Transactional
    public void deleteUserService(Long id) {
        try {
            UserEntity deleteUser = userRepository.findById(id).get();
            String imgDir = deleteUser.getProfileImg();
            String delFollowedKey = "user:followed:";
            String delFollowingKey = "user:following:";
            userRepository.deleteById(id);

            //프로필 이미지 삭제
            s3Service.deleteImg(deleteUser.getUserId() + "profile.jpg");

            //나의 팔로워 및 팔로잉 정보 삭제.
            //내 꺼에서 followed, following 지우면서 각 유저의 것들도 지워줘야함.
            try {
                SetOperations<String, String> setOperations = redisTemplate.opsForSet();
                //내 팔로잉 조회
                FollowingList followingList = getFollowingIdService(id);
                //내 팔로워 조회
                FollowedList followedList = getFollowerIdService(id);
                // 내가 팔로잉한 유저들의 팔로워 정보에서 내 id 삭제
                for(int i=0; i<followingList.getCnt(); i++) {
                    String followedKey = "user:followed:" + followingList.getFollowing_users().get(i).getId();
                    setOperations.remove(followedKey, String.valueOf(id));
                }
                //나를 팔로우한 유저들의 팔로잉 정보에서 내 id 삭제
                for(int i=0; i<followedList.getCnt(); i++) {
                    String followingKey = "user:following:" + followedList.getFollowed_users().get(i).getId();
                    setOperations.remove(followingKey, String.valueOf(id));
                }
                //내 팔로잉/팔로우 정보 삭제
                redisTemplate.delete(delFollowedKey + id);
                redisTemplate.delete(delFollowingKey + id);
            } catch (Exception e) {
                e.printStackTrace();
                log.info("[UserService] unfollowService Error : " + e.getMessage());
                throw new UnfollowFailedException();
            }
        } catch (Exception e) {
            log.info("[UserService] deleteUserService Error : " + e.getMessage());
            throw new DeleteUserFailedException();
        }
    }

    public Boolean idCheckService(String user_id) {
        try {
            return userRepository.existsUserByUserId(user_id) == true ? true : false;
        } catch (Exception e) {
            log.info("[UserService] idCheckService Error : " + e.getMessage());
            throw new IdCheckFailedException();
        }
    }

    @Transactional
    public String userLoginService(String user_id, String password) { // 성공만 처리하고 나머지 exception 던짐
        try {
            if(!userRepository.existsUserByUserId(user_id)) throw new Exception(UserConst.NO_USER); // 아이디 존재 안함.
            else {
                UserEntity user = userRepository.findByUserId(user_id);
                String pwOrigin = user.getPassword();
                if (BCrypt.checkpw(password, pwOrigin)) return jwtUtil.createToken(user.getUserId());
                else throw new Exception(UserConst.INVALID_PASSWORD); // 비밀번호 오류
            }
        } catch (Exception e) {
            log.info("[UserService] userLoginService Error : " + e.getMessage());
            throw new LoginFailedException(e.getMessage());
    }
    }

    // 유저 권한 부여
    public Boolean updateUserAuthService(String userId) {
        try {
            userRepository.updateUserAuth(userId);
            return true;
        } catch (Exception e) {
            log.info("[UserService] userAuthService Error : " + e.getMessage());
            return false;
            //throw new UserAuthGrantFailedException();
        }
    }

    //유저 권한 체크
    public char userAuthCheckServie(String userId) {
        try {
            return userRepository.checkUserAuth(userId);
        } catch (Exception e) {
            log.info("[UserService] userAuthCheckService Error : " + e.getMessage());
            throw new UserAuthCheckFailedException();
        }
    }

    // 비밀번호 변경
    public void updateUserPasswordService(Long id, String newPassword) {
        try{
            UserEntity originUser = userRepository.findById(id).get();
            originUser.setUpdated(DateFormatUtil.makeNowTimeStamp());   // 비밀번호 변경했을때 updateTime 갱신
            originUser.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
            userRepository.save(originUser);
        } catch (Exception e) {
            log.info("[UserService] updateUserPassword Error : " + e.getMessage());
            throw new UpdatePasswordFailedException();
        }
    }

    // 비밀번호 찾기 ( = 새로운 비밀번호 전송 )
    @Transactional
    public void findUserPasswordService(String user_id) {

        // 입력된 user_id가 회원 가입된 아이디인지 판별
        if(!userRepository.existsUserByUserId(user_id)) { // 회원가입이 안된 유저일 경우
            throw new UserNotFoundException();
        }

        try {
            // 랜덤값으로 비밀번호 변경 후 -> 이메일 발송
            String randomValue = ApiHelper.getRandomString(); // 이 값을 메일로 전송

            String userEmail = userRepository.findByUserId(user_id).getEmail(); // 해당 id로 가입된 이메일 조회
            userRepository.updatePassword(user_id, BCrypt.hashpw(randomValue, BCrypt.gensalt()));
            try {
                mailSender.sendNewPwMail("신규 비밀번호 안내 !", userEmail, user_id, randomValue); // 회원가입 후 해당 이메일로 인증 메일보냄
            } catch (Exception e) {
                log.info("[UserService] emailSend Error : " + e.getMessage());
                throw new EmailSendFailedException();
            }
            // 해당 값을 이메일로 발송
        } catch (Exception e) {
            log.info("[UserService] findUserPassword Error : " + e.getMessage());
            throw new FindPasswordFailedException();
        }
    }

    // 팔로우하기
    @Transactional
    public void followService(Long following_id, Long followed_id) {
        try {
            SetOperations<String, String> setOperations = redisTemplate.opsForSet();
            String followedKey = "user:followed:" + followed_id;
            String followingKey = "user:following:" + following_id;
            setOperations.add(followedKey, String.valueOf(following_id));
            setOperations.add(followingKey, String.valueOf(followed_id));
        } catch (Exception e) {
            log.info("[UserService] followService Error : " + e.getMessage());
            throw new FollowFailedException();
        }
    }

    // 언팔로우하기
    @Transactional
    public void unfollowService(Long following_id, Long followed_id) {
        try {
            SetOperations<String, String> setOperations = redisTemplate.opsForSet();
            String followingKey = "user:following:" + following_id;
            String followedKey = "user:followed:" + followed_id;
            setOperations.remove(followingKey, String.valueOf(followed_id));
            setOperations.remove(followedKey, String.valueOf(following_id));
        } catch (Exception e) {
            e.printStackTrace();
            log.info("[UserService] unfollowService Error : " + e.getMessage());
            throw new UnfollowFailedException();
        }
    }

    // 내 팔로워 조회 ( ID )
    public FollowedList getFollowerIdService(Long followed_id) {
        try {
            SetOperations<String, String> setOperations = redisTemplate.opsForSet();
            String followerKey = "user:followed:" + followed_id;
            Set<String> followers = setOperations.members(followerKey);
            List<Long> follower_id_list = new ArrayList<>();
            List<FollowData> follower_result_list = new ArrayList<>();

            // String -> Long
             for(String follower : followers) {
                 follower_id_list.add(Long.parseLong(follower));
             };

             // 유저 id로 닉네임 조회
             String sql = "Select nickname from UserEntity where id=";
             boolean flag = false;
             for(int i=0; i<follower_id_list.size(); i++) {
                 flag = true;
                 sql += follower_id_list.get(i);
                 if(i != follower_id_list.size()-1) sql += " OR id=";
             }

             if(flag) { // 팔로워가 1명 이상인 경우 ( 팔로워가 존재하는 경우 )
                 Query query = entityManager.createQuery(sql);
                 List<String> follower_nickname_list = query.getResultList();
                 for(int i=0; i<follower_nickname_list.size(); i++) {
                     follower_result_list.add(new FollowData(follower_id_list.get(i), follower_nickname_list.get(i)));
                 }
             }

            FollowedList followedDto = FollowedList.builder()
                    .cnt(follower_result_list.size())
                    .followed_users(follower_result_list)
                    .build();

            return followedDto;
        } catch (Exception e) {
            log.info("[UserService] getFollowerIdService Error : " + e.getMessage());
            throw new SelectFollowerFailedException();
        }
    }

    // 내가 팔로잉하는 유저 조회 ( Id )
    public FollowingList getFollowingIdService(Long following_id) {
        try {
            SetOperations<String, String> setOperations = redisTemplate.opsForSet();
            String followingKey = "user:following:" + following_id;
            Set<String> following_users = setOperations.members(followingKey);
            List<Long> following_id_list = new ArrayList<>();
            List<FollowData> following_result_list = new ArrayList<>();

            // String -> Long
            for(String follower : following_users) {
                following_id_list.add(Long.parseLong(follower));
            };

            // 유저 id로 닉네임 조회
            String sql = "Select nickname from UserEntity where id=";
            boolean flag = false;
            for(int i=0; i<following_id_list.size(); i++) {
                flag = true;
                sql += following_id_list.get(i);
                if(i != following_id_list.size()-1) sql += " OR id=";
            }

            if(flag) { // 팔로워가 1명 이상인 경우 ( 팔로워가 존재하는 경우 )
                Query query = entityManager.createQuery(sql);
                List<String> following_nickname_list = query.getResultList();
                for(int i=0; i<following_nickname_list.size(); i++) {
                    following_result_list.add(new FollowData(following_id_list.get(i), following_nickname_list.get(i)));
                }
            }

            FollowingList followingDto = FollowingList.builder()
                    .cnt(following_result_list.size())
                    .following_users(following_result_list)
                    .build();

            return followingDto;

        } catch (Exception e) {
            log.info("[UserService] getFollowingIdService Error : " + e.getMessage());
            throw new SelectFollowingUsersFailedException();
        }
    }
}
