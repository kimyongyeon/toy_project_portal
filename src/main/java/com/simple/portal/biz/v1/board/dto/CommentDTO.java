package com.simple.portal.biz.v1.board.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDTO {
    @NotNull
    @NotEmpty
    private Long id; // 아이디
    private Long boardId; // 게시글 아이디
    private String title; // 제목
    private String content; // 내용
    private String itemGb; // 아이템 구분 L:D
}
