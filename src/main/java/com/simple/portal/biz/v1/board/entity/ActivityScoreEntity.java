package com.simple.portal.biz.v1.board.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

// 활동점수 테이블
@Data
@Entity
@Table(name = "TB_ACTIVITY_SCORE")
@EntityListeners(AuditingEntityListener.class)
public class ActivityScoreEntity {

    //    private Long boardScore; // 게시글 +5
//    private Long commentScore; // 댓글 +2
//    private Long eventScore; // 좋아요/싫어요 +1
//    private Long loginScore; // 로그인 점수 +1
    public static enum ScoreType {
        BOARD, COMMENT, EVENT, LOGIN
    }
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String userId;
    @Enumerated(EnumType.STRING)
    private ScoreType type;
    private Long score;
    @CreatedDate
    private LocalDateTime createdDate;
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;
}
