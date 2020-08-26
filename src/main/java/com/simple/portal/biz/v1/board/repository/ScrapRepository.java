package com.simple.portal.biz.v1.board.repository;

import com.simple.portal.biz.v1.board.entity.ScrapEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScrapRepository extends JpaRepository<ScrapEntity, Long> {
    ScrapEntity findByUserIdAndBoardId(String userId, Long boardId);
}
