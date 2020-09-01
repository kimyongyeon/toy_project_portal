package com.simple.portal.biz.v1.user.api;


import com.amazonaws.auth.profile.internal.Profile;
import com.simple.portal.biz.v1.user.UserConst;
import com.simple.portal.biz.v1.user.service.ProfileService;
import com.simple.portal.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/api")
@Slf4j
@CrossOrigin
public class ProfileAPI {

    private ApiResponse apiResponse;
    private ProfileService profileService;

    public ProfileAPI(ApiResponse apiResponse, ProfileService profileService) {
        this.apiResponse = apiResponse;
        this.profileService = profileService;
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse> updateProfileImg(@RequestParam("userId") String userId,
                                                        @RequestParam("file") MultipartFile file) {

        log.info("[PUT] /v1/api/profile " +  userId);
        profileService.updateProfileImgService(userId, file);
        apiResponse.setMsg(UserConst.SUCCESS_UPDATE_PROFILE_IMG);
        apiResponse.setBody("");
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }
}