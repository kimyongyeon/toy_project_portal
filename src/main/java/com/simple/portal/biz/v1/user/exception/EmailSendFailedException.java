package com.simple.portal.biz.v1.user.exception;

import com.simple.portal.biz.v1.user.UserConst;

public class EmailSendFailedException extends RuntimeException {
    public EmailSendFailedException( ) {
        super(UserConst.FAILED_EMAIL_SEND);
    }
}
