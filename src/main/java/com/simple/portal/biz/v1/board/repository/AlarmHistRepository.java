package com.simple.portal.biz.v1.board.repository;

import com.simple.portal.biz.v1.board.entity.AlarmHistEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmHistRepository extends JpaRepository<AlarmHistEntity, Long> {
    AlarmHistEntity findByUserId(String userId);
    AlarmHistEntity findByUserIdAndEventType(String userId, AlarmHistEntity.EventType eventType);
    void deleteByUserIdAndEventType(String userId, AlarmHistEntity.EventType eventType);
    void deleteByUserIdAndNoteIdAndEventType(String userId, Long noteId, AlarmHistEntity.EventType eventType);
}
