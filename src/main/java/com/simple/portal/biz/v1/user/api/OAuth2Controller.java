package com.simple.portal.biz.v1.user.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simple.portal.biz.v1.user.UserConst;
import com.simple.portal.biz.v1.user.dto.LoginTokenDto;
import com.simple.portal.biz.v1.user.dto.OAuthDto;
import com.simple.portal.biz.v1.user.exception.UserAuthCheckFailedException;
import com.simple.portal.biz.v1.user.repository.UserRepository;
import com.simple.portal.biz.v1.user.service.Oauth2Service;
import com.simple.portal.biz.v1.user.service.UserService;
import com.simple.portal.common.ApiResponse;
import com.simple.portal.common.Interceptor.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@Slf4j
@CrossOrigin
public class OAuth2Controller {

    private UserRepository userRepository;
    private UserService userService;
    private JwtUtil jwtUtil;
    private ApiResponse apiResponse;
    private Oauth2Service oauth2Service;

    @Autowired
    public OAuth2Controller(UserRepository userRepository, UserService userService, ApiResponse apiResponse, JwtUtil jwtUtil, Oauth2Service oauth2Service) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.apiResponse = apiResponse;
        this.jwtUtil = jwtUtil;
        this.oauth2Service = oauth2Service;
    }

    @GetMapping("/")
    public String home( ) {
        return "home";
    }

    @GetMapping("/login")
    public String login( ) {
        return "login";
    }

    // oauth 인증이 완료되고 해당 유저를 db에 저장하고 jwt 토큰을 만들어서 클라이언트로 전송
    @ResponseBody
    @GetMapping({"/loginSuccess"})

    @Transactional
    public ResponseEntity<ApiResponse> loginSuccess(OAuth2AuthenticationToken authentication) {

        Map<String, Object> oauthMap = authentication.getPrincipal().getAttributes();
        LoginTokenDto loginTokenDto = oauth2Service.OauthUserLoginService(oauthMap);
        String email = (String) oauthMap.get("email");
        String platform = (String) oauthMap.get("platform");
        String userId = email + ":" + platform;

        apiResponse.setMsg(UserConst.SUCCESS_LOGIN);
        Map<String, String> obj = new HashMap<>();
        obj.put("userId", userId);
        obj.put("Role", "Y"); // 일반 유저
        obj.put("access-token", loginTokenDto.getAccessToken());
        obj.put("refresh-token", loginTokenDto.getRefreshToken());
        apiResponse.setBody(obj);  // user_id 기반 토큰 생성
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/loginFailure")
    public String loginFailure( ) {
        return "loginFailure";
    }
}