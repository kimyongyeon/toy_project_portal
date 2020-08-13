package com.simple.portal.biz.v1.user.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreateDto { // 회원 가입을 위한 dto

    @NotBlank(message="아이디는 필수 입력값입니다.")
    private String userId;

    @NotBlank(message="닉네임은 필수 입력값입니다.")
    private String nickname;

    @NotBlank(message="비밀번호는 필수 입력값입니다.")
    private String password;

    @Override
    public String toString( ) {
        return "userId : " + this.userId + "\n"
                +"nickname : " + this.nickname + "\n";
    }

}
