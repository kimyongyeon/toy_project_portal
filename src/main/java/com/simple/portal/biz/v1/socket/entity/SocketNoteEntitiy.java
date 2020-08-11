package com.simple.portal.biz.v1.socket.entity;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@ToString
@Table(name="TB_RECV_NOTE")
public class SocketNoteEntitiy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOTE_RECV_ID")
    private Long id;
    @Column
    private String title; // 제목
    @Column
    private String contents; // 내용
    @Column
    private String rev_id; // 받는사람
    @Column
    private String send_id; // 보낸사람
    @Column
    private int viewPoint; // 조회수: 상세내용 확인후 +1 한다. 0이면 읽지 않는 글로 간주. 알람푸시 개수로 써야 함.
}
