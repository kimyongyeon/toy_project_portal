package com.simple.portal.biz.v1.board.entity;

import com.querydsl.core.annotations.PropertyType;
import com.querydsl.core.annotations.QueryType;
import com.simple.portal.common.BaseEntity;
import lombok.*;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@ToString
@Table(name="TB_BOARD")
public class BoardEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_ID")
    private Long id;

    @Column(nullable = false, unique = false)
    private String title; // 제목

    @Column(nullable = false, unique = false)
    @Lob // 2020-06-17 LOB 타입 정의
    private String contents; // 내용

    @Column(nullable = false, updatable = false, unique = false)
    @Audited
    private String writer; // 글쓴이

    @Column
    private Long viewCount; // 조회수

    @Column
    private Long rowLike; // 좋아요

    @Column
    private Long rowDisLike; // 싫어요

    // cascade = CascadeType.ALL: 삭제시 자식까지...
    @Transient // 테이블 필드로 만들고 싶지 않을때 사용, 해당 어노테이션이 빠지면 java.lang.StackOverflowError: null
    @OneToMany(mappedBy = "boardEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<CommentEntity> commentEntityList = new ArrayList<>();

    @Transient // 테이블 필드로 만들고 싶지 않을때 사용
    @OneToMany(mappedBy = "boardEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FileEntity> fileEntities = new ArrayList<>();

    public void addComment(CommentEntity commentEntity) {
        commentEntity.setBoardEntity(this);
        commentEntityList.add(commentEntity);
    }
}
