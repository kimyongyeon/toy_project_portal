package com.simple.portal.biz.v1.user.exception;

import com.simple.portal.biz.v1.user.UserConst;

public class SelectUserPkFailedException extends RuntimeException {
    public SelectUserPkFailedException( ) {
        super(UserConst.FAILED_SELECT_USER_PK);
    }
}
