package com.simple.portal.biz.v1.user.exception;

import com.simple.portal.biz.v1.user.UserConst;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException( ) {
        super(UserConst.NO_USER);
    }
}