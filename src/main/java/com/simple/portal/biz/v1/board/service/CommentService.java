package com.simple.portal.biz.v1.board.service;

import com.simple.portal.biz.v1.board.BoardConst;
import com.simple.portal.biz.v1.board.dto.CommentDTO;
import com.simple.portal.biz.v1.board.dto.CommentLikeDTO;
import com.simple.portal.biz.v1.board.entity.CommentEntity;
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

@Service
public class CommentService implements BaseService {

    @Autowired
    CommentRepository commentRepository;

    @Override
    public void setLikeTransaction(Long id) {
        CommentEntity commentEntity = findByIdComment(id);
        commentEntity.setRowLike(increase(commentEntity.getRowLike())); // 좋아요 증가
        commentEntity.setRowDisLike(decrease(commentEntity.getRowDisLike())); // 싫어요 감소
        commentRepository.save(commentEntity);
    }

    @Override
    public void setDisLikeTransaction(Long id) {
        CommentEntity commentEntity = findByIdComment(id);
        commentEntity.setRowLike(decrease(commentEntity.getRowLike())); // 좋아요 감소
        commentEntity.setRowDisLike(increase(commentEntity.getRowDisLike())); // 싫어요 증가
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
    public void setLikeAndDisLike(CommentLikeDTO commentLikeDTO) {

        if (BoardComponent.isItemGbLike(commentLikeDTO.getItemGb())) { // 좋아요
            setLikeTransaction(commentLikeDTO.getId());

        } else if(BoardComponent.isItemGbDisLike(commentLikeDTO.getItemGb())) { // 싫어요
            setDisLikeTransaction(commentLikeDTO.getId());

        } else {
            throw new ItemGubunExecption();
        }
    }

    public CommentEntity findByIdComment(Long id) {
        if (commentRepository.findById(id).isEmpty()){
            throw new BoardDetailNotException();
        }
        return commentRepository.findById(id).get();
    }

    @Transactional
    public void writeComment(CommentEntity commentEntity) {
        commentRepository.save(commentEntity);
    }

    @Transactional
    public void updateComment(CommentEntity commentEntity) {
        CommentEntity upCommentEntity = commentRepository.findById(commentEntity.getId()).get();
        // upCommentEntity.setWriter(commentEntity.getWriter());
        upCommentEntity.setTitle(commentEntity.getTitle());
        upCommentEntity.setContents(commentEntity.getContents());
        commentRepository.save(upCommentEntity);
    }

    public List listComment() {
        return commentRepository.findAllBy();
    }

}
