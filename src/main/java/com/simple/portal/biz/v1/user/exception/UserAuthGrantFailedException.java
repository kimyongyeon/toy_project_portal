package com.simple.portal.biz.v1.user.exception;

import com.simple.portal.biz.v1.user.UserConst;

public class UserAuthGrantFailedException extends RuntimeException{
    public UserAuthGrantFailedException( ) {
        super(UserConst.FAILED_GRANT_USER_AUTH);
    }
}
