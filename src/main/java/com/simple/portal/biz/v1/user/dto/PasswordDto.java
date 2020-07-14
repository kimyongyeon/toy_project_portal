package com.simple.portal.biz.v1.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordDto {
    @NotNull(message="아이디는 필수값 입니다.")
    private Long id; // 유저 테이블의 PK -> Wrapper type의 경우는 NotNull을 써야됨
    @NotBlank(message="비밀번호는 필수값 입니다.")
    private String newPassword;
}
