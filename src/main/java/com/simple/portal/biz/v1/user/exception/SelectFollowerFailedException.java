package com.simple.portal.biz.v1.user.exception;

import com.simple.portal.biz.v1.user.UserConst;

public class SelectFollowerFailedException extends RuntimeException{
    public SelectFollowerFailedException( ) {
        super(UserConst.FAILED_SELECT_FOLLOWERS);
    }
}
