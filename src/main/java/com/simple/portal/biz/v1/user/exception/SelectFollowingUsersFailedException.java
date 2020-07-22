package com.simple.portal.biz.v1.user.exception;

import com.simple.portal.biz.v1.user.UserConst;

public class SelectFollowingUsersFailedException extends RuntimeException {
    public SelectFollowingUsersFailedException( ) {
        super(UserConst.FAILED_SELECT_FOLLOWING_USERS);
    }
}
