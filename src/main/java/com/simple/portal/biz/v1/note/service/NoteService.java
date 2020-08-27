package com.simple.portal.biz.v1.note.service;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.simple.portal.biz.v1.board.entity.AlarmHistEntity;
import com.simple.portal.biz.v1.board.repository.AlarmHistRepository;
import com.simple.portal.biz.v1.note.dto.NoteDTO;
import com.simple.portal.biz.v1.note.dto.NoteListDTO;
import com.simple.portal.biz.v1.note.dto.NoteSaveDTO;
import com.simple.portal.biz.v1.note.entity.*;

import com.simple.portal.biz.v1.note.repository.RecvNoteRepository;
import com.simple.portal.biz.v1.note.repository.SendNoteRepository;
import com.simple.portal.util.DateFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteService {

    @Autowired
    SendNoteRepository sendNoteRepository;

    @Autowired
    RecvNoteRepository recvNoteRepository;

    @Autowired
    JPAQueryFactory query;

    @Autowired
    AlarmHistRepository alarmHistRepository;

    @Autowired
    private SimpMessagingTemplate template;

    public Page<NoteDTO> findAll(NoteListDTO noteListDTO) {

        QSendNoteEntity qSendNoteEntity = new QSendNoteEntity("s");
        QRecvNoteEntity qRecvNoteEntity = new QRecvNoteEntity("r");

        int offset = (noteListDTO.getCurrentPage() - 1) * noteListDTO.getSize();

        if (noteListDTO.getGb().equals("R")) { // 받은 목록

            QueryResults<NoteDTO> list = query.select(Projections.bean(NoteDTO.class,
                    qRecvNoteEntity.id,
                    qRecvNoteEntity.revId,
                    qRecvNoteEntity.sendId,
                    qRecvNoteEntity.title,
                    qRecvNoteEntity.contents,
                    qRecvNoteEntity.createdDate
                    ))
                    .from(qRecvNoteEntity)
                    .where(qRecvNoteEntity.revId.contains(noteListDTO.getUserId())
                            .and(qRecvNoteEntity.delYn.eq(false))) // 삭제 안된것만 검색
                    .offset(offset)
                    .limit(noteListDTO.getSize())
                    .fetchResults();

            List<NoteDTO> results = list.getResults().stream()
                    .map(r -> {
                        NoteDTO noteDTO =new NoteDTO();
                        Date date = Date.from(r.getCreatedDate().atZone(ZoneId.systemDefault()).toInstant());
                        String createDate = DateFormatUtil.getTimeBefore(date);
                        noteDTO.setStrCreateDate(createDate);
                        noteDTO.setId(r.getId()); // 상세보기, 삭제 할때 사용하기 위함.
                        noteDTO.setRevId(r.getRevId());
                        noteDTO.setSendId(r.getSendId());
                        noteDTO.setTitle(r.getTitle());
                        noteDTO.setContents(r.getContents());
                        return noteDTO;
                    }).collect(Collectors.toList());

            return new PageImpl(results, PageRequest.of(0, noteListDTO.getSize()), list.getTotal());

        } else { // 보낸 목록
            QueryResults<NoteDTO> list = query.select(Projections.bean(NoteDTO.class,
                    qSendNoteEntity.id,
                    qSendNoteEntity.revId,
                    qSendNoteEntity.sendId,
                    qSendNoteEntity.title,
                    qSendNoteEntity.contents,
                    qSendNoteEntity.createdDate))
                    .from(qSendNoteEntity)
                    .where(qSendNoteEntity.sendId.contains(noteListDTO.getUserId())
                            .and(qSendNoteEntity.delYn.eq(false))) // 삭제 안된것만 검색
                    .offset(offset)
                    .limit(noteListDTO.getSize())
                    .fetchResults();

            List<NoteDTO> results = list.getResults().stream()
                    .map(r -> {
                        NoteDTO noteDTO =new NoteDTO();
                        Date date = Date.from(r.getCreatedDate().atZone(ZoneId.systemDefault()).toInstant());
                        String createDate = DateFormatUtil.getTimeBefore(date);
                        noteDTO.setStrCreateDate(createDate);
                        noteDTO.setId(r.getId()); // 상세보기, 삭제 할때 사용하기 위함.
                        noteDTO.setRevId(r.getRevId());
                        noteDTO.setSendId(r.getSendId());
                        noteDTO.setTitle(r.getTitle());
                        noteDTO.setContents(r.getContents());
                        return noteDTO;
                    }).collect(Collectors.toList());

            return new PageImpl(results, PageRequest.of(0, noteListDTO.getSize()), list.getTotal());
        }
    }
    @Transactional
    public NoteDTO findDetail(Long id, String gb) {

        QSendNoteEntity qSendNoteEntity = new QSendNoteEntity("s");
        QRecvNoteEntity qRecvNoteEntity = new QRecvNoteEntity("r");
        // 받음 쪽지함에서 찾기
        if ("R".equals(gb)) {
            RecvNoteEntity recvNoteEntity = query.select(qRecvNoteEntity)
                    .from(qRecvNoteEntity)
                    .where(qRecvNoteEntity.id.eq(id)
                            .and(qRecvNoteEntity.delYn.eq(false))) // 삭제 안된것만 검색
                    .fetchOne();

            // 알람 삭제
            if (recvNoteEntity != null) {
                alarmHistRepository.deleteByUserIdAndNoteIdAndEventType(
                        recvNoteEntity.getSendId()
                        , recvNoteEntity.getId()
                        , AlarmHistEntity.EventType.EVT_NR);
            }

            NoteDTO noteDTO = new NoteDTO();
            Date date = Date.from(recvNoteEntity.getCreatedDate().atZone(ZoneId.systemDefault()).toInstant());
            String createDate = DateFormatUtil.getTimeBefore(date);

            // 조회수 증가 ? 업데이트 증가 안함?
            if (!sendNoteRepository.findById(id).isEmpty()) {
                SendNoteEntity viewPointUpdateEntity = sendNoteRepository.findById(id).get();
                viewPointUpdateEntity.setViewPoint(1);
                sendNoteRepository.save(viewPointUpdateEntity);
            }

            noteDTO.setId(recvNoteEntity.getId());
            noteDTO.setSendId(recvNoteEntity.getSendId());
            noteDTO.setRevId(recvNoteEntity.getRevId());
            noteDTO.setTitle(recvNoteEntity.getTitle());
            noteDTO.setContents(recvNoteEntity.getContents());
            noteDTO.setStrCreateDate(createDate);
            return noteDTO;

        } else {
            SendNoteEntity sendNoteEntity = query.select(qSendNoteEntity)
                    .from(qSendNoteEntity)
                    .where(qSendNoteEntity.id.eq(id)
                            .and(qSendNoteEntity.delYn.eq(false))) // 삭제 안된것만 검색
                    .fetchOne();

            NoteDTO noteDTO = new NoteDTO();
            Date date = Date.from(sendNoteEntity.getCreatedDate().atZone(ZoneId.systemDefault()).toInstant());
            String createDate = DateFormatUtil.getTimeBefore(date);

            // 조회수 증가 ? 업데이트 증가 안함?
            if (!recvNoteRepository.findById(id).isEmpty()) {
                RecvNoteEntity viewPointUpdateEntity = recvNoteRepository.findById(id).get();
                viewPointUpdateEntity.setViewPoint(1);
                recvNoteRepository.save(viewPointUpdateEntity);
            }

            noteDTO.setId(sendNoteEntity.getId());
            noteDTO.setSendId(sendNoteEntity.getSendId());
            noteDTO.setRevId(sendNoteEntity.getRevId());
            noteDTO.setTitle(sendNoteEntity.getTitle());
            noteDTO.setContents(sendNoteEntity.getContents());
            noteDTO.setStrCreateDate(createDate);
            return noteDTO;
        }
    }

    @Transactional
    public Long save(NoteSaveDTO noteDTO) {
        // 쪽지를 보낼때 보낸쪽지함과 받는쪽지함을 담아 나야 아이디별로 쪽지함을 구별 가능하다.
        // 보낸 편지함
        SendNoteEntity sendNoteEntity = sendNoteRepository.save(SendNoteEntity.builder()
                .title(noteDTO.getTitle())
                .contents(noteDTO.getContents())
                .revId(noteDTO.getRevId())
                .sendId(noteDTO.getSendId())
                .viewPoint(0)
                .delYn(false)
                .build());

        // 받은 편지함
        RecvNoteEntity recvNoteEntity = recvNoteRepository.save(RecvNoteEntity.builder()
                .title(noteDTO.getTitle())
                .contents(noteDTO.getContents())
                .revId(noteDTO.getRevId())
                .sendId(noteDTO.getSendId())
                .viewPoint(0)
                .delYn(false)
                .build());

        if (recvNoteEntity != null) {
            // 알람 등록
            AlarmHistEntity alarmHistEntity = new AlarmHistEntity();
            String userId = noteDTO.getSendId();
            alarmHistEntity.setUserId(userId);
            alarmHistEntity.setNoteId(recvNoteEntity.getId());
            alarmHistEntity.setEventType(AlarmHistEntity.EventType.EVT_NR);
            alarmHistRepository.save(alarmHistEntity);

            this.template.convertAndSend("/socket/sub/note/" + userId, 1);
        }

        return sendNoteEntity.getId();
    }

    // 쪽지는 수정 기능이 없다.
//    public void update(NoteSaveDTO noteDTO) {
//        save(noteDTO);
//    }

    public void delete(Long id, String gb) {
        // 영구보존? 복원할 수 있게 설계함.
        if ("R".equals(gb)) {
            RecvNoteEntity recvNoteEntity = new RecvNoteEntity();
            recvNoteEntity.setDelYn(true);
            recvNoteEntity.setId(id);
            recvNoteRepository.save(recvNoteEntity);
        } else {
            SendNoteEntity sendNoteEntity = new SendNoteEntity();
            sendNoteEntity.setDelYn(true);
            sendNoteEntity.setId(id);
            sendNoteRepository.save(sendNoteEntity);

        }
    }
}
