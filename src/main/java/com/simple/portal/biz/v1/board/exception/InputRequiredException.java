package com.simple.portal.biz.v1.board.exception;

import com.simple.portal.biz.v1.board.BoardConst;

public class InputRequiredException extends RuntimeException{
    public InputRequiredException() {
        super(BoardConst.FAIL_REQUIRED_VALUE);
    }
}
