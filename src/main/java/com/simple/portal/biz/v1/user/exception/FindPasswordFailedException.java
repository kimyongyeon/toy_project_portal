package com.simple.portal.biz.v1.user.exception;

import com.simple.portal.biz.v1.user.UserConst;

public class FindPasswordFailedException extends RuntimeException {
    public FindPasswordFailedException( ) {
        super(UserConst.FAILED_EMAIL_SEND);
    }
}
