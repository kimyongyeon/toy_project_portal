package com.simple.portal.biz.v1.user.service;

import com.simple.portal.biz.v1.user.dto.LoginTokenDto;
import com.simple.portal.biz.v1.user.dto.OAuthDto;
import com.simple.portal.biz.v1.user.entity.UserEntity;
import com.simple.portal.biz.v1.user.exception.CreateUserFailedException;
import com.simple.portal.biz.v1.user.repository.UserRepository;
import com.simple.portal.common.Interceptor.JwtUtil;
import com.simple.portal.util.ActivityScoreConst;
import com.simple.portal.util.DateFormatUtil;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;

import static com.simple.portal.util.DateFormatUtil.makeNowTimeStamp;

@Service
@Slf4j
public class Oauth2Service {
    private UserRepository userRepository;
    private UserService userService;
    private JwtUtil jwtUtil;

    @Autowired
    public void Oauth2Service(UserRepository userRepository, UserService userService, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public LoginTokenDto OauthUserLoginService(Map<String, Object> oauthMap) {

        try {
            String platform = (String) oauthMap.get("platform");
            String nickname = (String) oauthMap.get("nickname");
            String email = (String) oauthMap.get("email");
            String profile = (String) oauthMap.get("profile");
            String userId = email + ":" + platform; // 이메일로 가입된 사람과 중복될 수 있으므로 뒤에 플랫폼 이름 붙여줌

            Boolean isExist = userRepository.existsUserByUserId(email+":"+platform);
            if(isExist) {  // 기존에 insert 된 경우
                UserEntity user = userRepository.findByUserId(userId);
                char userRole = user.getAuthority();
                String lastLoginTime = user.getLastLoginTime();
                String LastLoginDate = lastLoginTime.split(" ")[0]; // "lastLoginTime": "2020-08-29 17:53:56",
                String nowDate = makeNowTimeStamp().split(" ")[0];
                // 로그인의 경우 하루에 한번만 카운트 되도록 처리
                if(!LastLoginDate.equals(nowDate)) userService.updateActivityScore(userId, ActivityScoreConst.LOGIN_ACTIVITY_SCORE);
                userRepository.updateLastLoginTime(userId, makeNowTimeStamp()); // 최근 로그인 시간 update

                // 로그인시 Access Token, Refresh Token 모두 발급.
                String accessToken = jwtUtil.createAccessToken(user.getUserId(),  user.getAuthority());
                String refrehToken = jwtUtil.createRefreshToken(user.getUserId());
                return new LoginTokenDto(accessToken, refrehToken);

            } else { // Oauth의 경우 최초 로그인일때 가입과 동시에 로그인이 된다.
                UserEntity insertUser = UserEntity.builder()
                        .userId(userId)
                        .email(email)
                        .nickname(nickname)
                        .password(platform) // 비밀번호 ( oauth의 경우 사용안하므로 platform이름으로 대체)
                        .gitAddr("https://github.com") // default 깃 주소
                        .profileImg(profile)
                        .activityScore(0) // 초기점수 0 점
                        .authority('N') // 초기 권한 N
                        .created(makeNowTimeStamp())
                        .updated(makeNowTimeStamp())
                        .lastLoginTime(makeNowTimeStamp())
                        .build();

                userRepository.save(insertUser);
                userService.updateActivityScore(userId, ActivityScoreConst.LOGIN_ACTIVITY_SCORE);
                // 로그인시 Access Token, Refresh Token 모두 발급.
                String accessToken = jwtUtil.createAccessToken(insertUser.getUserId(), insertUser.getAuthority());
                String refrehToken = jwtUtil.createRefreshToken(insertUser.getUserId());
                return new LoginTokenDto(accessToken, refrehToken);
            }
            // 기존에 유저가 있으면 등록안하고 없으면 등록 ( select -> insert )
        } catch (Exception e) {
            log.info("[Oauth2Service] createOauthUserService error : " + e.getMessage());
            throw new CreateUserFailedException();
        }
    }
}
