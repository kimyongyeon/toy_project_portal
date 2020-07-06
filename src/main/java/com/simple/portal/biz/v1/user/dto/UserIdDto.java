package com.simple.portal.biz.v1.user.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserIdDto {
    @NotBlank(message="아이디는 필수값 입니다.")
    private String userId;
}
