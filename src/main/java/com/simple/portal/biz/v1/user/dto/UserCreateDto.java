package com.simple.portal.biz.v1.user.dto;

import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreateDto { // 회원 가입을 위한 dto

    @NotBlank(message="아이디는 필수 입력값입니다.")
    private String userId;

    @NotBlank(message="이메일은 필수 입력값입니다.")
    private String email;

    @Column(nullable = true)
    private String nickname;

    @NotBlank(message="비밀번호는 필수 입력값입니다.")
    private String password;

    @Override
    public String toString( ) {
        return "userId : " + this.userId + "\n"
                +"email : " + this.email + "\n"
                +"nickname : " + this.nickname + "\n";
    }

}
