package com.simple.portal.biz.v1.note.entity;

import com.simple.portal.common.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@ToString
@Table(name="TB_SEND_NOTE")
public class SendNoteEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOTE_SEND_ID")
    private Long id;
    @Column
    private String title; // 제목
    @Column
    private String contents; // 내용
    @Column
    private String revId; // 받는이 아이디
    @Column
    private String sendId; // 보내는이 아이디
    @Column
    private int viewPoint; // 조회수: 상세내용 확인후 +1 한다. 0이면 읽지 않는 글로 간주. 알람푸시 개수로 써야 함.
    @Column
    private boolean delYn; // 삭제유무
}
