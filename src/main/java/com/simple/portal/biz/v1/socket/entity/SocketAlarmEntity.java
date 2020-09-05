package com.simple.portal.biz.v1.socket.entity;

import com.simple.portal.biz.v1.board.entity.AlarmHistEntity;
import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@ToString
@Table(name = "TB_ALARM_HIST_2")
public class SocketAlarmEntity {
    public static enum EventType {
        // 쪽지, 댓글, 좋아요, 싫어요, 최초1회 로그인
        EVT_NR, EVT_BC, EVT_BL, EVT_BD, EVT_UL
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column
    private long board_id;
    @Column
    private long login_id;
    @Column
    private String user_id;
    @Column
    private long note_id;

    @Enumerated(EnumType.STRING)
    private SocketAlarmEntity.EventType eventType;

}
