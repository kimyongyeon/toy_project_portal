package com.simple.portal.biz.v1.board.entity;

import lombok.Data;

import javax.persistence.*;

// 감정 테이블
@Data
@Entity
@Table(name = "TB_BOARD_FEEL")
public class FeelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long boardId; // 게시글 PK
    private String userId;
    private String feelState; // L: Like, D: Dislike
    private int count;
}
