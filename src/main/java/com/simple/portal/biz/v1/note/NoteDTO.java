package com.simple.portal.biz.v1.note;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class NoteDTO {
    private Long id;
    private String title; // 제목
    private String contents; // 내용
    private String writer; // 글쓴이
    private String createdDate; // 작성일자
    private int viewPoint; // 조회수: 상세내용 확인후 +1 한다. 0이면 읽지 않는 글로 간주. 알람푸시 개수로 써야 함.
}
