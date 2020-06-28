package com.simple.portal.biz.v1.board.dto;

import com.simple.portal.biz.v1.board.entity.BoardEntity;
import com.simple.portal.biz.v1.board.entity.CommentEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardDTO {

    @NotBlank(message="기본키는 필수 입력값입니다.")
    private Long id;
    @NotBlank(message="제목은 필수 입력값입니다.")
    private String title; // 제목
    @NotBlank(message="내용은 필수 입력값입니다.")
    private String contents; // 내용
    @NotBlank(message="아이디는 필수 입력값입니다.")
    private String writer; // 글쓴이
    private Long viewCount; // 조회수
    private Long rowLike; // 좋아요 개수
    private Long rowDisLike; // 싫어요 개수
    private String itemGb; // 아이템 구분 L:D
    private LocalDateTime createdDate; // 작성일자
    private CommentDTO commentDTO;

    // QueryDSL를 활용한 조인쿼리에서 사용함.
    public BoardDTO(String title, String writer) {
        this.title = title;
        this.writer = writer;
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

        commentDTO.setId(commentEntity.getId());
        commentDTO.setTitle(commentEntity.getTitle());
    }

}
