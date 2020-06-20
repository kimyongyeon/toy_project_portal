package com.simple.portal.biz.v1.user.exception;

import com.simple.portal.biz.v1.user.UserConst;

public class LoginFailedException extends RuntimeException {
    public LoginFailedException( ) { super(UserConst.LOGIN_ERROR); }
}