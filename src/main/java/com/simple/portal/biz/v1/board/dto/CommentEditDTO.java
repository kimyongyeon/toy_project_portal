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
public class CommentEditDTO {
    @NotNull(message="기본키는 필수 입력값입니다.")
    private Long id;
    @NotNull(message="제목은 필수 입력값입니다.")
    private String title; // 제목
    @NotNull(message="내용은 필수 입력값입니다.")
    private String content; // 내용
//    @NotNull(message="아이디는 필수 입력값입니다.")
//    private String writer; // 글쓴이
//    @NotNull(message="아이템 구분은 필수 입력값입니다.")
//    private String itemGb; // 아이템 구분 L:D
//
//    private Long viewCount; // 조회수
//    private Long rowLike; // 좋아요
//    private Long rowDisLike; // 싫어요

}
