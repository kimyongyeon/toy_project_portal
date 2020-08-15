package com.simple.portal.biz.v1.user.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
public class OAuth2Controller {
    @GetMapping("/")
    public String home( ) {
        return "home";
    }

    @GetMapping("/login")
    public String login( ) {
        return "login";
    }

    @ResponseBody
    @GetMapping({"/loginSuccess", "/hello"})
    public Object loginSuccess(OAuth2AuthenticationToken authentication) {
        //log.info("--------------------");
        //log.info("authentication = " + authentication.getPrincipal().getAttributes());
        //return authentication.getPrincipal().getAttributes();
        return authentication.getPrincipal().getAttributes();
    }

    @GetMapping("/loginFailure")
    public String loginFailure( ) {
        return "loginFailure";
    }
}
