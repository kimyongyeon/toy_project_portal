package com.simple.portal.biz.v1.user.exception;

import com.simple.portal.biz.v1.user.UserConst;

public class FollowFailedException extends RuntimeException{
    public FollowFailedException ( ) {
        super(UserConst.FAILED_FOLLOW);
    }
}
