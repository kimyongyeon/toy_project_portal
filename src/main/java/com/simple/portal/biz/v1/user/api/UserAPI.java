package com.simple.portal.biz.v1.user.api;

import com.simple.portal.biz.v1.user.UserConst;
import com.simple.portal.biz.v1.user.dto.LoginDto;
import com.simple.portal.biz.v1.user.entity.UserEntity;
import com.simple.portal.biz.v1.user.exception.ParamInvalidException;
import com.simple.portal.biz.v1.user.service.UserService;
import com.simple.portal.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/v1/api/user")
public class UserAPI {

    private UserService userService;
    private ApiResponse apiResponse;

    @Autowired
    public void UserController(UserService userService, ApiResponse apiResponse) {
        this.userService = userService;
        this.apiResponse = apiResponse;
    }


    // 메일전송 테스트
    @GetMapping("/mail")
    public void mail_sender( ) {

        String to ="xowns4817@naver.com";
        String subject = "회원가입 축하 !";
        String msg = "회원가입을 진심으로 축하드립니다.!";
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
        apiResponse.setMsg(UserConst.SUCCESS_UPDATE_USER);
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
    public ResponseEntity<ApiResponse> userlogin(@Valid @RequestBody LoginDto loginDto, BindingResult bindingResult) {
        log.info("[POST] /user/login/");

        if(bindingResult.hasErrors()) {
            log.info("400 Parameter Error !");
            String errMsg = bindingResult.getAllErrors().get(0).getDefaultMessage(); // 첫번째 에러로 출력
            throw new ParamInvalidException(errMsg);
        }

        String id = loginDto.getId();
        String pw = loginDto.getPassword();
        log.info("[POST] /user/login " + "[ID] :  "  + id + "[PW] : " + pw + " /userLogin");

        String token = userService.userLoginService(id, pw);

        // authority 조회 후 'Y'일때만 로그인 성공 로직 작성
        char auth = userService.userAuthCheckServie(id);
        //if(auth == 'N') 권한없음. -> 302 ....등등

        apiResponse.setMsg(UserConst.SUCCESS_LOGIN);
        Map<String, String> obj = new HashMap<>();
        obj.put("token", token);
        apiResponse.setBody(obj);  // user_id 기반 토큰 생성
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    //회원 가입 메일 전송 ( 회원 가입 완료하면 해당 api 호출 )
    @GetMapping("/send/email")
    public void sendEmail( ) {
    log.info("[GET] /send/email");

    };

    //회원 가입 메일에서 메인으로 이동 링크 클릭 ( 권한 부여 )
    @GetMapping("/auth/{id}")
    public ResponseEntity<ApiResponse> userAuth(@PathVariable("id") @NotNull String userId) {
        log.info("[GET] /user/auth/" + userId);

        userService.updateUserAuthService(userId);
        apiResponse.setMsg(UserConst.SUCCESS_GRANT_USER_AUTH);
        apiResponse.setMsg("");
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }
}
