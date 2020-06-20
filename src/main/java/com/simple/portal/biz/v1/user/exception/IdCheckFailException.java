package com.simple.portal.biz.v1.user.exception;

import com.simple.portal.biz.v1.user.UserConst;

public class IdCheckFailException extends RuntimeException{
    public IdCheckFailException ( ) { super(UserConst.FIND_USER_ERROR);}
}

