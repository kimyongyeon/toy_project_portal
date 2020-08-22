package com.simple.portal.biz.v1.note.dto;

import com.simple.portal.biz.v1.note.entity.RecvNoteEntity;
import com.simple.portal.biz.v1.note.entity.SendNoteEntity;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class NoteDTO {
    private Long id;
    private String title; // 제목
    private String contents; // 내용
    private String revId; // 받는이 아이디
    private String sendId; // 보내는이 아이디
    private LocalDateTime createdDate; // 작성일자
    private String strCreateDate;
    private int viewPoint; // 조회수: 상세내용 확인후 +1 한다. 0이면 읽지 않는 글로 간주. 알람푸시 개수로 써야 함.

    public NoteDTO(SendNoteEntity sendNoteEntity) {
        this.id = sendNoteEntity.getId();
        this.revId = sendNoteEntity.getRevId();
        this.sendId = sendNoteEntity.getSendId();
        this.title = sendNoteEntity.getTitle();
        this.contents = sendNoteEntity.getContents();
        this.createdDate = sendNoteEntity.getCreatedDate();
    }

    public NoteDTO(RecvNoteEntity recvNoteEntity) {
        this.id = recvNoteEntity.getId();
        this.revId = recvNoteEntity.getRevId();
        this.sendId = recvNoteEntity.getSendId();
        this.title = recvNoteEntity.getTitle();
        this.contents = recvNoteEntity.getContents();
        this.createdDate = recvNoteEntity.getCreatedDate();
    }
}
