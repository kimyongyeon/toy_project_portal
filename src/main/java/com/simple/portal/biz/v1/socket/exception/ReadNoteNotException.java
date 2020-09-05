package com.simple.portal.biz.v1.socket.exception;

import com.simple.portal.biz.v1.socket.SocketConst;

public class ReadNoteNotException extends RuntimeException {
    public ReadNoteNotException() {
        super(SocketConst.ALL_READ_ALRAM);
    }
}
