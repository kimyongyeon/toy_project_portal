package com.simple.portal.biz.v1.socket.entity;

import com.simple.portal.biz.v1.board.entity.CommentEntity;
import com.simple.portal.biz.v1.board.entity.FeelEntity;
import com.simple.portal.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name="TB_BOARD")
public class SocketBoardEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기본키

    @Column(nullable = false)
    private String writer; // 글쓴이

    @Column(columnDefinition = "bigint default 0")
    private Long viewCount; // 조회수

    @Column(columnDefinition = "bigint default 0")
    private Long rowLike; // 좋아요

    @Column(columnDefinition = "bigint default 0")
    private Long rowDisLike; // 싫어요

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID")
    FeelEntity feelEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID")
    CommentEntity commentEntity;


}
