package com.simple.portal.biz.v1.user.exception;

import com.simple.portal.biz.v1.user.UserConst;

public class UpdateUserFailedException extends RuntimeException {
    public UpdateUserFailedException( ) {
        super(UserConst.FAILED_UPDATE_USER);
    }
}
