package com.simple.portal.common;

import com.simple.portal.biz.v1.user.exception.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.simple.portal.common.CommonConst.*;

@ControllerAdvice
public class CommonExceptionHandler {
    @Value("${spring.profile.value}")
    private String profileActive;

    private boolean isProfileActive() {
        if (PROFILE_DEFAULT.equals(profileActive)) {
            return true;
        } else {
            return false;
        }
    }

    private String getMag(Exception e) {
        if (isProfileActive()) { // default
            return e.getMessage() == null ? MSG_NULL_MSG : e.getMessage();
        } else { // dev < stag, prod
            return PROD_EXCEPTION_MSG;
        }
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> runtimeException(RuntimeException e) {
        return new ResponseEntity<>(
                ApiResponse
                        .builder()
                        .code(CODE_RE)
                        .msg(getMag(e))
                        .body(BODY_BLANK)
                        .build()
                , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<?> dataAccessException(DataAccessException e) {
        return new ResponseEntity<>(
                ApiResponse
                        .builder()
                        .code(CODE_DAE)
                        .msg(getMag(e))
                        .body(BODY_BLANK)
                        .build()
                , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> exception(Exception e) {
        return new ResponseEntity<>(
                ApiResponse
                        .builder()
                        .code(CODE_E)
                        .msg(getMag(e))
                        .body(BODY_BLANK)
                        .build()
                , HttpStatus.BAD_REQUEST);
    }

    //유저 관련 Exception 처리
    @ExceptionHandler({CreateUserFailedException.class, UpdateUserFaileException.class,
            SelectUserFailedException.class, DeleteUserFailedException.class,
            IdCheckFailedException.class, LoginFailedException.class
    })
    public ResponseEntity<ApiResponse> userException(Exception e) {
        return new ResponseEntity<>(
                ApiResponse
                        .builder()
                        .code(CODE_USER)
                        .msg(getMag(e))
                        .body(BODY_BLANK)
                        .build()
                , HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
