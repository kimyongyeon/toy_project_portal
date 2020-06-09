package com.simple.portal.biz.v1.board.repository;

import com.simple.portal.biz.v1.board.entity.BoardEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BoardRepository extends CrudRepository<BoardEntity, Double> {
    public List findAllByTitleOrContents(String title, String contents);
}
