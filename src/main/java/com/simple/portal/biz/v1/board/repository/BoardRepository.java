package com.simple.portal.biz.v1.board.repository;

import com.simple.portal.biz.v1.board.dto.BoardDTO;
import com.simple.portal.biz.v1.board.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface BoardRepository extends JpaRepository<BoardEntity, Long>, QuerydslPredicateExecutor<BoardEntity> {
    List<BoardDTO> findAllByTitleOrContents(String title, String contents);
    Page<BoardEntity> findByTitleContaining(String title, Pageable pageable);
}
