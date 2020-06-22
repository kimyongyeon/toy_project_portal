package com.simple.portal.biz.v1.user.exception;

import com.simple.portal.biz.v1.user.UserConst;

public class TokenCreateFailException extends RuntimeException{
    public TokenCreateFailException( ) {
        super(UserConst.FAILED_TOKEN);
    }
}
