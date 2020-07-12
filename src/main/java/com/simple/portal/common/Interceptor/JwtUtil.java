package com.simple.portal.common.Interceptor;

import com.simple.portal.biz.v1.user.exception.TokenCreateFailedException;
import com.simple.portal.biz.v1.user.exception.TokenVaildFailedException;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtUtil {

    private String key = "okkyProject";
    private Date EXPIRE_TIME = new Date(System.currentTimeMillis() + (1000*60*30)); // 유효시간 30분

    public String createToken(String userId) {

        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        Map<String, Object> payloads = new HashMap<>();
        payloads.put("userId", userId);

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

    public String checkToken(String jwtTokenString) throws InterruptedException {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(key.getBytes())
                    .parseClaimsJws(jwtTokenString)
                    .getBody(); // catch에 안걸린다면 해당 토큰은 유효한 토큰이다.

            //Date expiration = claims.getExpiration();
            String userId = claims.get("userId", String.class);
            return userId;
        } catch (ExpiredJwtException exception){
            log.info("[JwtUtil] exception : 토큰 만료 ");
            throw new TokenVaildFailedException();
        } catch (JwtException exception) {
            log.info("[JwtUtil] exception : 토큰 변조 ");
            throw new TokenVaildFailedException();
        }
    }
}