package com.simple.portal.biz.v1.board.service;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.simple.portal.biz.v1.board.BoardConst;
import com.simple.portal.biz.v1.board.dto.BoardDTO;
import com.simple.portal.biz.v1.board.entity.BoardEntity;
import com.simple.portal.biz.v1.board.entity.QBoardEntity;
import com.simple.portal.biz.v1.board.entity.QCommentEntity;
import com.simple.portal.biz.v1.board.exception.BoardDetailNotException;
import com.simple.portal.biz.v1.board.exception.ItemGubunExecption;
import com.simple.portal.biz.v1.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class BoardService {

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    JPAQueryFactory query;

    @Transactional
    public void setLike(BoardDTO boardDTO) {
        BoardEntity boardEntity = findById(boardDTO.getId());

        if (BoardConst.isItemGbLike(boardDTO.getItemGb())) { // 좋아요
            boardEntity.setRowLike(boardEntity.getRowLike() + 1);
            boardEntity.setRowDisLike(boardEntity.getRowDisLike() - 1);
            addLike(boardEntity);

        } else if (BoardConst.isItemGbDisLike(boardDTO.getItemGb())){ // 싫어요
            boardEntity.setRowLike(boardEntity.getRowLike() - 1);
            boardEntity.setRowDisLike(boardEntity.getRowDisLike() + 1);
            addDislike(boardEntity);
        } else {
            throw new ItemGubunExecption();
        }
    }

    public List search(BoardDTO boardDTO) {
        return boardRepository.findAllByTitleOrContents(boardDTO.getTitle(), boardDTO.getContents());
    }

    public Page<BoardEntity> pageList(String title, Pageable pageable) {
        return boardRepository.findByTitleContaining(title, pageable);
    }

    public List myScrap(String userId) {
        QBoardEntity qBoardEntity = new QBoardEntity("b");
        return query
                .select(qBoardEntity)
                .from(qBoardEntity)
                .where(qBoardEntity.title.contains("title")
                ,qBoardEntity.writer.contains(userId)
                ) // 스크랩 유무?
                .orderBy(qBoardEntity.title.desc())
                .fetch();
    }

    public List userBoardList(String userId) {
        QBoardEntity qBoardEntity = new QBoardEntity("b");
        return query
                .select(qBoardEntity)
                .from(qBoardEntity)
                .where(qBoardEntity.writer.contains(userId))
                .orderBy(qBoardEntity.title.desc())
                .fetch();
    }

    public List recentBoardList(String userId) {
        QBoardEntity qBoardEntity = new QBoardEntity("b");
        QCommentEntity qCommentEntity = new QCommentEntity("c");
        return query
                .select(Projections.constructor(BoardDTO.class, qBoardEntity.title, qCommentEntity.writer))
                .from(qBoardEntity)
                .join(qCommentEntity).on(qCommentEntity.boardEntity.eq(qBoardEntity))
                .where(qBoardEntity.writer.contains(userId))
                .fetch();
    }

    public BoardEntity findById(Long id) {
        if (boardRepository.findById(id).isEmpty()) {
            throw new BoardDetailNotException();
        }
        return boardRepository.findById(id).get();
    }

    @Async
    @Transactional
    public void insert(BoardDTO boardDTO) {
        boardRepository.save(BoardEntity.builder()
                .title(boardDTO.getTitle())
                .contents(boardDTO.getContents())
                .viewCount(0L)
                .rowLike(0)
                .rowDisLike(0)
                .writer(boardDTO.getWriter())
                .build());
    }

    @Transactional
    public BoardEntity save(BoardEntity boardEntity) {
        return boardRepository.save(boardEntity);
    }

    @Async
    @Transactional
    public void titleOrContentsUpdate(BoardDTO boardDTO) {

        if (boardRepository.findById(boardDTO.getId()).isEmpty()) {
            throw new BoardDetailNotException();
        }

        BoardEntity boardEntity = boardRepository.findById(boardDTO.getId()).get();
        boardEntity.setTitle(boardDTO.getTitle());
        boardEntity.setContents(boardDTO.getContents());
        boardRepository.save(boardEntity);
    }

    @Async
    @Transactional
    public void idDelete(BoardDTO boardDTO) {
        boardRepository.delete(BoardEntity.builder().id(boardDTO.getId()).build());
    }

    @Async
    @Transactional
    public void addLike(BoardEntity boardEntity) {
        boardRepository.save(boardEntity);
    }

    @Async
    @Transactional
    public void addDislike(BoardEntity boardEntity) {
        boardRepository.save(boardEntity);
    }

}
