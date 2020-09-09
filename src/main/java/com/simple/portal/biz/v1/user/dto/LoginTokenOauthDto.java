package com.simple.portal.biz.v1.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginTokenOauthDto {
    String accessToken;
    String refreshToken;
    char auth;
    String userId;
}
