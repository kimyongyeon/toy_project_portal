package com.simple.portal.biz.v1.user.exception;

import com.simple.portal.biz.v1.user.UserConst;

public class UploadProfileImgFailedException extends RuntimeException {
    public UploadProfileImgFailedException( ) {
        super(UserConst.FAILED_UPLOAD_PROFILE_IMG);
    }
}
