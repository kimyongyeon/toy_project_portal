package com.simple.portal.biz.v1.board.exception;

import com.simple.portal.biz.v1.board.BoardConst;

public class BoardDetailNotException extends RuntimeException {
    public BoardDetailNotException() {
        super(BoardConst.FAIL_BOARD_LIST);
    }
}
