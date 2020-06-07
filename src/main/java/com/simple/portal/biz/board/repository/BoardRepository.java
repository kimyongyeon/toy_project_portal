package com.simple.portal.biz.board.repository;

import com.simple.portal.biz.board.entity.BoardEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
@Service
public interface BoardRepository extends CrudRepository<BoardEntity, Integer> {
}
