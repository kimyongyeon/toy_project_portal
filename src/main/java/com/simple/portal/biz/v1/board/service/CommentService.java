package com.simple.portal.biz.v1.board.service;

import com.simple.portal.biz.v1.board.entity.CommentEntity;
import com.simple.portal.biz.v1.board.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    CommentRepository commentRepository;

    public CommentEntity findByIdComment(Long id) {
        if (commentRepository.findById(id).isEmpty()){
            throw new RuntimeException("게시글이 존재하지 않습니다.");
        }
        return commentRepository.findById(id).get();
    }

    public void writeComment(CommentEntity commentEntity) {
        commentRepository.save(commentEntity);
    }

    public List listComment(CommentEntity commentEntity) {
        return (List) commentRepository.findAll();
    }

    public void addLike(CommentEntity commentEntity) {
        commentRepository.save(commentEntity);
    }

    public void addDislike(CommentEntity commentEntity) {
        commentRepository.save(commentEntity);
    }
}
