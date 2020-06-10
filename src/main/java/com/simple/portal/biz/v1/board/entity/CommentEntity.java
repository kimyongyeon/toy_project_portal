package com.simple.portal.biz.v1.board.entity;

import com.simple.portal.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name="comment")
public class CommentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기본키

    @NotEmpty
    @NotNull
    @Column
    private String title; // 제목

    @NotEmpty
    @NotNull
    @Column
    private String contents; // 내용

    @NotEmpty
    @NotNull
    @Column
    private String writer; // 글쓴이

    @Column
    private Long viewCount; // 조회수

    @Column
    private int rowLike; // 좋아요

    @Column
    private int rowDisLike; // 싫어요

    @NotEmpty
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID")
    BoardEntity boardEntity;


}
