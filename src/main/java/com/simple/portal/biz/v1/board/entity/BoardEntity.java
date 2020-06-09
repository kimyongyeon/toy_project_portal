package com.simple.portal.biz.v1.board.entity;

import com.simple.portal.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class BoardEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private double id;
    @Column
    private String title; // 제목
    @Column
    private String contents; // 내용
    @Column
    private String writer; // 글쓴이
    @Column
    private double viewCount; // 조회수
    @Column
    private int rowLike; // 좋아요
    @Column
    private int rowDisLike; // 싫어요

    @OneToMany(mappedBy = "boardEntity")
    List<CommentEntity> commentEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "boardEntity")
    List<FileEntity> fileEntities = new ArrayList<>();

    public void addComment(CommentEntity commentEntity) {
        commentEntity.setBoardEntity(this);
        commentEntityList.add(commentEntity);

    }
}
