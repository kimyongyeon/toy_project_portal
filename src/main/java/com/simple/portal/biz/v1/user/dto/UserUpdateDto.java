package com.simple.portal.biz.v1.user.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateDto { // 유저 수정을 위한 dto

    @NotBlank(message="유저의 pk id는 필수 입력값입니다.")
    private Long id;

    @NotBlank(message="유저의 아이디는 필수 입력값입니다.")
    private String userId;

    @NotBlank(message="유저의 닉네임은 필수 입력값입니다.")
    private String nickname;

    private String gitAddr;

}
