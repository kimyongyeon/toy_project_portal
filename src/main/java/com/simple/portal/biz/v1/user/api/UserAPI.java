package com.simple.portal.biz.v1.user.api;

import com.amazonaws.Response;
import com.simple.portal.biz.v1.user.ApiConst;
import com.simple.portal.biz.v1.user.UserConst;
import com.simple.portal.biz.v1.user.dto.*;
import com.simple.portal.biz.v1.user.exception.ParamInvalidException;
import com.simple.portal.biz.v1.user.exception.UpdateActivityScoreFailedException;
import com.simple.portal.biz.v1.user.exception.UserAuthCheckFailedException;
import com.simple.portal.biz.v1.user.exception.UserNotFoundException;
import com.simple.portal.biz.v1.user.service.UserService;
import com.simple.portal.common.ApiResponse;
import com.simple.portal.common.Interceptor.JwtUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/v1/api/user")
@CrossOrigin("*")
public class UserAPI {

    private UserService userService;
    private ApiResponse apiResponse;
    private JwtUtil jwtUtil;

    @Autowired
    public void UserController(UserService userService, ApiResponse apiResponse, JwtUtil jwtUtil) {
        this.userService = userService;
        this.apiResponse = apiResponse;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/getToken")
    public void getToken( ) {
        String token = jwtUtil.createAccessToken("xowns1234", 'N');
        log.info("token : " + token);
    }

    // jwt 토큰 테스트
    @GetMapping("/token")
    public String token(HttpServletRequest httpServletRequest, @RequestParam("name") String name) {

        log.info("token test!");
        log.info("name : " + name);

        String userId = (String) httpServletRequest.getAttribute("userId");
        String token = (String) httpServletRequest.getAttribute("token");
        log.info("userId : " + userId);
        log.info("token : " + token);

        return "ok";
    };

    //전체 유저 조회
    @GetMapping("")
    public ResponseEntity<?> userFindAll( ) {
        log.info("[GET] /user/ - /userFindAll/");

        apiResponse.setMsg(UserConst.SUCCESS_SELECT_USER);
        apiResponse.setBody(userService.userFindAllService());
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    };

    //특정 유저 조회 -> 팔로잉, 팔로워 수도 같이 출력
    @GetMapping("/{userId}")
    public ResponseEntity<?> userFindOne(@PathVariable("userId") @NotNull String userId) {
        log.info("[GET] /user/{id}" + userId + "/userFindOne/");

        apiResponse.setMsg(UserConst.SUCCESS_SELECT_USER);
        apiResponse.setBody(userService.userFineOneService(userId));
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    // 유저 등록 ( 회원 가입 ) - 이메일로 가입하는 유저
    @PostMapping("")
    public ResponseEntity<ApiResponse> userCreate(@Valid @RequestBody UserCreateDto userCreateDto, BindingResult bindingResult) {
        log.info("[POST] /user/ userCreateAPI" + "[RequestBody] " + userCreateDto.toString());

        // client가 요청 잘못했을때 (파라미터 ) - 400
        if(bindingResult.hasErrors()) {
            String errMsg = bindingResult.getAllErrors().get(0).getDefaultMessage(); // 첫번째 에러로 출력
            throw new ParamInvalidException(errMsg);
        }
        userService.createUserService(userCreateDto);
        apiResponse.setMsg(UserConst.SUCCESS_CREATE_USER);
        apiResponse.setBody("");
        return  new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    //유저 수정
    @PutMapping("")
    public ResponseEntity<ApiResponse> userUpdate(@ModelAttribute @Valid UserUpdateDto userUpdateDto,
                                                  BindingResult bindingResult) {
        log.info("[PUT] /user/ userUpdateApi" + "[RequestBody] " + userUpdateDto);

        // client가 요청 잘못했을때 (파라미터 ) - 400
        if(bindingResult.hasErrors()) {
            String errMsg = bindingResult.getAllErrors().get(0).getDefaultMessage(); // 첫번째 에러로 출력
            throw new ParamInvalidException(errMsg);
        }

        userService.updateUserService(userUpdateDto);
        apiResponse.setMsg(UserConst.SUCCESS_UPDATE_USER);
        apiResponse.setBody("");
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    //유저 삭제 - 회원 탈퇴 -> redis에 있는 팔로우 정보도 삭제
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> userDelete(@PathVariable("userId") @NotNull String userId) {
        log.info("[DELETE] /user/ " + userId + " /userDelete");

        userService.deleteUserService(userId);
        apiResponse.setMsg(UserConst.SUCCESS_DELETE_USER);
        apiResponse.setBody("");
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    // 아이디 중복 체크 ( 회원 가입 )
    @GetMapping("/check/id/{userId}")
    public ResponseEntity<ApiResponse> userIdCheck(@PathVariable("userId") String user_id) {
        log.info("[GET] /user/check/id " + user_id + " /userIdCheck");

        if(userService.idCheckService(user_id)) apiResponse.setMsg(UserConst.EXIST_USER);
        else apiResponse.setMsg(UserConst.NO_USER);
        apiResponse.setBody("");
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    };

    // 이메일 중복 체크 ( 회원가입 )
    @GetMapping("/check/email/{email}")
    public ResponseEntity<ApiResponse> emailCheck(@PathVariable("email") String email) {
        log.info("[GET] /user/check/email " + email);

        if(userService.emailCheckService(email)) apiResponse.setMsg(UserConst.EXIST_EMAIL);
        else apiResponse.setMsg(UserConst.NO_EMAIL);
        apiResponse.setBody("");
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> userlogin(@Valid @RequestBody LoginDto loginDto, BindingResult bindingResult) {
        log.info("[POST] /user/login/");

        if(bindingResult.hasErrors()) {
            String errMsg = bindingResult.getAllErrors().get(0).getDefaultMessage(); // 첫번째 에러로 출력
            throw new ParamInvalidException(errMsg);
        }
        String id = loginDto.getUserId();
        String pw = loginDto.getPassword();
        log.info("[POST] /user/login " + "[ID] :  "  + id + "[PW] : " + pw + " /userLogin");

        LoginTokenDto loginTokenDto = userService.userLoginService(id, pw);

        // authority 조회 후 'Y'일때만 로그인 성공 로직 작성
        char auth = userService.userAuthCheckServie(id);
        if(auth == 'N') throw new UserAuthCheckFailedException();

        apiResponse.setMsg(UserConst.SUCCESS_LOGIN);
        Map<String, String> obj = new HashMap<>();
        obj.put("userId", id);// 로그인 return값에 userId 추가
        obj.put("Role", String.valueOf(auth)); // 'Y' 일반 회원, 'A' 관리자 -> 관리자는 API로 설정 못하고 수동으로 가능
        obj.put("platform", "normal"); // 일반 로그일 경우 "normal"
        obj.put("access_token", loginTokenDto.getAccessToken());
        obj.put("refresh_token", loginTokenDto.getRefreshToken());
        apiResponse.setBody(obj);  // user_id 기반 토큰 생성
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    // oauth 로그인 ( 최초에만 write 해준다. )
    @PostMapping("/oauth")
    public ResponseEntity<ApiResponse> oauthUserLogin(@Valid @RequestBody OAuthDto oAuthDto,
                                                       BindingResult bindingResult) {

        log.info("[Post] /user/oauth oauthUserLogin" + oAuthDto.toString());

        // client가 요청 잘못했을때 (파라미터 ) - 400
        if(bindingResult.hasErrors()) {
            String errMsg = bindingResult.getAllErrors().get(0).getDefaultMessage(); // 첫번째 에러로 출력
            throw new ParamInvalidException(errMsg);
        }

        // 기존에 했는지 구분
        LoginTokenOauthDto loginTokenOauthDto = userService.oauthUserLoginService(oAuthDto);
        apiResponse.setMsg(UserConst.SUCCESS_LOGIN);
        Map<String, String> obj = new HashMap<>();
        obj.put("userId", loginTokenOauthDto.getUserId());// 로그인 return값에 userId 추가
        obj.put("Role", String.valueOf(loginTokenOauthDto.getAuth())); // 'Y' 일반 회원, 'A' 관리자 -> 관리자는 API로 설정 못하고 수동으로 가능
        obj.put("access-token", loginTokenOauthDto.getAccessToken());
        obj.put("refresh-token", loginTokenOauthDto.getRefreshToken());
        apiResponse.setBody(obj);  // user_id 기반 토큰 생성
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    // 로그아웃 -> 토큰을 만료시킴
    // 토큰이 한번 만들어지면 바꿀수 없다.. 즉, 유저가 로그아웃해도 세션처럼 값을 지울수 없다는 뜻이다. ( = 로그아웃해도 토큰이 살아있다. expireTime까지 )
    // 해당 유저의 refresh token을 레디스에서 삭제하고 access token을 blackList에 등록한다.
    @GetMapping("/logout")
    public ResponseEntity<ApiResponse> logout(@RequestParam("userId") String userId) {

        log.info("[POST] /user/logout " + userId);

        userService.userLogoutService(userId);
        apiResponse.setMsg(UserConst.SUCCESS_LOGOUT);
        apiResponse.setBody("");
        return  new ResponseEntity(apiResponse, HttpStatus.OK);
    };

    // 권한 업데이트
    @GetMapping("/auth")
    public ModelAndView auth_update(@RequestParam(value="userId") @NotNull String userId) {

        log.info("[GET] /user/auth/ " + userId);
        Boolean res = userService.updateUserAuthService(userId);

        if(res) { // 권한 업데이트 성공
           return new ModelAndView("redirect:" + ApiConst.grantAuthSuccessUrl + "?userId="+userId);
        } else { // 권한 업데이트 실패
            return new ModelAndView("redirect:" + ApiConst.grantAuthFailUrl + "?userId="+userId);
        }
    }

    // 비밀번호 변경
    // 값이 없을때는 체크하는데 잘못된 값일경우도 체크하는지? (디비조회 필요) 클라에서 체크 됬다고 판단하고 체크 안해도 되는지 ?
    @PutMapping("/password")
    public ResponseEntity<ApiResponse> updatePassword(@Valid @RequestBody PasswordDto passwordDto, BindingResult bindingResult) {

        log.info("[PUT] /user/passwrod/" + " id : " + passwordDto.getUserId());

        if(bindingResult.hasErrors()) {
            String errMsg = bindingResult.getAllErrors().get(0).getDefaultMessage(); // 첫번째 에러로 출력
            throw new ParamInvalidException(errMsg);
        }

        userService.updateUserPasswordService(passwordDto.getUserId(), passwordDto.getNewPassword());
        apiResponse.setMsg(UserConst.SUCCESS_UPDATE_PASSWORD);
        apiResponse.setBody("");
        return  new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    // 비밀번호 찾기 -> 인자로 받는 메일주소로 새로운 비밀번호 발송하면서 해당 값으로 비밀번호 설정
    // 만약, 없는 유저일 경우? 클라에서 검증된 값을 넘기는지? 아니면 서버에서 디비조회 한번 더 해서 없는 유저인지 판단해야 되는지 ?
    // 유저 아이디만 입력받음 -> 해당 아이디의 이메일을 조회해서 그 이메일로 신규 비밀번호 전송
    @GetMapping("/find/password")
    public ResponseEntity<ApiResponse> findPassword(@RequestParam(value="userId", required = false, defaultValue = "") String userId) {

        log.info("[PUT] /user/find/passwrod/" + " user_id : " + userId);

        if(userId.equals("")) throw new ParamInvalidException(UserConst.ERROR_PARAMS);

        userService.findUserPasswordService(userId);
        apiResponse.setMsg(UserConst.SUCCESS_SEND_NEW_PASSWORD);
        apiResponse.setBody("");
        return  new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    // 1. setOperation 빈으로 만들어서 주입하기
    // 2. 팔로워 리스트 조회했는데 id이므로 id로 닉네임 조회해서 클라한테 뿌려주기
    // 팔로우 하기
    @PostMapping("/follow")
    public ResponseEntity<ApiResponse> do_follow(@Valid @RequestBody FollowDto followDto, BindingResult bindingResult) {

        log.info("[PUT] /user/follow/" + " followDto : " + followDto.toString());

        if(bindingResult.hasErrors()) {
            String errMsg = bindingResult.getAllErrors().get(0).getDefaultMessage(); // 첫번째 에러로 출력
            throw new ParamInvalidException(errMsg);
        }

        userService.followService(followDto.getFollowing_id(), followDto.getFollowed_id());
        apiResponse.setMsg(UserConst.SUCCESS_FOLLOW);
        apiResponse.setBody("");
        return  new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    // 팔로우 끊기 ( 언팔로우 하기 )
    @PutMapping("/unfollow")
    public ResponseEntity<ApiResponse> un_follow(@Valid @RequestBody FollowDto followDto, BindingResult bindingResult) {

        log.info("[PUT] /user/unfollow" + " followDto : " + followDto.toString());

        if(bindingResult.hasErrors()) {
            String errMsg = bindingResult.getAllErrors().get(0).getDefaultMessage(); // 첫번째 에러로 출력
            throw new ParamInvalidException(errMsg);
        }

        userService.unfollowService(followDto.getFollowing_id(),followDto.getFollowed_id());
        apiResponse.setMsg(UserConst.SUCCESS_UNFOLLOW);
        apiResponse.setBody("");
        return  new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    // 나를 팔로우 하는 유저들 조회
    @GetMapping("/follower")
    public ResponseEntity<ApiResponse> get_follower(@RequestParam(value="followed_id", required = false, defaultValue = "") String followed_id) {

        log.info("[Get] /user/follower" + " followed_id : " + followed_id);
        if(followed_id.equals("")) throw new ParamInvalidException(UserConst.ERROR_PARAMS);

        FollowedList follower_list = userService.getFollowerIdService(followed_id);
        apiResponse.setMsg(UserConst.SUCCESS_SELECT_FOLLOWERS);
        Map<String, Object> obj = new HashMap<>();
        obj.put("followedList", follower_list);
        apiResponse.setBody(obj);
        return  new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    // 내가 팔로잉 하는 유저들 조회
    @GetMapping("/following")
    public ResponseEntity<ApiResponse> get_following(@RequestParam(value="following_id", required = false, defaultValue = "") String following_id) {
        log.info("[GET] /user/following" + " following_id : " + following_id);
        if(following_id.equals("")) throw new ParamInvalidException(UserConst.ERROR_PARAMS);
        FollowingList following_list = userService.getFollowingIdService(following_id);
        apiResponse.setMsg(UserConst.SUCCESS_SELECT_FOLLOWING_USERS);
        Map<String, Object> obj = new HashMap<>();
        obj.put("followingList", following_list);
        apiResponse.setBody(obj);
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }
}
