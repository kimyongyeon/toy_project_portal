package com.simple.portal.biz.v1.board.entity;

// 알람푸시 테이블

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_ALARM_HIST")
@Data
@EntityListeners(AuditingEntityListener.class)
public class AlarmHistEntity {
    public static enum EventType {
        // 쪽지, 댓글, 좋아요, 싫어요, 최초1회 로그인
        EVT_NR, EVT_BC, EVT_BL, EVT_BD, EVT_UL
    }
    @Id @GeneratedValue(strategy = GenerationType.AUTO)

    // test01,1,EVT_BC = 댓글1
    // test01,1,EVT_BC = 댓글2

    private Long id;
    private String userId;// 등록자

    private Long boardId; // 상세글을 클릭할때 댓글이나, 좋아요/싫어요 관련 카운트를 감소하기 위해 사용.
    private Long loginId; // 로그인 아이디
    private Long noteId;  // 쪽지 아이디

    @CreatedDate
    private LocalDateTime regDate;
//    @Column(name = "NOTE_CNT", columnDefinition = "Decimal(10,2) default '0'")
//    private Long noteCount;
//    @Column(name = "BOARD_CNT", columnDefinition = "Decimal(10,2) default '0'")
//    private Long boardCount;
//    @Column(name = "USER_CNT", columnDefinition = "Decimal(10,2) default '0'")
//    private Long userCount;



    @Enumerated(EnumType.STRING)
    private EventType eventType;
}
