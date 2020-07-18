package com.simple.portal.biz.v1.note.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class NoteSaveDTO {
    private String title; // 제목
    private String contents; // 내용
    private String writer; // 글쓴이
    private String createdDate; // 작성일자
    private int viewPoint; // 조회 카운트
}
