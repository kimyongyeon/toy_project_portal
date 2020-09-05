package com.simple.portal.biz.v1.socket.service;

import com.querydsl.core.NonUniqueResultException;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.simple.portal.biz.v1.board.entity.*;
import com.simple.portal.biz.v1.board.exception.ItemGubunExecption;
import com.simple.portal.biz.v1.socket.dto.SocketCommentDTO;
import com.simple.portal.biz.v1.socket.dto.SocketFollowDTO;
import com.simple.portal.biz.v1.socket.dto.SocketNoteDTO;
import com.simple.portal.biz.v1.socket.entity.QSocketNoteEntitiy;
import com.simple.portal.biz.v1.socket.exception.ReadNoteNotException;
import com.simple.portal.biz.v1.socket.repository.SocketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SocketService {

    @Autowired
    SocketRepository socketRepository;

    @Autowired
    JPAQueryFactory query;

    public SocketNoteDTO findNotReadNote(String userId) {
        QSocketNoteEntitiy qSocketNoteEntitiy = new QSocketNoteEntitiy("n");
        long viewCount = 0;
        try {
            viewCount = query.select(qSocketNoteEntitiy.viewPoint.count())
                    .from(qSocketNoteEntitiy)
                    .where(qSocketNoteEntitiy.rev_id.eq(userId)
                            ,qSocketNoteEntitiy.viewPoint.eq(0))
                    .fetchOne();
        } catch (NullPointerException e) {
            throw new ReadNoteNotException();
        }

        return SocketNoteDTO.builder()
                .noteNotReadCount(viewCount)
                .build();
    }

    public SocketCommentDTO findNotReadComment(String userId) {
        QBoardEntity qBoardEntity = new QBoardEntity("b");
        QCommentEntity qCommentEntity = new QCommentEntity("c");
        QFeelEntity qFeelEntity = new QFeelEntity("f");
        List boardId = query.select(qBoardEntity.id.as("boardId")
        )
                .from(qBoardEntity)
                .join(qCommentEntity).on(qCommentEntity.boardEntity.eq(qBoardEntity))
                .join(qFeelEntity).on(qFeelEntity.boardId.eq(qBoardEntity.id))
                .where(
                        qBoardEntity.writer.eq(userId)
                        ,qCommentEntity.viewCount.eq(0L)
                                .or(qFeelEntity.count.eq(0))
                )
                .fetch();
        return SocketCommentDTO.builder()
                .boardId(boardId)
                .commentCnt(boardId.size())
                .build();
    }

    public SocketFollowDTO findNotConfirmFollowing (String userId) {
        QAlarmHistEntity qAlarmHistEntity = new QAlarmHistEntity("a");
        List dtoList = query.select(qAlarmHistEntity.userId)
                .from(qAlarmHistEntity)
                .where(qAlarmHistEntity.eventType.eq(AlarmHistEntity.EventType.valueOf("EVT_UL"))
                        ,qAlarmHistEntity.userId.eq(userId))
                .fetch();
        return SocketFollowDTO.builder()
                .followList(dtoList)
                .newFollowingCount(dtoList.size())
                .build();
    }
/*
    public SocketCommentDTO findNotReadCommentDetail(String userId) {
        QSocketBoardEntity BoardEntity = new QSocketBoardEntity("c");
        List boardId = query.select(BoardEntity.id.as("boardId")
        )
                .from(BoardEntity)
                .where(
                        BoardEntity.writer.contains(userId)
                        ,BoardEntity.commentEntity.viewCount.eq(0L)
                                .or(BoardEntity.feelEntity.count.eq(0))
                )
                .fetch();
        return SocketCommentDTO.builder()
                .boardId(boardId)
                .commentCnt(boardId.size())
                .build();
    }*/
}
