package com.simple.portal.biz.v1.user.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class OAuthDto {

    @NotBlank(message="플랫폼 이름은 필수값 입니다.")
    private String platform;
    @NotBlank(message="닉네임은 필수값 입니다.")
    private String nickname;
    @NotBlank(message="이메일은 필수값 입니다.")
    private String email;
    @NotBlank(message="프로필 이미지는 필수값 입니다.")
    private String profileImg;

    @Builder
    public OAuthDto(String platform, String nickname, String email, String profileImg) {
        this.platform = platform;
        this.nickname = nickname;
        this.email = email;
        this.profileImg = profileImg;
    }

    @Override
    public String toString() {
        return "platform : " + this.platform +
                "nickname : " + this.nickname +
                "email : " + this.email +
                "profile : " + this.profileImg;
    }
}
