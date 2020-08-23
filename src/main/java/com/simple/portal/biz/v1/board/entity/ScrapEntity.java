package com.simple.portal.biz.v1.board.entity;

import lombok.Data;

import javax.persistence.*;

// 스크랩 테이블
@Data
@Entity
@Table(name = "TB_SCRAP")
public class ScrapEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String userId;
    private Long boardId;
}
