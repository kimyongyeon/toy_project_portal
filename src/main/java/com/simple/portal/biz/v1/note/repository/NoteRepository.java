package com.simple.portal.biz.v1.note.repository;

import com.simple.portal.biz.v1.note.entity.NoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<NoteEntity, Long> {
}
