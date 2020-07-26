package com.simple.portal.biz.v1.note.repository;

import com.simple.portal.biz.v1.note.entity.SendNoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SendNoteRepository extends JpaRepository<SendNoteEntity, Long> {
}
