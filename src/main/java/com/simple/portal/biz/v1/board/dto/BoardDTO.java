package com.simple.portal.biz.v1.board.dto;

import com.simple.portal.biz.v1.board.entity.BoardEntity;
import com.simple.portal.biz.v1.board.entity.CommentEntity;
import com.simple.portal.biz.v1.board.entity.ScrapEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardDTO {

    @NotNull(message = "Please enter id")
    private Long id;
    @NotNull(message="제목은 필수 입력값입니다.")
    private String title; // 제목
    @NotNull(message="내용은 필수 입력값입니다.")
    private String contents; // 내용
    @NotNull(message="아이디는 필수 입력값입니다.")
    private String writer; // 글쓴이
    private Long viewCount = 0L; // 조회수
    private Long rowLike = 0L; // 좋아요 개수
    private Long rowDisLike = 0L; // 싫어요 개수
    private String itemGb = ""; // 아이템 구분 L:D
    private LocalDateTime createdDate; // 작성일자
    private Long commentCnt = 0L;
    private Long key; // React Rendering

    // QueryDSL를 활용한 조인쿼리에서 사용함.
    public BoardDTO(String title, String writer) {
        this.title = title;
        this.writer = writer;
        this.key = id;
    }
    public BoardDTO(Long id, String title, String contents, String writer) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.writer = writer;
        this.key = id;
    }
    public BoardDTO(Long id, String title, String contents, String writer, LocalDateTime createdDate) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.writer = writer;
        this.createdDate = createdDate;
        this.key = id;
    }

    public BoardDTO(BoardEntity boardEntity, CommentEntity commentEntity) {
        this.id = boardEntity.getId();
        this.title = boardEntity.getTitle();
        this.contents = boardEntity.getContents();
        this.writer = boardEntity.getWriter();
        this.viewCount = boardEntity.getViewCount();
        this.rowLike = boardEntity.getRowLike();
        this.rowDisLike = boardEntity.getRowDisLike();
        this.createdDate = boardEntity.getCreatedDate();
        this.key = boardEntity.getId();
    }

    public BoardDTO(BoardEntity boardEntity, ScrapEntity scrapEntity) {
        this.id = boardEntity.getId();
        this.title = boardEntity.getTitle();
        this.contents = boardEntity.getContents();
        this.writer = boardEntity.getWriter();
        this.viewCount = boardEntity.getViewCount();
        this.rowLike = boardEntity.getRowLike();
        this.rowDisLike = boardEntity.getRowDisLike();
        this.createdDate = boardEntity.getCreatedDate();
        this.key = boardEntity.getId();
    }

}
