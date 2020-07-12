package com.simple.portal.biz.v1.note.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.simple.portal.biz.v1.note.dto.NoteDTO;
import com.simple.portal.biz.v1.note.entity.NoteEntity;
import com.simple.portal.biz.v1.note.entity.QNoteEntity;
import com.simple.portal.biz.v1.note.repository.NoteRepository;
import com.simple.portal.util.DateFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class NoteService {

    @Autowired
    NoteRepository noteRepository;

    @Autowired
    JPAQueryFactory query;

    public List findAll(String userId) {
        QNoteEntity qNoteEntity = new QNoteEntity("n");
        return query.select(qNoteEntity)
                .from(qNoteEntity)
                .where(qNoteEntity.writer.contains(userId))
                .fetch();
    }

    @Transactional
    public NoteDTO findDetail(Long id) {

        // 상세조회
        QNoteEntity qNoteEntity = new QNoteEntity("n");
        NoteEntity noteEntity = query.select(qNoteEntity)
                .from(qNoteEntity)
                .where(qNoteEntity.id.eq(id))
                .fetchOne();

        // 조회수 증가 ? 업데이트 증가 안함?
        if (!noteRepository.findById(id).isEmpty()) {
            NoteEntity viewPointUpdateEntity = noteRepository.findById(id).get();
            viewPointUpdateEntity.setViewPoint(1);
            noteRepository.save(viewPointUpdateEntity);
        }

        // LocalDateTime to Date
        Date date = Date.from(noteEntity.getCreatedDate().atZone(ZoneId.systemDefault()).toInstant());

        return NoteDTO.builder()
                .id(noteEntity.getId())
                .writer(noteEntity.getWriter()) // 글쓴이
                .title(noteEntity.getTitle()) // 제목
                .contents(noteEntity.getContents()) // 상세내용
                .createdDate(DateFormatUtil.getTimeBefore(date)) // 1일전, 10일전... 변환
                .build();
    }

    public void save(NoteDTO noteDTO) {

        noteRepository.save(NoteEntity.builder()
            .title(noteDTO.getTitle())
            .contents(noteDTO.getContents())
            .writer(noteDTO.getWriter())
            .viewPoint(noteDTO.getViewPoint())
            .build()
        );
    }

    public void update(NoteDTO noteDTO) {
        save(noteDTO);
    }

    public void delete(NoteDTO noteDTO) {
        noteRepository.delete(NoteEntity.builder().id(noteDTO.getId()).build());
    }
}
