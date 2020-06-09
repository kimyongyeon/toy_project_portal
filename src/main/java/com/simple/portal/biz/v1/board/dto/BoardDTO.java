package com.simple.portal.biz.v1.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardDTO {
    @NotEmpty
    private Long id;
    private String title; // 제목
    private String contents; // 내용
    private String writer; // 글쓴이
    private double viewCount; // 조회수
    private String itemGb; // 아이템 구분 L:D
}
