package com.simple.portal.common.Interceptor;

import com.simple.portal.biz.v1.user.dto.AccressTokenDto;
import com.simple.portal.biz.v1.user.exception.TokenVaildFailedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    @Qualifier("redisTemplate_token")
    private RedisTemplate<String, String> redisTemplate_token;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        log.info("=========preHandler");

        ValueOperations<String, String> refreshTokenList = redisTemplate_token.opsForValue();
        ListOperations<String, String> blackList = redisTemplate_token.opsForList();

        String jwtAccessToken = request.getHeader("Authorization");
        String jwtRefreshToken = request.getHeader("RefreshToken");

        // Token 만료 및 변조 검사 -> 변조됬으면 재로그인 요청, 만료 됬으면 refreshToken 확인해서 재발급 여부 판단.
        AccressTokenDto tokenDto = jwtUtil.checkAccessToken(jwtAccessToken); // 토큰 재발급을 위해서 userId, role, isExpired..등 값이 필요함. ( 유효하거나, 만료된 토큰 )
        String userId = tokenDto.getUserId();
        char role = tokenDto.getRole();
        Boolean isExpired = tokenDto.getExpired();

        String tokenRefreshyKey = "refresh:"+userId;
        String tokenBlackListKey = "blackList:"+userId;

        // Token blackList 검사
        jwtUtil.checkBlackList(userId, jwtAccessToken);

        /**
         *  토큰 만료됬으면 재발급
         *  1. 유저가 입력한 refresh token에서 userId 뽑아냄
         *  2. 해당 userId로 redis 조회
         *  3. 2에서 조회한 refresh token과 유저가 입력한 refresh token이 일치하지 않거나 변조, 만료 되었다면 재로그인 요청
         *  3-1. 만약, 3이 아니라면 refresh token으로 accress token 재발급해서 client에게 return, 기존 accress-token 블랙리스트에 등록
         */

        // Access Token이 만료된 경우 -> Refresh Token을 이용해 Accress Token 재발급
        if (isExpired) {
            // 유저가 입력한 Refresh Token의 만료, 변조 확인
            jwtUtil.checkRefreshToken(jwtRefreshToken);
            String orifinRefreshToken = refreshTokenList.get(tokenRefreshyKey);

            // redis에 저장된 refresh token과 다른지 체크
            if (!jwtRefreshToken.equals(orifinRefreshToken)) {
                log.info("[JwtInterceptor] exception : 토큰 변조 ");
                throw new TokenVaildFailedException();
            }

            //Accress Token 재발급 ( 기존 토큰과 재발급된 토큰은 만료시간을 제외한 모든 값이 같다.)
            String newAccessToken = jwtUtil.createAccessToken(userId, role); // -> 이 값 클라로 뿌려줘야함
            log.info("newAccessToken : " + newAccessToken);

            // 만료된 Access Token은 black list에 등록 ( list 자료형으로 관리 )
            blackList.rightPush(tokenBlackListKey, jwtAccessToken);

            // 클라에게 refresh Token을 어떻게 뿌려줘야되나....
        }

       // request.setAttribute("userId", userId); // request에 userId를 세팅해서 컨트롤러에 넘김
       // request.setAttribute("token", jwtToken);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        log.info("=========postHandler");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        log.info("=========afterHandler");
    }
}