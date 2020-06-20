package com.simple.portal.biz.v1.user.exception;

import com.simple.portal.biz.v1.user.UserConst;

public class IdCheckFailedException extends RuntimeException{
    public IdCheckFailedException( ) { super(UserConst.FIND_USER_ERROR);}
}

