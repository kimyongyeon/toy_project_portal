package com.simple.portal.biz.v1.user.exception;

import com.simple.portal.biz.v1.user.UserConst;

public class LoginFailException extends RuntimeException {
    public LoginFailException( ) { super(UserConst.LOGIN_ERROR); }
}