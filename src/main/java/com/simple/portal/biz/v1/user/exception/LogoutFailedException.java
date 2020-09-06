package com.simple.portal.biz.v1.user.exception;

import com.simple.portal.biz.v1.user.UserConst;

public class LogoutFailedException extends RuntimeException {
    public LogoutFailedException() {
        super(UserConst.LOGOUT_ERROR);
    }
}
