package com.simple.portal.biz.v1.user.exception;

import com.simple.portal.biz.v1.user.UserConst;

public class UpdateUserFaileException extends RuntimeException {
    public UpdateUserFaileException( ) {
        super(UserConst.FAILED_UPDATE_USER);
    }
}
