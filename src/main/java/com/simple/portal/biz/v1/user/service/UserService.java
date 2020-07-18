package com.simple.portal.biz.v1.user.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.simple.portal.biz.v1.user.UserConst;
import com.simple.portal.biz.v1.user.dto.UserDto;
import com.simple.portal.biz.v1.user.entity.QUserEntity;
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
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class UserService {
    private UserRepository userRepository;
    private CustomMailSender mailSender;
    private JwtUtil jwtUtil;
    private RedisTemplate<String, String> redisTemplate;
    private JPAQueryFactory jpaQueryFactory;


    @Autowired
    public void UserController(UserRepository userRepository, CustomMailSender mailSender, JwtUtil jwtUtil, RedisTemplate redisTemplate, JPAQueryFactory jpaQueryFactory) {
        this.userRepository = userRepository;
        this.mailSender = mailSender;
        this.jwtUtil = jwtUtil;
        this.redisTemplate = redisTemplate;
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<UserDto> userFindAllService( ) {
        try {
            List<UserEntity> userEntityList = userRepository.findAll();
            List<UserDto> userDtoList = new ArrayList<>();
            for(int i=0; i<userEntityList.size(); i++) {
               userDtoList.add(UserDto.builder()
                       .id(userEntityList.get(i).getId())
                       .userId(userEntityList.get(i).getUserId())
                       .nickname(userEntityList.get(i).getNickname())
                       .gitAddr(userEntityList.get(i).getGitAddr())
                       .profileImg(userEntityList.get(i).getProfileImg())
                       .activityScore(userEntityList.get(i).getActivityScore())
                       .authority(userEntityList.get(i).getAuthority())
                       .updated(DateFormatUtil.makeNowTimeStamp())
                       .build());
            }

            return userDtoList;
        }
        catch(Exception e) {
            log.info("[UserService] userFindAllService Error : " + e.getMessage());
            throw new SelectUserFailedException();
        }
    }

     // 유저의 기본키로 유저 조회
    public UserDto userFineOneService(Long id) {
        try {

            UserEntity userEntity = userRepository.findById(id).get();
            UserDto userDto = UserDto.builder()
                    .id(userEntity.getId())
                    .userId(userEntity.getUserId())
                    .nickname(userEntity.getNickname())
                    .gitAddr(userEntity.getGitAddr())
                    .profileImg(userEntity.getProfileImg())
                    .activityScore(userEntity.getActivityScore())
                    .authority(userEntity.getAuthority())
                    .updated(DateFormatUtil.makeNowTimeStamp())
                    .build();

            return userDto;
        } catch (Exception e) {
            log.info("[UserService] userFindOneService Error : " + e.getMessage());
            throw new SelectUserFailedException();
        }
    }

    @Transactional
    public void createUserService(UserEntity user, MultipartFile file) {
        try {
            String imgDir = "/E:\\file_test\\" + user.getUserId() + "-profileImg.png";
            file.transferTo(new File(imgDir)); // 해당 경로에 파일 생성

            // 빌더 패턴 적용
            UserEntity insertUser = UserEntity.builder()
                    .userId(user.getUserId())
                    .nickname(user.getNickname())
                    .password(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt())) // 비밀번호
                    .gitAddr(user.getGitAddr())
                    .profileImg(imgDir)
                    .activityScore(user.getActivityScore())
                    .authority(user.getAuthority())
                    .created(DateFormatUtil.makeNowTimeStamp())
                    .updated(DateFormatUtil.makeNowTimeStamp())
                    .build();

            userRepository.save(insertUser);
            try {
                mailSender.sendJoinMail("Okky 회원 가입 완료 메일 !", user.getUserId()); // 회원가입 후 해당 이메일로 인증 메일보냄
            } catch (Exception e) {
                log.info("[UserService] emailSend Error : " + e.getMessage());
                throw new EmailSendFailedException();
            }
        } catch (Exception e) {

            log.info("[UserService] createUserService Error : " + e.getMessage());
            throw new CreateUserFailedException();
        }
    };

    public void updateUserService(UserDto user, MultipartFile file) {
        try {
            String imgDir = "/E:\\file_test\\" + user.getUserId() + "-profileImg.png";
            file.transferTo(new File(imgDir)); // 해당 경로에 파일 생성

            UserEntity originUser = userRepository.findById(user.getId()).get();

            // 빌더 패턴 적용
            UserEntity updateUser = UserEntity.builder()
                    .id(originUser.getId())
                    .userId(originUser.getUserId())
                    .nickname(user.getNickname()) // 변경 가능
                    .password(originUser.getPassword())
                    .gitAddr(user.getGitAddr()) // 변경 가능
                    .profileImg(imgDir) // 변경 가능
                    .activityScore(originUser.getActivityScore())
                    .authority(originUser.getAuthority())
                    .created(originUser.getCreated())
                    .updated(DateFormatUtil.makeNowTimeStamp())
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

            userRepository.deleteById(id);
            File deleteFile = new File(imgDir);
            if (deleteFile.exists()) { // 프로필 이미지 삭제
                deleteFile.delete();
            };

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
            userRepository.updatePassword(id,  BCrypt.hashpw(newPassword, BCrypt.gensalt()));
        } catch (Exception e) {
            log.info("[UserService] updateUserPassword Error : " + e.getMessage());
            throw new UpdatePasswordFailedException();
        }
    }

    // 비밀번호 찾기 ( = 새로운 비밀번호 전송 )
    @Transactional
    public void findUserPasswordService(Long id, String user_id) {
        try {
            // 랜덤값으로 비밀번호 변경 후 -> 이메일 발송
            String randomValue = ApiHelper.getRandomString(); // 이 값을 메일로 전송
            userRepository.updatePassword(id, BCrypt.hashpw(randomValue, BCrypt.gensalt()));
            log.info("new Password : " + randomValue);
            try {
                mailSender.sendNewPwMail("신규 비밀번호 안내 !", user_id, randomValue); // 회원가입 후 해당 이메일로 인증 메일보냄
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
    public void followService(Long following_id, Long followed_id) {
        try {
            redisTemplate.execute(
                    new SessionCallback<Object>() {
                        @Override
                        @SuppressWarnings("unchecked")
                        public Object execute(RedisOperations operations) throws DataAccessException {
                            operations.multi(); // 트랜잭션 start
                            SetOperations setOperations = operations.opsForSet();
                            String followingKey = "user:following:" + following_id;
                            String followerKey = "user:followed:" + followed_id;
                            setOperations.add(followingKey, String.valueOf(followed_id));
                            setOperations.add(followerKey, String.valueOf(following_id));
                            operations.exec(); // 트랜잭션 commit
                            return null;
                        }
                    }
            );
        } catch (Exception e) {
            log.info("[UserService] followService Error : " + e.getMessage());
            throw new FollowFailedException();
        }
    }

    // 언팔로우하기
    public void unfollowService(Long followed_id, Long following_id) {
        try {
            redisTemplate.execute(
                    new SessionCallback<Object>() {
                        @Override
                        @SuppressWarnings("unchecked")
                        public Object execute(RedisOperations operations) throws DataAccessException {
                            operations.multi(); // 트랜잭션 start
                            SetOperations setOperations = operations.opsForSet();
                            String followingKey = "user:following:" + following_id;
                            String followerKey = "user:followed:" + followed_id;
                            setOperations.remove(followingKey, String.valueOf(followed_id));
                            setOperations.remove(followerKey, String.valueOf(following_id));
                            operations.exec(); // 트랜잭션 commit
                            return null;
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
            log.info("[UserService] unfollowService Error : " + e.getMessage());
            throw new UnfollowFailedException();
        }
    }

    // 내 팔로워 조회 ( ID )
    public List<Long> getFollowerIdService(Long followed_id) {
        try {
            SetOperations<String, String> setOperations = redisTemplate.opsForSet();
            String followerKey = "user:followed:" + followed_id;
            Set<String> followers = setOperations.members(followerKey);
            List<Long> follower_list = new ArrayList<>();

             for(String follower : followers) {
                 follower_list.add(Long.parseLong(follower));
             };
            return follower_list;
        } catch (Exception e) {
            log.info("[UserService] getFollowerIdService Error : " + e.getMessage());
            throw new SelectFollowerFailedException();
        }
    }

    // 내 팔로워 조회 ( 닉네임 )
    public List<String> getFollowerNicknameService(List<Long> follower_id_list) {
        return new ArrayList<>();
    }

    // 내가 팔로잉하는 유저 조회 ( Id )
    public List<Long> getFollowingIdService(Long following_id) {
        try {
            SetOperations<String, String> setOperations = redisTemplate.opsForSet();
            String followingKey = "user:following:" + following_id;
            Set<String> following_users = setOperations.members(followingKey);
            List<Long> following_list = new ArrayList<>();

            for(String follower : following_users) {
                following_list.add(Long.parseLong(follower));
            };
            return following_list;
        } catch (Exception e) {
            log.info("[UserService] getFollowingIdService Error : " + e.getMessage());
            throw new SelectFollowingUsersFailedException();
        }
    }

    // 내가 팔로잉 하는 유저 조회 ( 닉네임 )
    public List<String> getFollowingNicknameService(List<Long> following_id_list) {
        return new ArrayList<>();
    }

}
