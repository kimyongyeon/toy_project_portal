package com.simple.portal.biz.v1.board.service;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.simple.portal.biz.v1.board.dto.BoardDTO;
import com.simple.portal.biz.v1.board.dto.BoardIdDTO;
import com.simple.portal.biz.v1.board.dto.BoardLikeDTO;
import com.simple.portal.biz.v1.board.entity.BoardEntity;
import com.simple.portal.biz.v1.board.entity.QBoardEntity;
import com.simple.portal.biz.v1.board.entity.QCommentEntity;
import com.simple.portal.biz.v1.board.exception.BoardDetailNotException;
import com.simple.portal.biz.v1.board.exception.ItemGubunExecption;
import com.simple.portal.biz.v1.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class BoardService implements BaseService {

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    JPAQueryFactory query;

    @Override
    public void setLikeTransaction(Long id) {
        BoardEntity boardEntity = boardRepository.findById(id).get();
        boardEntity.setRowLike(increase(boardEntity.getRowLike())); // 좋아요 증가
        boardEntity.setRowDisLike(decrease(boardEntity.getRowDisLike())); // 싫어요 감소
        boardRepository.save(boardEntity);
    }

    @Override
    public void setDisLikeTransaction(Long id) {
        BoardEntity boardEntity = boardRepository.findById(id).get();
        boardEntity.setRowLike(decrease(boardEntity.getRowLike())); // 좋아요 감소
        boardEntity.setRowDisLike(increase(boardEntity.getRowDisLike())); // 싫어요 증가
        boardRepository.save(boardEntity);
    }

    @Override
    public Long increase(Long curVal) {
        return curVal + 1;
    }

    @Override
    public Long decrease(Long currVal) {
        return currVal - 1;
    }

    @Async
    @Transactional
    public void setLikeAndDisLike(BoardLikeDTO boardLikeDTO) {

        if (BoardComponent.isItemGbLike(boardLikeDTO.getItemGb())) { // 좋아요
            setLikeTransaction(boardLikeDTO.getId());

        } else if (BoardComponent.isItemGbDisLike(boardLikeDTO.getItemGb())){ // 싫어요
            setDisLikeTransaction(boardLikeDTO.getId());

        } else {
            throw new ItemGubunExecption();
        }
    }

    public List<BoardDTO> search(BoardDTO boardDTO) {
        return boardRepository.findAllByTitleOrContents(boardDTO.getTitle(), boardDTO.getContents());
    }

    public List<BoardDTO> pageList(String title, Pageable pageable) {
        Page<BoardEntity> pages = boardRepository.findByTitleContaining(title, pageable);
        return getBoardDTOS(pages.stream());
    }

    public List<BoardDTO> myScrap(String userId) {

        QBoardEntity qBoardEntity = new QBoardEntity("b");

        // 테이블 구조 그대로 목록을 뽑는다.
        List<BoardEntity> boards = query
                .select(qBoardEntity)
                .from(qBoardEntity)
                .where(
                        qBoardEntity.title.contains("title")
                        ,qBoardEntity.writer.contains(userId)
                ) // 스크랩 유무?
                .orderBy(qBoardEntity.title.desc())
                .fetch();

        // 테이블 구조를 공개하지 않기 위해 DTO에 담는다.
        return getBoardDTOS(boards.stream());
    }

    private List<BoardDTO> getBoardDTOS(Stream<BoardEntity> stream) {
        List<BoardDTO> list = stream.map(b -> {
            BoardDTO boardDTO = new BoardDTO();
            boardDTO.setWriter(b.getWriter());           // 글쓴이
            boardDTO.setTitle(b.getTitle());             // 제목
            boardDTO.setContents(b.getContents());       // 내용
            boardDTO.setId(b.getId());                   // 게시판 아이디
            boardDTO.setViewCount(b.getViewCount());     // 조회수
            boardDTO.setRowLike(b.getRowLike());         // 좋아요 개수
            boardDTO.setRowDisLike(b.getRowDisLike());   // 싫어요 개수
            boardDTO.setCreatedDate(b.getCreatedDate()); // 작성일자
            return boardDTO;
        }).collect(Collectors.toList());
        return list;
    }

    public List<BoardDTO> userBoardList(String userId) {
        QBoardEntity qBoardEntity = new QBoardEntity("b");
        List<BoardEntity> boards = query
                .select(qBoardEntity)
                .from(qBoardEntity)
                .where(qBoardEntity.writer.contains(userId))
                .orderBy(qBoardEntity.title.desc())
                .fetch();
        return getBoardDTOS(boards.stream());
    }

    public List<BoardDTO> recentBoardList(String userId) {
        QBoardEntity qBoardEntity = new QBoardEntity("b");
        QCommentEntity qCommentEntity = new QCommentEntity("c");
        List<BoardDTO> boards = query
                .select(Projections.constructor(BoardDTO.class, qBoardEntity, qCommentEntity))
                .from(qBoardEntity)
                .join(qCommentEntity).on(qCommentEntity.boardEntity.eq(qBoardEntity))
                .where(qBoardEntity.writer.contains(userId))
                .fetch();
        return boards;
    }

    public BoardDTO findById(Long id) {

        if (boardRepository.findById(id).isEmpty()) {
            throw new BoardDetailNotException();
        }

        BoardEntity boardEntity = boardRepository.findById(id).get();
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(boardEntity.getId());
        boardDTO.setTitle(boardEntity.getTitle());
        boardDTO.setContents(boardEntity.getContents());
        boardDTO.setWriter(boardEntity.getWriter());
        boardDTO.setCreatedDate(boardEntity.getCreatedDate());
        boardDTO.setViewCount(boardEntity.getViewCount());
        boardDTO.setRowLike(boardEntity.getRowLike());
        boardDTO.setRowDisLike(boardEntity.getRowDisLike());

        return boardDTO;
    }

    @Async
    @Transactional
    public void insert(BoardDTO boardDTO) {
        boardRepository.save(BoardEntity.builder()
                .title(boardDTO.getTitle())
                .contents(boardDTO.getContents())
                .viewCount(0L)
                .rowLike(0L)
                .rowDisLike(0L)
                .writer(boardDTO.getWriter())
                .build());
    }

    @Async
    @Transactional
    public void updateTitleOrContents(BoardDTO boardDTO) {

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
    public void idDelete(BoardIdDTO boardIdDTO) {
        boardRepository.delete(BoardEntity.builder().id(boardIdDTO.getId()).build());
    }

}
