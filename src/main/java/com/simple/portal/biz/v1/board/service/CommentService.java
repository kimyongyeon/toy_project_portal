package com.simple.portal.biz.v1.board.service;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.simple.portal.biz.v1.board.BoardConst;
import com.simple.portal.biz.v1.board.dto.BoardDTO;
import com.simple.portal.biz.v1.board.dto.CommentDTO;
import com.simple.portal.biz.v1.board.dto.CommentEditDTO;
import com.simple.portal.biz.v1.board.dto.CommentLikeDTO;
import com.simple.portal.biz.v1.board.entity.*;
import com.simple.portal.biz.v1.board.exception.BoardDetailNotException;
import com.simple.portal.biz.v1.board.exception.ItemGubunExecption;
import com.simple.portal.biz.v1.board.repository.ActivityScoreRepository;
import com.simple.portal.biz.v1.board.repository.AlarmHistRepository;
import com.simple.portal.biz.v1.board.repository.BoardRepository;
import com.simple.portal.biz.v1.board.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.swing.text.html.HTMLDocument;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class CommentService implements BaseService {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    JPAQueryFactory query;

    @Autowired
    ActivityScoreRepository activityScoreRepository;

    @Autowired
    AlarmHistRepository alarmHistRepository;

    @Autowired
    BoardRepository boardRepository;

    @Override
    public void setLikeTransaction(Long id) {
        CommentEntity commentEntity = findByIdComment(id);
        commentEntity.setRowLike(increase(commentEntity.getRowLike())); // 좋아요 증가
//        commentEntity.setRowDisLike(decrease(1L)); // 싫어요 감소
        commentRepository.save(commentEntity);
    }

    @Override
    public void setDisLikeTransaction(Long id) {
        CommentEntity commentEntity = findByIdComment(id);
//        commentEntity.setRowLike(decrease(commentEntity.getRowLike())); // 좋아요 감소
        commentEntity.setRowDisLike(increase(commentEntity.getRowDisLike())); // 싫어요 증가
        commentRepository.save(commentEntity);
    }

    @Override
    public Long increase(Long curVal) {
        return curVal + 1;
    }

    @Override
    public Long decrease(Long currVal) {
        if (currVal < 0) {
            return 0L;
        } else if (currVal == 0) {
            return 0L;
        }   else {
            return currVal - 1;
        }
    }

    @Transactional
    public void remove(Long id) {
        commentRepository.deleteById(id);
    }
    @Transactional
    public void removeAll(List<Long> ids) {
        for(Long id: ids) {
            remove(id);
        }
    }

    @Transactional
    public void setLikeAndDisLike(CommentLikeDTO commentLikeDTO) {

        if (BoardComponent.isItemGbLike(commentLikeDTO.getItemGb())) { // 좋아요
            setLikeTransaction(commentLikeDTO.getId());

        } else if (BoardComponent.isItemGbDisLike(commentLikeDTO.getItemGb())) { // 싫어요
            setDisLikeTransaction(commentLikeDTO.getId());

        } else {
            throw new ItemGubunExecption();
        }
    }

    public CommentDTO findById(Long id) {
        CommentEntity commentEntity = commentRepository.findById(id).get();
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(commentEntity.getId());
        commentDTO.setTitle(commentEntity.getTitle());
        commentDTO.setContents(commentEntity.getContents());
        commentDTO.setRowLike(commentEntity.getRowLike());
        commentDTO.setRowDisLike(commentEntity.getRowDisLike());
        commentDTO.setViewCount(commentEntity.getViewCount());
        commentDTO.setBoardId(commentEntity.getBoardEntity().getId());
        return commentDTO;
    }

    public CommentEntity findByIdComment(Long id) {
        if (commentRepository.findById(id).isEmpty()) {
            throw new BoardDetailNotException();
        }
        return commentRepository.findById(id).get();
    }

    @Transactional
    public CommentEntity writeComment(CommentEntity commentEntity) {

        // 1. 댓글 저장
        CommentEntity commentEntity1 = commentRepository.save(commentEntity);
        if (commentEntity1 != null) {

            // 2. 활동점수 저장 : 활동점수는 댓글난 당사자의 잠수가 올라간다.
            ActivityScoreEntity activityScoreEntity = new ActivityScoreEntity();
            activityScoreEntity.setType(ActivityScoreEntity.ScoreType.COMMENT);
            activityScoreEntity.setUserId(commentEntity1.getWriter());
            activityScoreEntity.setScore(2L);
            activityScoreRepository.save(activityScoreEntity);

            // 3. 댓글알람 저장 : 댓글을 달면 댓글에 해당하는 게시글 주인에게 알람 카운트를 올려준다.
            // 댓글알람 +1
            // 댓글을 쓰면 이 댓글의 게시글의 주인을 찾아서 알람을 추가 한다.
            Long boardId = commentEntity.getBoardEntity().getId();
            Optional<BoardEntity> boardEntity = boardRepository.findById(boardId);
            if (!boardEntity.isEmpty()) {
                String userId = boardEntity.get().getWriter();
                AlarmHistEntity alarmHistEntity = new AlarmHistEntity();
                alarmHistEntity.setUserId(userId);
                alarmHistEntity.setBoardId(boardId);
                alarmHistEntity.setEventType(AlarmHistEntity.EventType.EVT_BC);
                alarmHistRepository.save(alarmHistEntity);

            } else {
                throw new RuntimeException("존재 하지 않는 게시글 입니다.");
            }
        }
        return commentEntity1;
    }

    @Transactional
    public Long updateComment(CommentEntity commentEntity) {
        CommentEntity upCommentEntity = commentRepository.findById(commentEntity.getId()).get();
        // upCommentEntity.setWriter(commentEntity.getWriter());
        upCommentEntity.setTitle(commentEntity.getTitle());
        upCommentEntity.setContents(commentEntity.getContents());
        CommentEntity commentEntity1 = commentRepository.save(upCommentEntity);
        return commentEntity1.getId();
    }

    public List listComment(Long boardId) {

        QCommentEntity qCommentEntity = new QCommentEntity("c");
        QueryResults<CommentDTO> comments = query
                .select(Projections.bean(CommentDTO.class,
                        qCommentEntity.id,
                        qCommentEntity.writer,
                        qCommentEntity.title,
                        qCommentEntity.contents,
                        qCommentEntity.rowDisLike,
                        qCommentEntity.rowLike,
                        qCommentEntity.viewCount, qCommentEntity.createdDate))
                .from(qCommentEntity)
                .where(qCommentEntity.boardEntity.id.eq(boardId))
                .limit(10)
                .fetchResults();

        return comments.getResults().stream().map(c -> {
                    c.setBoardId(boardId);
                    return c;
                })
                .collect(Collectors.toList());

    }

}
