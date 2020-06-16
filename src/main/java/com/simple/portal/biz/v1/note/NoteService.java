package com.simple.portal.biz.v1.note;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    @Autowired
    NoteRepository noteRepository;

    @Autowired
    JPAQueryFactory query;

    public List findNowAll(String userId) {
        QNoteEntity qNoteEntity = new QNoteEntity("n");
        return query.select(qNoteEntity)
                .from(qNoteEntity)
                .where(qNoteEntity.writer.contains(userId))
                .fetch();
    }

    public void save(NoteEntity noteEntity) {
        noteRepository.save(noteEntity);
    }

    public void update(NoteEntity noteEntity) {
        noteRepository.save(noteEntity);
    }

    public void delete(NoteEntity noteEntity) {
        noteRepository.delete(noteEntity);
    }
}
