package com.simple.portal.biz.v1.socket.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.simple.portal.biz.v1.socket.dto.SocketDTO;
import com.simple.portal.biz.v1.socket.entity.QSocketNoteEntitiy;
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

    public List findNotReadNote(String userId) {
        QSocketNoteEntitiy qSocketNoteEntitiy = new QSocketNoteEntitiy("n");
        return query.select(qSocketNoteEntitiy.viewPoint.count().as("notReadTotalCount"))
                .from(qSocketNoteEntitiy)
                .where(
                        qSocketNoteEntitiy.rev_id.contains(userId)
                        ,qSocketNoteEntitiy.viewPoint.eq(0)
                      )
                .fetch();
    }

    public List find(String userId) {
        QSocketNoteEntitiy qSocketNoteEntitiy = new QSocketNoteEntitiy("n");
        return query.select(qSocketNoteEntitiy.viewPoint.count())
                .from(qSocketNoteEntitiy)
                .where(
                        qSocketNoteEntitiy.rev_id.contains(userId)
                        ,qSocketNoteEntitiy.viewPoint.eq(0)
                )
                .fetch();
    }
}
