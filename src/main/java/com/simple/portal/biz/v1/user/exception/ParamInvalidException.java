package com.simple.portal.biz.v1.user.exception;

import com.simple.portal.biz.v1.user.UserConst;

public class ParamInvalidException extends RuntimeException {
    public ParamInvalidException(String errorMsg) {
        super(errorMsg);
    }
}
