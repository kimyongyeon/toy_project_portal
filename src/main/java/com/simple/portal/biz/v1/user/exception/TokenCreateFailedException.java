package com.simple.portal.biz.v1.user.exception;

import com.simple.portal.biz.v1.user.UserConst;

public class TokenCreateFailedException extends RuntimeException{
    public TokenCreateFailedException( ) {
        super(UserConst.FAILED_CREATE_TOKEN);
    }
}
