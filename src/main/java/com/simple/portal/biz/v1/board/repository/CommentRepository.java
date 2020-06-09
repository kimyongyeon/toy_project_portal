package com.simple.portal.biz.v1.board.repository;

import com.simple.portal.biz.v1.board.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
}
