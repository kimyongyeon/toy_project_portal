package com.simple.portal.biz.v1.board.exception;

import com.simple.portal.biz.v1.board.BoardConst;

public class ItemGubunExecption extends RuntimeException{
    public ItemGubunExecption() {
        super(BoardConst.FAIL_ITEM_GB);
    }
}
