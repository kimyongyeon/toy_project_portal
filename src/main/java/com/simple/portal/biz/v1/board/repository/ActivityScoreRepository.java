package com.simple.portal.biz.v1.board.repository;

import com.simple.portal.biz.v1.board.entity.ActivityScoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityScoreRepository extends JpaRepository<ActivityScoreEntity, Long> {
}
