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
public class BoardReqWriteDTO {
    @NotNull(message = "Please enter id")
    private String title; // 제목
    @NotBlank(message="내용은 필수 입력값입니다.")
    private String contents; // 내용
    @NotBlank(message="아이디는 필수 입력값입니다.")
    private String writer; // 글쓴이
}
