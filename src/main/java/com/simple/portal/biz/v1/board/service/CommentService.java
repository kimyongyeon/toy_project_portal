package com.simple.portal.biz.v1.board.service;

import com.simple.portal.biz.v1.board.BoardConst;
import com.simple.portal.biz.v1.board.dto.CommentDTO;
import com.simple.portal.biz.v1.board.entity.BoardEntity;
import com.simple.portal.biz.v1.board.entity.CommentEntity;
import com.simple.portal.biz.v1.board.exception.BoardDetailNotException;
import com.simple.portal.biz.v1.board.exception.ItemGubunExecption;
import com.simple.portal.biz.v1.board.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    CommentRepository commentRepository;

    @Transactional
    public void setLike(CommentDTO commentDTO) {
        CommentEntity commentEntity = findByIdComment(commentDTO.getId());
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setId(commentDTO.getBoardId());
        commentEntity.setBoardEntity(boardEntity);

        if (BoardConst.isItemGbLike(commentDTO.getItemGb())) { // 좋아요
            commentEntity.setRowLike(commentEntity.getRowLike() + 1);
            commentEntity.setRowDisLike(commentEntity.getRowDisLike() - 1);
            addLike(commentEntity);
        } else if(BoardConst.isItemGbDisLike(commentDTO.getItemGb())) { // 싫어요
            commentEntity.setRowLike(commentEntity.getRowLike() - 1);
            commentEntity.setRowDisLike(commentEntity.getRowDisLike() + 1);
            addDislike(commentEntity);
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

    public List listComment() {
        return commentRepository.findAllBy();
    }

    @Transactional
    public void addLike(CommentEntity commentEntity) {
        commentRepository.save(commentEntity);
    }

    @Transactional
    public void addDislike(CommentEntity commentEntity) {
        commentRepository.save(commentEntity);
    }
}
