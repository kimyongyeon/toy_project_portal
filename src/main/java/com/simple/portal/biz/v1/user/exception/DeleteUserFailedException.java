package com.simple.portal.biz.v1.user.exception;

import com.simple.portal.biz.v1.user.UserConst;

public class DeleteUserFailedException extends RuntimeException{
    public DeleteUserFailedException( ) {
        super(UserConst.FAILED_DELETE_USER);
    }
}
