package com.simple.portal.biz.v1.user.exception;

import com.simple.portal.biz.v1.user.UserConst;

public class UserAuthCheckFailedException extends RuntimeException {
    public UserAuthCheckFailedException( ) {
        super(UserConst.FAILED_CHECK_USER_AUTH);
    }
}
