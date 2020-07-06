package com.simple.portal.biz.v1.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardReqDTO {
    @NotBlank(message="기본키는 필수 입력값입니다.")
    private Long id;
    @NotBlank(message="제목은 필수 입력값입니다.")
    private String title; // 제목
    @NotBlank(message="내용은 필수 입력값입니다.")
    private String contents; // 내용
    @NotBlank(message="아이디는 필수 입력값입니다.")
    private String writer; // 글쓴이
}
