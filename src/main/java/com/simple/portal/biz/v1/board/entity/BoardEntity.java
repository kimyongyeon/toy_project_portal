package com.simple.portal.biz.v1.board.entity;

import com.simple.portal.biz.v1.board.repository.CommentRepository;
import com.simple.portal.common.BaseEntity;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@ToString
@Table(name="board")
public class BoardEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_ID")
    private Long id;
    @Column
    private String title; // 제목
    @Column
    private String contents; // 내용
    @Column
    private String writer; // 글쓴이
    @Column
    private Long viewCount; // 조회수
    @Column
    private int rowLike; // 좋아요
    @Column
    private int rowDisLike; // 싫어요

    @Transient
    @OneToMany(mappedBy = "boardEntity", fetch = FetchType.LAZY)
    List<CommentEntity> commentEntityList = new ArrayList<>();

    @Transient
    @OneToMany(mappedBy = "boardEntity", fetch = FetchType.LAZY)
    List<FileEntity> fileEntities = new ArrayList<>();

    public void addComment(CommentEntity commentEntity) {
        commentEntity.setBoardEntity(this);
        commentEntityList.add(commentEntity);
    }
}
