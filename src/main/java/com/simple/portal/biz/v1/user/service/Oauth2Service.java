package com.simple.portal.biz.v1.user.service;

import com.simple.portal.biz.v1.user.dto.OAuthDto;
import com.simple.portal.biz.v1.user.entity.UserEntity;
import com.simple.portal.biz.v1.user.exception.CreateUserFailedException;
import com.simple.portal.biz.v1.user.repository.UserRepository;
import com.simple.portal.common.Interceptor.JwtUtil;
import com.simple.portal.util.DateFormatUtil;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;

@Service
@Slf4j
public class Oauth2Service {
    private UserRepository userRepository;
    private JwtUtil jwtUtil;

    @Autowired
    public void Oauth2Service(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public String OauthUserLoginService(Map<String, Object> oauthMap) {

        try {
            String platform = (String) oauthMap.get("platform");
            String nickname = (String) oauthMap.get("nickname");
            String email = (String) oauthMap.get("email");
            String profile = (String) oauthMap.get("profile");

            Boolean isExist = userRepository.existsUserByUserId(email+":"+platform);
            if(isExist) { // 기존에 insert 된 경우
                String userId = email + ":" + platform;
                return jwtUtil.createToken(userId);
            } else {
                UserEntity insertUser = UserEntity.builder()
                        .userId(email + ":" + platform) // 이메일로 가입된 사람과 중복될 수 있으므로 뒤에 플랫폼 이름 붙여줌
                        .nickname(nickname)
                        .password(platform) // 비밀번호 ( oauth의 경우 사용안하므로 platform이름으로 대체)
                        .gitAddr("https://github.com") // default 깃 주소
                        .profileImg(profile)
                        .activityScore(0) // 초기점수 0 점
                        .authority('N') // 초기 권한 N
                        .created(DateFormatUtil.makeNowTimeStamp())
                        .updated(DateFormatUtil.makeNowTimeStamp())
                        .build();

                userRepository.save(insertUser);
                return jwtUtil.createToken(insertUser.getUserId());
            }
            // 기존에 유저가 있으면 등록안하고 없으면 등록 ( select -> insert )
        } catch (Exception e) {
            log.info("[Oauth2Service] createOauthUserService error : " + e.getMessage());
            throw new CreateUserFailedException();
        }
    }
}
