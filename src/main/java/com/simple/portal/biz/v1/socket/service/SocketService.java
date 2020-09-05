package com.simple.portal.biz.v1.socket.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.simple.portal.biz.v1.socket.dto.SocketNoteDTO;
import com.simple.portal.biz.v1.socket.entity.QSocketNoteEntitiy;
import com.simple.portal.biz.v1.socket.repository.SocketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SocketService {

    @Autowired
    SocketRepository socketRepository;

    @Autowired
    JPAQueryFactory query;

    public SocketNoteDTO findNotReadNote(String userId) {
        QSocketNoteEntitiy qSocketNoteEntitiy = new QSocketNoteEntitiy("n");
        long viewCount = query.select(qSocketNoteEntitiy.viewPoint.count())
                .from(qSocketNoteEntitiy)
                .where(qSocketNoteEntitiy.rev_id.eq(userId)
                       ,qSocketNoteEntitiy.viewPoint.eq(0))
                .fetchOne();
        return SocketNoteDTO.builder()
                .noteNotReadCount(viewCount)
                .build();
    }

//    public SocketCommentDTO findNotReadComment(String userId) {
//        QSocketBoardEntity qSocketBoardEntity = new QSocketBoardEntity("c");
//        List boardId = query.select(qSocketBoardEntity.id.as("boardId")
//                                    )
//                .from(qSocketBoardEntity)
//                .where(
//                        qSocketBoardEntity.writer.contains(userId)
//                        ,qSocketBoardEntity.commentEntity.viewCount.eq(0L)
//                                .or(qSocketBoardEntity.feelEntity.count.eq(0))
//                )
//                .fetch();
//        return SocketCommentDTO.builder()
//                .boardId(boardId)
//                .commentCnt(boardId.size())
//                .build();
//    }

//   public SocketFollowDTO findNotConfirmFollowing (String userId) {
//       QSocketAlarmEntity qSocketAlarmEntity = new QSocketAlarmEntity("a");
//        List dtoList = query.select(qSocketAlarmEntity.user_id)
//                .from(qSocketAlarmEntity)
//                .where(qSocketAlarmEntity.eventType.eq(SocketAlarmEntity.EventType.EVT_UL)
//                       ,qSocketAlarmEntity.user_id.eq(userId))
//                .fetch();
//        return SocketFollowDTO.builder()
//                .userId(dtoList)
//                .newFollowingCount(dtoList.size())
//                .build();
//    }
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
