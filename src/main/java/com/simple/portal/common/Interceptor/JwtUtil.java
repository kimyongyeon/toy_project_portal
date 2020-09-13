package com.simple.portal.common.Interceptor;

import com.simple.portal.biz.v1.user.dto.AccressTokenDto;
import com.simple.portal.biz.v1.user.exception.TokenCreateFailedException;
import com.simple.portal.biz.v1.user.exception.TokenVaildFailedException;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Header
 *   typ : 토큰 타입을 지정 ex) JWT
 *   alg : 알고리즘 방식을 지정하며, 서명과 토큰 검증에 사용
 */
/**
 * Payload
 *   iss : 토큰 발급자 ( issuer )
 *   sub : 토큰 제목  ( subject )
 *   aud : 토큰 대상자 ( audience )
 *   exp : 토큰 만료 시간 ex) 1480849147370
 *   nbf : 토큰 활설 날짜
 *   iat : 토큰 발급시간
 */

@Slf4j
@Component
public class JwtUtil {

    @Value("${jwt.token.key}")
    private String key;

    @Autowired
    @Qualifier("redisTemplate_token")
    private RedisTemplate<String, String> redisTemplate_token;

    public String createAccessToken(String userId, char role) {

        Date EXPIRE_TIME = new Date(System.currentTimeMillis() + (1000*60*10)); // Access Token은 유효시간 10분

        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        Map<String, Object> payloads = new HashMap<>();
        payloads.put("iss", "KimCoding");
        payloads.put("sub", "okky-project-jwt-key"); // 발행자
        payloads.put("userId", userId);
        payloads.put("Role", String.valueOf(role));
        payloads.put("tokenType", "Access-Token");

        String jwt = null;
        try {
            jwt = Jwts.builder()
                    .setHeader(headers)
                    .setClaims(payloads)
                    .setExpiration(EXPIRE_TIME)
                    .signWith(SignatureAlgorithm.HS256, key.getBytes())
                    .compact();
            return jwt;
        } catch (Exception e) {
            log.info("[JwtUtil] create Token Error : " + e.getMessage());
            throw new TokenCreateFailedException();
        }
    };

    // refresh token은 redis에 넣어줌
    public String createRefreshToken(String userId) {
        Date EXPIRE_TIME = new Date(System.currentTimeMillis() + (1000*60*30*4)); // Refresh Token은 유효시간 2시간

        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        // refresh 토큰은 userId 필드 없음.
        Map<String, Object> payloads = new HashMap<>();
        payloads.put("iss", "KimCoding");
        payloads.put("sub", "okky-project-jwt-key"); // 발행자
        payloads.put("tokenType", "Refresh-Token");

        String jwt = null;
        try {
            jwt = Jwts.builder()
                    .setHeader(headers)
                    .setClaims(payloads)
                    .setExpiration(EXPIRE_TIME)
                    .signWith(SignatureAlgorithm.HS256, key.getBytes())
                    .compact();

            // refresh Token은 redis에 넣어줌 ( refresh:userId )
            ValueOperations<String, String> refreshTokenList = redisTemplate_token.opsForValue();
            String refreshTokenKey = "refresh:"+userId;
            refreshTokenList.set(refreshTokenKey, jwt);

            Long remainTime = getRemainTime(EXPIRE_TIME);
            redisTemplate_token.expire(refreshTokenKey, remainTime, TimeUnit.SECONDS); // 해당 토큰의 유효시간 끝나면 redis에서 삭제.

            return jwt;
        } catch (Exception e) {
            log.info("[JwtUtil] create Token Error : " + e.getMessage());
            throw new TokenCreateFailedException();
        }
    }

    public AccressTokenDto checkAccessToken(String jwtTokenString) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(key.getBytes())
                    .parseClaimsJws(jwtTokenString)
                    .getBody(); // catch에 안걸린다면 해당 토큰은 유효한 토큰이다.

            Date expiration = claims.getExpiration();
            Long remainTime = getRemainTime(expiration);

            String userId = claims.get("userId", String.class);
            String strRole = claims.get("Role", String.class);
            char role = strRole.charAt(0);

            Boolean isExpired = false;

            return new AccressTokenDto(userId, role, isExpired);
        } catch (ExpiredJwtException exception){
            log.info("[JwtUtil] exception : 토큰 만료 ");
            String userId = exception.getClaims().get("userId", String.class);
            String strRole = exception.getClaims().get("Role", String.class);
            char role = strRole.charAt(0);
            Boolean isExpired = true;

            Long remainTime = 1000*60*30*4L;
            return new AccressTokenDto(userId, role, isExpired);
        } catch (JwtException exception) {
            log.info("[JwtUtil] exception : 토큰 변조 ");
            throw new TokenVaildFailedException();
        }
    }

    // refresh:userId 키로 저장된 refresh 토큰이 유효한지 확인
    public void checkRefreshToken(String jwtRefreshTokenString) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(key.getBytes())
                    .parseClaimsJws(jwtRefreshTokenString)
                    .getBody(); // catch에 안걸린다면 해당 토큰은 유효한 토큰이다.
        } catch (ExpiredJwtException exception){
            log.info("[checkRefreshToken] exception : 토큰 만료 ");
            throw new TokenVaildFailedException();
        } catch (JwtException exception) {
            log.info("[checkRefreshToken] exception : 토큰 변조 ");
            throw new TokenVaildFailedException();
        }
    }

    public void checkBlackList(String userId, String accessToken) {
        ListOperations<String, String> blackList = redisTemplate_token.opsForList();
        String tokenBlackListKey = "blackList:"+userId;
        try {
            List<String> inRedisBlackList = blackList.range(tokenBlackListKey, 0, -1);
            for(int i=0; i<inRedisBlackList.size(); i++) {
                if(inRedisBlackList.contains(accessToken)) throw new Exception(); // 해당 토큰이 blackList에 존재하는 경우
            }
        } catch (Exception e) {
            log.info("[isTokenValid] exception : BlackList에 추가된 토큰 ");
            throw new TokenVaildFailedException();
        }
    }

    public Long getRemainTime(Date expiration) {
        Long timeNow = new Date(System.currentTimeMillis()).getTime();
        Long expireTime = expiration.getTime();
        Long remainTime = (expireTime - timeNow)/(1000);
        return remainTime;
    }
}