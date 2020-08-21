package com.simple.portal.biz.v1.board.service;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.simple.portal.biz.v1.board.dto.*;
import com.simple.portal.biz.v1.board.entity.BoardEntity;
import com.simple.portal.biz.v1.board.entity.QBoardEntity;
import com.simple.portal.biz.v1.board.entity.QCommentEntity;
import com.simple.portal.biz.v1.board.exception.BoardDetailNotException;
import com.simple.portal.biz.v1.board.exception.ItemGubunExecption;
import com.simple.portal.biz.v1.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.querydsl.core.types.ExpressionUtils.count;

@Service
public class BoardService implements BaseService {

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    JPAQueryFactory query;

    @Override
    public void setLikeTransaction(Long id) {
        BoardEntity boardEntity = boardRepository.findById(id).get();
        boardEntity.setRowLike(increase(1L)); // 좋아요 증가
        boardEntity.setRowDisLike(decrease(1L)); // 싫어요 감소
        boardRepository.save(boardEntity);
    }

    @Override
    public void setDisLikeTransaction(Long id) {
        BoardEntity boardEntity = boardRepository.findById(id).get();
        boardEntity.setRowLike(decrease(1L)); // 좋아요 감소
        boardEntity.setRowDisLike(increase(1L)); // 싫어요 증가
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

    /**
     * Async 처리시 오류가 나면 그냥 200을 던지는 오류를 범함.
     * @param boardLikeDTO
     */
    @Transactional
    public void setLikeAndDisLike(BoardLikeDTO boardLikeDTO) {

        if (BoardComponent.isItemGbLike(boardLikeDTO.getItemGb())) { // 좋아요
            setLikeTransaction(Long.parseLong(boardLikeDTO.getId()));

        } else if (BoardComponent.isItemGbDisLike(boardLikeDTO.getItemGb())){ // 싫어요
            setDisLikeTransaction(Long.parseLong(boardLikeDTO.getId()));

        } else {
            throw new ItemGubunExecption();
        }
    }

//    public List<BoardDTO> search(BoardReqDTO boardDTO) {
//        return boardRepository.findAllByTitleOrContents(boardDTO.getTitle(), boardDTO.getContents());
//    }

    public Page<BoardDTO> pageList(BoardSearchDTO boardSearchDTO) {

        /**
         * SELECT *,
         *  (SELECT count(*) FROM tb_comment WHERE tb_comment.board_id = b.board_id ) AS comment_cnt
         * FROM tb_board b
         * order by comment_cnt desc
         */

        // boardSearchDTO.getTitle(), PageRequest.of(boardSearchDTO.getPage(), boardSearchDTO.getSize()))
        QBoardEntity qBoardEntity = new QBoardEntity("b");
        QCommentEntity qCommentEntity = new QCommentEntity("c");
        // 테이블 구조 그대로 목록을 뽑는다.
        int offset = (boardSearchDTO.getCurrentPage() - 1) * boardSearchDTO.getSize();
        QueryResults<BoardDTO> boards = query
//                .select(qBoardEntity)
                .select(Projections.bean(BoardDTO.class,
                        qBoardEntity.id,
                        qBoardEntity.title,
                        qBoardEntity.contents,
                        qBoardEntity.writer,
                        qBoardEntity.rowLike,
                        qBoardEntity.rowDisLike,
                        qBoardEntity.viewCount,
                        qBoardEntity.createdDate,
                        ExpressionUtils.as(
                                JPAExpressions.select(count(qCommentEntity.id))
                                        .from(qCommentEntity)
                                        .where(qCommentEntity.boardEntity.id.eq(qBoardEntity.id)),
                                "commentCnt")
                        ))
                .from(qBoardEntity)
                .where(getContains(boardSearchDTO, qBoardEntity)) // 검색 조건
                .orderBy(getDesc(qBoardEntity, boardSearchDTO.getSort())) // 정렬
                .offset(offset)
                .limit(boardSearchDTO.getSize())
                .fetchResults();
        return new PageImpl(boards.getResults(), PageRequest.of(boardSearchDTO.getCurrentPage(), boardSearchDTO.getSize()),boards.getTotal());
    }

    private BooleanExpression getContains(BoardSearchDTO boardSearchDTO, QBoardEntity qBoardEntity) {
        if (boardSearchDTO.getGb().equals("title")) {
            return qBoardEntity.title.contains(boardSearchDTO.getKeyword());
        } else if (boardSearchDTO.getGb().equals("contents")) {
            return qBoardEntity.contents.contains(boardSearchDTO.getKeyword());
        } else if (boardSearchDTO.getGb().equals("writer")) {
            return qBoardEntity.writer.contains(boardSearchDTO.getKeyword());
        } else {
            return qBoardEntity.title.contains(boardSearchDTO.getKeyword());
        }
    }

    private OrderSpecifier getDesc(QBoardEntity qBoardEntity, String sort) {
        if (sort.equals("title")) {
            return qBoardEntity.title.desc();
        } else if (sort.equals("contents")) {
            return qBoardEntity.contents.desc();
        } else if (sort.equals("writer")) {
            return qBoardEntity.writer.desc();
        } else if (sort.equals("date")) { // 날짜
            return qBoardEntity.createdDate.desc();
        } else if (sort.equals("like")) { // 좋아요 순
            return qBoardEntity.rowLike.desc();
        } else if (sort.equals("viewCount")) { // 조회수 순
            return qBoardEntity.viewCount.desc();
        } else if (sort.equals("commentCnt")) { /// 댓글 순
            Path<Long> commentCnt = Expressions.numberPath(Long.class, "commentCnt");
            return ((ComparableExpressionBase<Long>) commentCnt).desc();
        }
        else {
            return qBoardEntity.title.desc();
        }
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

    @Transactional
    public Long insert(BoardReqWriteDTO boardDTO) {
        BoardEntity boardEntity = boardRepository.save(BoardEntity.builder()
                .title(boardDTO.getTitle())
                .contents(boardDTO.getContents())
                .viewCount(0L)
                .rowLike(0L)
                .rowDisLike(0L)
                .writer(boardDTO.getWriter())
                .build());
        return boardEntity.getId();
    }

    @Transactional
    public void updateTitleOrContents(BoardReqUpdateDTO boardDTO) {

        if (boardRepository.findById(boardDTO.getId()).isEmpty()) {
            throw new BoardDetailNotException();
        }

        BoardEntity boardEntity = boardRepository.findById(boardDTO.getId()).get();
        boardEntity.setTitle(boardDTO.getTitle());
        boardEntity.setContents(boardDTO.getContents());
        boardRepository.save(boardEntity);
    }

    @Transactional
    public void idDelete(Long id) {
        boardRepository.deleteById(id);
    }

}
