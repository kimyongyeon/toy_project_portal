package com.simple.portal.biz.v1.note.exception;

import com.simple.portal.biz.v1.note.NoteConst;

public class InputRequiredException extends RuntimeException{
    public InputRequiredException() {
        super(NoteConst.FAIL_REQUIRED_VALUE);
    }
}
