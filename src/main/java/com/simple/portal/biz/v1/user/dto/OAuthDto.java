package com.simple.portal.biz.v1.user.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class OAuthDto {
    private String platform;
    private String nickname;
    private String email;
    private String profile;

    @Builder
    public OAuthDto(String platform, String nickname, String email, String profile) {
        this.platform = platform;
        this.nickname = nickname;
        this.email = email;
        this.profile = profile;
    }
}
