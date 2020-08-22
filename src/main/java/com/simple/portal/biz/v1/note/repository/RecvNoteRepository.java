package com.simple.portal.biz.v1.note.repository;

import com.simple.portal.biz.v1.note.entity.RecvNoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecvNoteRepository extends JpaRepository<RecvNoteEntity, Long> {
}
