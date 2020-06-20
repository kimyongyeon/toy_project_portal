package com.simple.portal.biz.v1.user.service;

import com.simple.portal.biz.v1.user.entity.UserEntity;
import com.simple.portal.biz.v1.user.exception.IdCheckFailException;
import com.simple.portal.biz.v1.user.exception.LoginFailException;
import com.simple.portal.biz.v1.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public void UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserEntity> userFindAllService( ) {
        return userRepository.findAll();
    }

    public UserEntity userFineOneService(Long id) {
        return userRepository.findById(id).get();
    }

    public void createUserService(UserEntity user) {
            user.setCreated(LocalDateTime.now());
            user.setUpdated(LocalDateTime.now());
            userRepository.save(user);
    };

    public void updateUserService(UserEntity user) {
            user.setUpdated(LocalDateTime.now());
            userRepository.save(user);
    };

    public void deleteUserService(Long id) {
        userRepository.deleteById(id);
    }

    public Boolean idCheckService(String user_id) {
        try {
            return userRepository.existsUserByUserId(user_id) == true ? true : false;
        } catch (Exception e) {
            log.info("[UserService] idCheckService Error : " + e.getMessage());
            throw new IdCheckFailException();
        }
    }

    @Transactional
    public Boolean userLoginService(String user_id, String password) {
        try {
            if(!userRepository.existsUserByUserId(user_id)) return false; // 아이디 존재안함
            else {
                UserEntity user = userRepository.findByUserId(user_id);
                String pwOrigin = user.getPassword();

                if(BCrypt.checkpw(password, pwOrigin)) return true;
                else return false;
            }

        } catch (Exception e) {
            log.info("[UserService] userLoginService Error : " + e.getMessage());
            throw new LoginFailException();
        }
    }
}
