package com.simple.portal.biz.v1.board.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentLikeDTO {
    @NotNull(message = "Please enter id")
    private Long id; // 아이디
    @NotBlank(message="아이템 구분은 필수 입력값입니다.")
    private String itemGb; // 아이템 구분 L:D
}
