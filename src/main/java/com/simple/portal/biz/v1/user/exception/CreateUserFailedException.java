package com.simple.portal.biz.v1.user.exception;

import com.simple.portal.biz.v1.user.UserConst;

public class CreateUserFailedException extends RuntimeException {
    public CreateUserFailedException( ) {
        super(UserConst.FAILED_CREATE_USER);
    }
}
