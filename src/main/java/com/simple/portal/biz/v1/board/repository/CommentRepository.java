package com.simple.portal.biz.v1.board.repository;

import com.simple.portal.biz.v1.board.entity.CommentEntity;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<CommentEntity, Double> {
}
