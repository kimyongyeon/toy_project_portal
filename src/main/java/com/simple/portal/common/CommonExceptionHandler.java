package com.simple.portal.common;

import com.simple.portal.biz.v1.user.exception.*;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CommonExceptionHandler {

    public static final String CODE_RE = "502";
    public static final String CODE_DAE = "503";
    public static final String CODE_E = "500";
    public static final String CODE_USER_RE = "505"; // server error
    public static final String CODE_USER_C_E = "405"; // client error

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> runtimeException(RuntimeException e) {
        return new ResponseEntity<>(
                ApiResponse
                        .builder()
                        .code(CODE_RE)
                        .msg(e.getMessage())
                        .body("")
                        .build()
                , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<?> dataAccessException(DataAccessException e) {
        return new ResponseEntity<>(
                ApiResponse
                        .builder()
                        .code(CODE_DAE)
                        .msg(e.getMessage())
                        .body("")
                        .build()
                , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> exception(Exception e) {
        return new ResponseEntity<>(
                ApiResponse
                        .builder()
                        .code(CODE_E)
                        .msg(e.getMessage())
                        .body("")
                        .build()
                , HttpStatus.BAD_REQUEST);
    }

    //유저 관련 Exception 처리 ( 500 )
    @ExceptionHandler({CreateUserFailedException.class, UpdateUserFailedException.class,
            SelectUserFailedException.class, DeleteUserFailedException.class,
            IdCheckFailedException.class,
            TokenCreateFailedException.class
    })
    public ResponseEntity<ApiResponse> user500Exception(Exception e) {
        return new ResponseEntity<>(
                ApiResponse
                        .builder()
                        .code(CODE_USER_RE)
                        .msg(e.getMessage())
                        .body("")
                        .build()
                , HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //유저 관련 Exception 처리 ( 400 )
    @ExceptionHandler({TokenVaildFailedException.class, ParamInvalidException.class, LoginFailedException.class})
    public ResponseEntity<ApiResponse> user400Exception(Exception e) {
        return new ResponseEntity<>(
                ApiResponse
                        .builder()
                        .code(CODE_USER_C_E)
                        .msg(e.getMessage())
                        .body("")
                        .build()
                , HttpStatus.BAD_REQUEST);
    }
}
