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
        return true;
//        if (PROFILE_DEFAULT.equals(profileActive)) {
//            return true;
//        } else {
//            return false;
//        }
    }
  
    public static final String CODE_RE = "502";
    public static final String CODE_DAE = "503";
    public static final String CODE_E = "500";
    public static final String CODE_USER_RE = "505"; // server error
    public static final String CODE_USER_C_E = "405"; // client error

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
                        .msg(e.getMessage())
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
                        .msg(e.getMessage())
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
                        .msg(e.getMessage())
                        .body(BODY_BLANK)
                        .build()
                , HttpStatus.BAD_REQUEST);
    }

    //유저 관련 Exception 처리 ( 500 )
    @ExceptionHandler({CreateUserFailedException.class, UpdateUserFailedException.class,
            SelectUserFailedException.class, SelectUserPkFailedException.class, DeleteUserFailedException.class,
            IdCheckFailedException.class,
            TokenCreateFailedException.class,
            UserAuthGrantFailedException.class,
            EmailSendFailedException.class,
            FindPasswordFailedException.class,
            FollowFailedException.class,
            UnfollowFailedException.class,
            SelectFollowerFailedException.class, SelectFollowingUsersFailedException.class,
            UploadProfileImgFailedException.class, DeleteProfileImgFailedException.class,
            UpdateActivityScoreFailedException.class,
            UpdateProfileImgFailedException.class,
            EmailCheckFailedException.class,
            LogoutFailedException.class,
    })
    public ResponseEntity<ApiResponse> user500Exception(Exception e) {
        return new ResponseEntity<>(
                ApiResponse
                        .builder()
                        .code(CODE_USER)
                        .msg(e.getMessage())
                        .body(BODY_BLANK)
                        .build()
                , HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //유저 관련 Exception 처리 ( 400 )
    @ExceptionHandler({TokenVaildFailedException.class, ParamInvalidException.class, LoginFailedException.class, UserNotFoundException.class})
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

    //유저 관련 Exception 처리 ( 401 - unauthorization )
    @ExceptionHandler({UserAuthCheckFailedException.class})
    public ResponseEntity<ApiResponse> user401Exception(Exception e) {
        return new ResponseEntity<>(
                ApiResponse
                        .builder()
                        .code(CODE_USER_C_E)
                        .msg(e.getMessage())
                        .body("")
                        .build()
                , HttpStatus.UNAUTHORIZED);
    }
}
