package com.simple.portal.biz.v1.user.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OAuth2Controller {
    @GetMapping("/")
    public String home( ) {
        return "home";
    }

    @GetMapping("/login")
    public String login( ) {
        return "login";
    }

    @GetMapping({"/loginSuccess", "/hello"})
    public String loginSuccess( ) {
        return "hello";
    }

    @GetMapping("/loginFailure")
    public String loginFailure( ) {
        return "loginFailure";
    }
}
