package com.simple.portal.biz.v1.user.exception;

import com.simple.portal.biz.v1.user.UserConst;

public class UnfollowFailedException extends RuntimeException {
    public UnfollowFailedException( ) {
        super(UserConst.FAILED_UNFOLLOW);
    }
}
