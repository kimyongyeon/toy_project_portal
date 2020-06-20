package com.simple.portal.biz.v1.user.exception;

import com.simple.portal.biz.v1.user.UserConst;

public class SelectUserFailedException extends RuntimeException {
    public SelectUserFailedException( ) {
        super(UserConst.NO_USER);
    }
}
