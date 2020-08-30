package com.simple.portal.biz.v1.user.exception;

import com.simple.portal.biz.v1.user.UserConst;

public class EmailCheckFailedException extends RuntimeException {
    public EmailCheckFailedException() { super(UserConst.NO_EMAIL);}
}
