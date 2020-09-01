package com.simple.portal.biz.v1.user.exception;

import com.simple.portal.biz.v1.user.UserConst;

public class UpdateActivityScoreFailedException extends RuntimeException{
    public UpdateActivityScoreFailedException() { super(UserConst.FAILED_SELECT_ACTIVITY_SCORE);}
}
