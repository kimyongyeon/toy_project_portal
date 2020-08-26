package com.simple.portal.biz.v1.socket.service;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.simple.portal.biz.v1.socket.dto.SocketCommentDTO;
import com.simple.portal.biz.v1.socket.dto.SocketDTO;
import com.simple.portal.biz.v1.socket.entity.QSocketCommentEntity;
import com.simple.portal.biz.v1.socket.entity.QSocketNoteEntitiy;
import com.simple.portal.biz.v1.socket.entity.SocketNoteEntitiy;
import com.simple.portal.biz.v1.socket.repository.SocketRepository;
import com.simple.portal.util.DateFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SocketService {

    @Autowired
    SocketRepository socketRepository;

    @Autowired
    JPAQueryFactory query;

    public SocketDTO findNotReadNote(String userId) {
        QSocketNoteEntitiy qSocketNoteEntitiy = new QSocketNoteEntitiy("n");
        long viewCount = query.select(qSocketNoteEntitiy.viewPoint.count())
                .from(qSocketNoteEntitiy)
                .where(qSocketNoteEntitiy.rev_id.eq(userId)
                       ,qSocketNoteEntitiy.viewPoint.eq(0))
                .fetchOne();
        return SocketDTO.builder()
                .noteNotReadCount(viewCount)
                .build();
    }

    public SocketCommentDTO findNotReadComment(String userId) {
        QSocketCommentEntity qSocketCommentEntity = new QSocketCommentEntity("c");
        List boardId = query.select(qSocketCommentEntity.boardEntity.id.as("boardId"))
                .from(qSocketCommentEntity)
                .where(
                        qSocketCommentEntity.boardEntity.writer.contains(userId)
                        ,qSocketCommentEntity.viewCount.eq(0L)
                )
                .fetch();
        return SocketCommentDTO.builder()
                .boardId(boardId)
                .commentCnt(boardId.size())
                .build();
    }
}
