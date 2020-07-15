package com.simple.portal.biz.v1.user.exception;

import com.simple.portal.biz.v1.user.UserConst;

public class UpdatePasswordFailedException extends RuntimeException {
    public UpdatePasswordFailedException( ) {
        super(UserConst.FAILED_UPDATE_PASSWORD);
    }
}
