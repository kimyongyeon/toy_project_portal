package com.simple.portal.biz.v1.board.service;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.simple.portal.biz.v1.board.BoardConst;
import com.simple.portal.biz.v1.board.dto.BoardDTO;
import com.simple.portal.biz.v1.board.dto.CommentDTO;
import com.simple.portal.biz.v1.board.dto.CommentEditDTO;
import com.simple.portal.biz.v1.board.dto.CommentLikeDTO;
import com.simple.portal.biz.v1.board.entity.CommentEntity;
import com.simple.portal.biz.v1.board.entity.QCommentEntity;
import com.simple.portal.biz.v1.board.exception.BoardDetailNotException;
import com.simple.portal.biz.v1.board.exception.ItemGubunExecption;
import com.simple.portal.biz.v1.board.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.swing.text.html.HTMLDocument;
import javax.transaction.Transactional;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class CommentService implements BaseService {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    JPAQueryFactory query;

    @Override
    public void setLikeTransaction(Long id) {
        CommentEntity commentEntity = findByIdComment(id);
        commentEntity.setRowLike(increase(1L)); // 좋아요 증가
        commentEntity.setRowDisLike(decrease(1L)); // 싫어요 감소
        commentRepository.save(commentEntity);
    }

    @Override
    public void setDisLikeTransaction(Long id) {
        CommentEntity commentEntity = findByIdComment(id);
        commentEntity.setRowLike(decrease(1L)); // 좋아요 감소
        commentEntity.setRowDisLike(increase(1L)); // 싫어요 증가
        commentRepository.save(commentEntity);
    }

    @Override
    public Long increase(Long curVal) {
        return curVal + 1;
    }

    @Override
    public Long decrease(Long currVal) {
        return currVal - 1;
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
    public void writeComment(CommentEntity commentEntity) {
        commentRepository.save(commentEntity);
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
