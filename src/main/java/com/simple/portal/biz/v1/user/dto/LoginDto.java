package com.simple.portal.biz.v1.user.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    @NotBlank(message="아이디는 필수값 입니다.")
    private String userId;
    @NotBlank(message="비밀번호는 필수값 입니다.")
    private String password;
}
