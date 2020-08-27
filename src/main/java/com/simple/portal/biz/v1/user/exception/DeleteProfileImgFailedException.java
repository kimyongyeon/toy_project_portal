package com.simple.portal.biz.v1.user.exception;

import com.simple.portal.biz.v1.user.UserConst;

public class DeleteProfileImgFailedException extends RuntimeException {
    public DeleteProfileImgFailedException( ) {
        super(UserConst.FAILED_DELETE_PROFILE_IMG);
    }
}
