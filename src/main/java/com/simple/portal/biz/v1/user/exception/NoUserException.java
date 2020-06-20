package com.simple.portal.biz.v1.user.exception;

import com.simple.portal.biz.v1.user.UserConst;

public class NoUserException extends RuntimeException {
    public NoUserException( ) {
        super(UserConst.NO_USER);
    }
}
