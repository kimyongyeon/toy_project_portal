package com.simple.portal.biz.v1.user.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateDto { // 유저 수정을 위한 dto

    @NonNull  // Wrapper type의 경우 NotNull을 사용함
    private Long id;

    @NotBlank(message="유저 id는 필수 입력값입니다.")
    private String userId; // 프로필 저장 경로를 위해 입력받음

    @NotBlank(message="유저의 닉네임은 필수 입력값입니다.")
    private String nickname;

    private String gitAddr;
}
