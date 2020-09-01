package com.simple.portal.biz.v1.user.exception;

import com.simple.portal.biz.v1.user.UserConst;

public class UpdateProfileImgFailedException extends RuntimeException {
    public UpdateProfileImgFailedException( ) { super(UserConst.FAILED_DELETE_PROFILE_IMG);}
}
