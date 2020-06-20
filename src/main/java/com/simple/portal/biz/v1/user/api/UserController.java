package com.simple.portal.biz.v1.user.api;

import com.simple.portal.biz.v1.user.UserConst;
import com.simple.portal.biz.v1.user.entity.UserEntity;
import com.simple.portal.biz.v1.user.service.UserService;
import com.simple.portal.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/v1/api/user")
public class UserController {

    private UserService userService;
    @Qualifier("successApiResponse")
    private ApiResponse successApiResponse;

    @Autowired
    public void UserController(UserService userService, ApiResponse successApiResponse) {
        this.userService = userService;
        this.successApiResponse = successApiResponse;
    }

    //전체 유저 조회
    @GetMapping("")
    public ResponseEntity<?> userFindAll( ) {
        log.info("[GET] /user/ - /userFindAll/");

        successApiResponse.setBody(userService.userFindAllService());
        return new ResponseEntity(successApiResponse, HttpStatus.OK);
    };

    //특정 유저 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> userFindOne(@PathVariable Long id) {
        log.info("[GET] /user/{id}" + id + "/userFindOne/");

        successApiResponse.setBody(userService.userFineOneService(id));
        return new ResponseEntity(successApiResponse, HttpStatus.OK);
    }

    // 유저 등록 ( 회원 가입 )
    @PostMapping("")
    public ResponseEntity<ApiResponse> userCreate(@Valid @RequestBody UserEntity user, BindingResult bindingResult) {
        log.info("[POST] /user/ userCreateAPI" + "[RequestBody] " + user.toString());

        // client가 요청 잘못했을때 (파라미터 ) - 400
        if(bindingResult.hasErrors()) {
            String errMsg = bindingResult.getAllErrors().get(0).getDefaultMessage(); // 첫번째 에러로 출력
            successApiResponse.setBody(errMsg);
            return new ResponseEntity(successApiResponse, HttpStatus.BAD_REQUEST);
        }

        userService.createUserService(user);
        successApiResponse.setBody(UserConst.SUCCESS_CREATE_USER);
        return  new ResponseEntity(successApiResponse, HttpStatus.OK);
    }

    //유저 수정
    @PutMapping("")
    public ResponseEntity<ApiResponse> userUpdate(@RequestBody UserEntity user) {
        log.info("[PUT] /user/ userUpdateApi" + "[RequestBody] " + user);
        userService.updateUserService(user);
        successApiResponse.setBody(UserConst.FAILED_UPDATE_USER);
        return  new ResponseEntity(successApiResponse, HttpStatus.OK);
    }

    //유저 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> userDelete(@PathVariable Long id) {
        log.info("[DELETE] /user/ " + id + " /userDelete");

        userService.deleteUserService(id);
        successApiResponse.setBody(UserConst.SUCCESS_DELETE_USER);
        return new ResponseEntity(successApiResponse, HttpStatus.OK);
    }

    // 아이디 중복 체크 ( 회원 가입 )
    @GetMapping("/check/{id}")
    public ResponseEntity<ApiResponse> userIdCheck(@PathVariable("id") String user_id) {
        log.info("[GET] /user/check/id " + user_id + " /userIdCheck");

        if(userService.idCheckService(user_id)) {
            successApiResponse.setBody(UserConst.EXIST_USER);
            return new ResponseEntity(successApiResponse, HttpStatus.OK);
        } else {
            successApiResponse.setBody(UserConst.NO_USER);
            return new ResponseEntity(successApiResponse, HttpStatus.OK);
        }
    };

    // 로그인 ( 암호화는 서버에서 할지? 클라에서 할지 ? )
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> userlogin(HttpServletRequest request) {

        String id = request.getParameter("id");
        String pw = request.getParameter("password");
        log.info("[POST] /user/login " + "[ID] :  "  + id + "[PW] : " + pw + " /userLogin");

        if(userService.userLoginService(id, pw)) {
            successApiResponse.setBody(UserConst.SUCCESS_LOGIN);
            return new ResponseEntity(successApiResponse, HttpStatus.OK);
        } else {
            successApiResponse.setBody(UserConst.FAILED_LOGIN);
            return new ResponseEntity(successApiResponse, HttpStatus.OK);
        }
    }
}
