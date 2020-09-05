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
@Table(name = "TB_ALARM_HIST")
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
    private long boardId;
    @Column
    private long loginId;
    @Column
    private String userId;
    @Column
    private long noteId;

    @Enumerated(EnumType.STRING)
    private SocketAlarmEntity.EventType eventType;

}
