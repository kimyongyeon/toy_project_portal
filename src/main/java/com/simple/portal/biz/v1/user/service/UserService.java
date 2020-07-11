package com.simple.portal.biz.v1.user.service;

import com.simple.portal.biz.v1.user.UserConst;
import com.simple.portal.biz.v1.user.entity.UserEntity;
import com.simple.portal.biz.v1.user.exception.*;
import com.simple.portal.biz.v1.user.repository.UserRepository;
import com.simple.portal.common.Interceptor.JwtUtil;
import com.simple.portal.util.CustomMailSender;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class UserService {
    private UserRepository userRepository;
    private CustomMailSender mailSender;
    private JwtUtil jwtUtil;

    @Autowired
    public void UserController(UserRepository userRepository, CustomMailSender mailSender, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.mailSender = mailSender;
        this.jwtUtil = jwtUtil;
    }

    public List<UserEntity> userFindAllService( ) {
        try {
            return userRepository.findAll();
        }
        catch(Exception e) {
            log.info("[UserService] userFindAllService Error : " + e.getMessage());
            throw new SelectUserFailedException();
        }
    }

     // 유저의 기본키로 유저 조회
    public UserEntity userFineOneService(Long id) {
        try {
            return userRepository.findById(id).get();
        } catch (Exception e) {
            log.info("[UserService] userFindOneService Error : " + e.getMessage());
            throw new SelectUserFailedException();
        }
    }

    @Transactional
    public void createUserService(UserEntity user, MultipartFile file) {
        try {
            user.setCreated(LocalDateTime.now());
            user.setUpdated(LocalDateTime.now());
            user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt())); // 비밀번호 암호화

            String imgDir = "/data/profile/" + user.getUserId() + "-profileImg.png";
            file.transferTo(new File(imgDir)); // 해당 경로에 파일 생성
            user.setProfileImg(imgDir); // 프로필 세팅
            userRepository.save(user);
            try {
                mailSender.sendMail(user.getUserId()); // 회원가입 후 해당 이메일로 인증 메일보냄
            } catch (Exception e) {
                log.info("[UserService] emailSend Error : " + e.getMessage());
                throw new EmailSendFailedException();
            }
        } catch (Exception e) {
            log.info("[UserService] createUserService Error : " + e.getMessage());
            throw new CreateUserFailedException();
        }
    };

    public void updateUserService(UserEntity user, MultipartFile file) {
        try {
            user.setUpdated(LocalDateTime.now());

            //기존 프로필 삭제하고 새 프로필 사진 저장
            File deleteProfileImg = new File(user.getProfileImg());
            if(deleteProfileImg.exists()) {
                deleteProfileImg.delete();
            }

            String imgDir = "/data/profile/" + user.getUserId() + "-profileImg.png";
            file.transferTo(new File(imgDir)); // 해당 경로에 파일 생성
            user.setProfileImg(imgDir); // 프로필 세팅
            userRepository.save(user);
        } catch (Exception e) {
            log.info("[UserService] updateUserService Error : " + e.getMessage());
            throw new UpdateUserFailedException();
        }
    };

    public void deleteUserService(Long id) {
        try {
            userRepository.deleteById(id);
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
}
