package com.simple.portal.biz.v1.user.api;

import com.google.gson.JsonObject;
import com.simple.portal.biz.v1.user.UserConst;
import com.simple.portal.biz.v1.user.entity.UserEntity;
import com.simple.portal.biz.v1.user.exception.ParamInvalidException;
import com.simple.portal.biz.v1.user.service.UserService;
import com.simple.portal.common.ApiResponse;
import com.simple.portal.common.Interceptor.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.HashAttributeSet;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/v1/api/user")
public class UserController {

    private UserService userService;
    private ApiResponse apiResponse;

    @Autowired
    public void UserController(UserService userService, ApiResponse apiResponse) {
        this.userService = userService;
        this.apiResponse = apiResponse;
    }

    //생성된 토큰 테스트
    @GetMapping("/token")
    public void token_test(HttpServletRequest request) {
        log.info("token");
        log.info((String)request.getAttribute("userId"));
    };

    //전체 유저 조회
    @GetMapping("")
    public ResponseEntity<?> userFindAll( ) {
        log.info("[GET] /user/ - /userFindAll/");

        apiResponse.setMsg(UserConst.SUCCESS_SELECT_USER);
        apiResponse.setBody(userService.userFindAllService());
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    };

    //특정 유저 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> userFindOne(@PathVariable @NotNull Long id) {
        log.info("[GET] /user/{id}" + id + "/userFindOne/");

        apiResponse.setMsg(UserConst.SUCCESS_SELECT_USER);
        apiResponse.setBody(userService.userFineOneService(id));
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    // 유저 등록 ( 회원 가입 )
    @PostMapping("")
    public ResponseEntity<ApiResponse> userCreate(@Valid @RequestBody UserEntity user, BindingResult bindingResult) {
        log.info("[POST] /user/ userCreateAPI" + "[RequestBody] " + user.toString());

        // client가 요청 잘못했을때 (파라미터 ) - 400
        if(bindingResult.hasErrors()) {
            String errMsg = bindingResult.getAllErrors().get(0).getDefaultMessage(); // 첫번째 에러로 출력
            throw new ParamInvalidException(errMsg);
        }

        userService.createUserService(user);
        apiResponse.setMsg(UserConst.SUCCESS_CREATE_USER);
        apiResponse.setBody("");
        return  new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    //유저 수정
    @PutMapping("")
    public ResponseEntity<ApiResponse> userUpdate(@Valid @RequestBody UserEntity user, BindingResult bindingResult) {
        log.info("[PUT] /user/ userUpdateApi" + "[RequestBody] " + user);

        // client가 요청 잘못했을때 (파라미터 ) - 400
        if(bindingResult.hasErrors()) {
            String errMsg = bindingResult.getAllErrors().get(0).getDefaultMessage(); // 첫번째 에러로 출력
            throw new ParamInvalidException(errMsg);
        }

        userService.updateUserService(user);
        apiResponse.setMsg(UserConst.FAILED_UPDATE_USER);
        apiResponse.setBody("");
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    //유저 삭제 - 회원 탈퇴
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> userDelete(@PathVariable @NotNull Long id) {
        log.info("[DELETE] /user/ " + id + " /userDelete");

        userService.deleteUserService(id);
        apiResponse.setMsg(UserConst.SUCCESS_DELETE_USER);
        apiResponse.setMsg("");
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    // 아이디 중복 체크 ( 회원 가입 )
    @GetMapping("/check/{id}")
    public ResponseEntity<ApiResponse> userIdCheck(@PathVariable("id") String user_id) {
        log.info("[GET] /user/check/id " + user_id + " /userIdCheck");

        if(userService.idCheckService(user_id)) {
            apiResponse.setMsg(UserConst.EXIST_USER);
            apiResponse.setBody("");
            return new ResponseEntity(apiResponse, HttpStatus.OK);
        } else {
            apiResponse.setMsg(UserConst.NO_USER);
            apiResponse.setBody("");
            return new ResponseEntity(apiResponse, HttpStatus.OK);
        }
    };

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> userlogin(HttpServletRequest request) {

        String id = request.getParameter("id");
        String pw = request.getParameter("password");
        log.info("[POST] /user/login " + "[ID] :  "  + id + "[PW] : " + pw + " /userLogin");

        String token = userService.userLoginService(id, pw);
        apiResponse.setMsg(UserConst.SUCCESS_LOGIN);

        Map<String, String> obj = new HashMap<>();
        obj.put("token", token);
        apiResponse.setBody(obj);  // user_id 기반 토큰 생성
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }
}
