package com.simple.portal.biz.v1.user.exception;

import com.simple.portal.biz.v1.user.UserConst;

public class TokenVaildFailedException extends RuntimeException{
    public TokenVaildFailedException( ) {
        super(UserConst.FAILED_TOKEN_VALIDATION);
    }
}
