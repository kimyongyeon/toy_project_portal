package com.simple.portal.biz.v1.board.service;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.simple.portal.biz.v1.board.dto.*;
import com.simple.portal.biz.v1.board.entity.*;
import com.simple.portal.biz.v1.board.exception.BoardDetailNotException;
import com.simple.portal.biz.v1.board.exception.ItemGubunExecption;
import com.simple.portal.biz.v1.board.repository.*;
import com.simple.portal.biz.v1.user.entity.QUserEntity;
import com.simple.portal.biz.v1.user.entity.UserEntity;
import com.simple.portal.biz.v1.user.repository.UserRepository;
import com.simple.portal.biz.v1.user.service.UserService;
import com.simple.portal.util.ActivityScoreConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.querydsl.core.types.ExpressionUtils.count;

@Service
@Slf4j
public class BoardService {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    JPAQueryFactory query;

    @Autowired
    ActivityScoreRepository activityScoreRepository;

    @Autowired
    ScrapRepository scrapRepository;

    @Autowired
    AlarmHistRepository alarmHistRepository;

    @Autowired
    UserService userService;

    @Autowired
    FeelRepository feelRepository;

    @Autowired
    UserRepository userRepository;

    public void removeScrap(ScrapDTO scrapDTO) {
        scrapRepository.deleteById(scrapDTO.getId());
    }

    public ScrapEntity saveScrap(ScrapDTO scrapDTO) {
        Optional<BoardEntity> byId = boardRepository.findById(scrapDTO.getBoardId());
        if (byId.isEmpty()) {
            throw new RuntimeException("게시글 키가 없는 내용 입니다.");
        } else {
            ScrapEntity scrapEntity1 = scrapRepository.findByUserIdAndBoardId(scrapDTO.getUserId(), scrapDTO.getBoardId());
            if (scrapEntity1 == null) {
                ScrapEntity scrapEntity = new ScrapEntity();
                scrapEntity.setBoardId(scrapDTO.getBoardId());
                scrapEntity.setUserId(scrapDTO.getUserId());
                return scrapRepository.save(scrapEntity);
            } else {
                throw new RuntimeException("이미 등록한 스크랩 입니다.");
            }
        }
    }

    public void setLikeTransaction(Long id, String userId) {
        BoardEntity boardEntity = boardRepository.findById(id).get();

        Long boardId = id;
        String feelLike = "L";
        String feelState = feelLike;
        FeelEntity feelEntity = feelRepository.findByUserIdAndBoardIdAndFeelState(userId, boardId, feelState);
        if (feelEntity == null) {
            // insert
            FeelEntity feelEntity1 = new FeelEntity();
            feelEntity1.setBoardId(id);
            feelEntity1.setCount(1);
            feelEntity1.setUserId(userId);
            feelEntity.setFeelState(feelLike);
            feelRepository.save(feelEntity1);
        } else {
            // delete
            feelRepository.delete(feelEntity);
        }
        Long likeTotalSumCnt = feelRepository.selectTocalCount(feelLike, boardId);
        // 좋아요/싫어요 따로 집계
        boardEntity.setRowLike(likeTotalSumCnt); // 좋아요 증가
//        boardEntity.setRowDisLike(decrease(boardEntity.getRowDisLike())); // 싫어요 감소
        BoardEntity boardEntity1 = boardRepository.save(boardEntity);
        if (boardEntity1 != null) {
            // 점수저장
            ActivityScoreEntity activityScoreEntity = new ActivityScoreEntity();
            activityScoreEntity.setType(ActivityScoreEntity.ScoreType.EVENT);
            activityScoreEntity.setUserId(boardEntity.getWriter());
            activityScoreEntity.setScore(Long.parseLong(ActivityScoreConst.EVENT_ACTIVITY_SCORE+""));
            activityScoreRepository.save(activityScoreEntity);
            // 총계
            try {
                userService.updateActivityScore(boardEntity.getWriter(), ActivityScoreConst.EVENT_ACTIVITY_SCORE);
            } catch(Exception e) {
                log.error(e.getClass().toString(), e);
            }
            // 알람 저장
            boardId = id;
            userId = boardEntity.getWriter();
            AlarmHistEntity alarmHistEntity = new AlarmHistEntity();
            alarmHistEntity.setUserId(userId);
            alarmHistEntity.setBoardId(boardId);
            alarmHistEntity.setEventType(AlarmHistEntity.EventType.EVT_BL);
            alarmHistRepository.save(alarmHistEntity);

            this.template.convertAndSend("/socket/sub/board/" + userId, 1);

        }
    }

    public void setDisLikeTransaction(Long id, String userId) {
        BoardEntity boardEntity = boardRepository.findById(id).get();

        Long boardId = id;
        String feelDisLike = "D";
        String feelState = feelDisLike;
        FeelEntity feelEntity = feelRepository.findByUserIdAndBoardIdAndFeelState(userId, boardId, feelState);
        if (feelEntity == null) {
            // insert
            FeelEntity feelEntity1 = new FeelEntity();
            feelEntity1.setBoardId(id);
            feelEntity1.setCount(1);
            feelEntity1.setUserId(userId);
            feelEntity.setFeelState(feelDisLike);
            feelRepository.save(feelEntity1);
        } else {
            // delete
            feelRepository.delete(feelEntity);
        }
        Long disLikeTotalSumCnt = feelRepository.selectTocalCount(feelDisLike, boardId);
        // 좋아요/싫어요 따로 집계
//        boardEntity.setRowLike(decrease(boardEntity.getRowLike())); // 좋아요 감소
        boardEntity.setRowDisLike(disLikeTotalSumCnt); // 싫어요 증가
        BoardEntity boardEntity1 = boardRepository.save(boardEntity);
        if (boardEntity1 != null) {

            // 점수 저장
            ActivityScoreEntity activityScoreEntity = new ActivityScoreEntity();
            activityScoreEntity.setType(ActivityScoreEntity.ScoreType.EVENT);
            activityScoreEntity.setUserId(boardEntity.getWriter());
            activityScoreEntity.setScore(Long.parseLong(ActivityScoreConst.EVENT_ACTIVITY_SCORE+""));
            activityScoreRepository.save(activityScoreEntity);
            // 총계
            try {
                userService.updateActivityScore(boardEntity.getWriter(), ActivityScoreConst.EVENT_ACTIVITY_SCORE);
            } catch(Exception e) {
                log.error(e.getClass().toString(), e);
            }
            // 알람 저장
            boardId = id;
            userId = boardEntity.getWriter();
            AlarmHistEntity alarmHistEntity = new AlarmHistEntity();
            alarmHistEntity.setUserId(userId);
            alarmHistEntity.setBoardId(boardId);
            alarmHistEntity.setEventType(AlarmHistEntity.EventType.EVT_BD);
            alarmHistRepository.save(alarmHistEntity);

            this.template.convertAndSend("/socket/sub/board/" + userId, 1);

        }
    }

    public Long increase(Long curVal) {
        return curVal + 1;
    }

    public Long decrease(Long currVal) {
        if (currVal < 0) {
            return 0L;
        } else if (currVal == 0) {
            return 0L;
        }   else {
            return currVal - 1;
        }

    }

    /**
     * Async 처리시 오류가 나면 그냥 200을 던지는 오류를 범함.
     * @param boardLikeDTO
     */
    @Transactional
    public void setLikeAndDisLike(BoardLikeDTO boardLikeDTO) {

        if (BoardComponent.isItemGbLike(boardLikeDTO.getItemGb())) { // 좋아요
            setLikeTransaction(boardLikeDTO.getId(), boardLikeDTO.getClickUserId());

        } else if (BoardComponent.isItemGbDisLike(boardLikeDTO.getItemGb())){ // 싫어요
            setDisLikeTransaction(boardLikeDTO.getId(), boardLikeDTO.getClickUserId());

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

        BOARD_TYPE board_type = BOARD_TYPE.FREE;
        // FREE, NOTICE, QNA, JOB_OFFER, JOB_SEARCH, SECRET
        if (boardSearchDTO.getBoardType().equals("FREE")) {
            board_type = BOARD_TYPE.FREE;
        } else if (boardSearchDTO.getBoardType().equals("NOTICE")) {
            board_type = BOARD_TYPE.NOTICE;
        } else if (boardSearchDTO.getBoardType().equals("QNA")) {
            board_type = BOARD_TYPE.QNA;
        } else if (boardSearchDTO.getBoardType().equals("JOB_OFFER")) {
            board_type = BOARD_TYPE.JOB_OFFER;
        } else if (boardSearchDTO.getBoardType().equals("JOB_SEARCH")) {
            board_type = BOARD_TYPE.JOB_SEARCH;
        } else if (boardSearchDTO.getBoardType().equals("SECRET")) {
            board_type = BOARD_TYPE.SECRET;
        }

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
                        qBoardEntity.board_type,
                        ExpressionUtils.as(
                                JPAExpressions.select(count(qCommentEntity.id))
                                        .from(qCommentEntity)
                                        .where(qCommentEntity.boardEntity.id.eq(qBoardEntity.id)),
                                "commentCnt")
                        ))
                .from(qBoardEntity)
                .where(getContains(boardSearchDTO, qBoardEntity)
                        , qBoardEntity.board_type.eq(board_type)) // 검색조건: 게시판 타입 추가
                .orderBy(getDesc(qBoardEntity, boardSearchDTO.getSort())) // 정렬
                .offset(offset)
                .limit(boardSearchDTO.getSize())
                .fetchResults();
//        return new PageImpl(boards.getResults(), PageRequest.of(boardSearchDTO.getCurrentPage(), boardSearchDTO.getSize()
//        , Sort.by("createdDate").descending()),boards.getTotal());

        List list = boards.getResults().stream().map(b -> {
            b.setKey(b.getId());
            b.setBoard_type(b.getBoard_type());
            return b;
        }).collect(Collectors.toList());

        return new PageImpl(list, PageRequest.of(0, boardSearchDTO.getSize()
        , Sort.by("createdDate").descending()),boards.getTotal());
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

    /**
     * 내가 올린글
     * @param userId
     * @return
     */
    public List<BoardDTO> userBoardList(String userId) {

        UserEntity userEntity = userRepository.findByUserId(userId); // 프로필 이미지를 주라고 하는데???

        QBoardEntity qBoardEntity = new QBoardEntity("b");
        List<BoardEntity> boards = query
                .select(qBoardEntity)
                .from(qBoardEntity)
                .where(qBoardEntity.writer.contains(userId))
                .orderBy(qBoardEntity.title.desc())
                .limit(10)
                .fetch();
        return getBoardDTOS(boards.stream());
    }

    /**
     * 내가 스크랩한 글
     * @param userId
     * @return
     */
    public Page<BoardDTO> myScrap(String userId, PageDTO pageDTO) {
        QBoardEntity qBoardEntity = new QBoardEntity("b");
        QScrapEntity qScrapEntity = new QScrapEntity("s");
        QUserEntity qUserEntity = new QUserEntity(("u"));
        int offset = (pageDTO.getCurrentPage() - 1) * pageDTO.getSize();
        List<BoardDTO> boards = query
                .select(Projections.constructor(BoardDTO.class, qBoardEntity, qScrapEntity, qUserEntity))
                .from(qBoardEntity)
                .join(qScrapEntity).on(qScrapEntity.boardId.eq(qBoardEntity.id))
                .join(qUserEntity).on(qUserEntity.userId.eq(qBoardEntity.writer))
                .where(qScrapEntity.userId.contains(userId))
                .offset(offset)
                .limit(pageDTO.getSize())
                .orderBy(qBoardEntity.createdDate.desc())
                .fetch();
        // 테이블 구조를 공개하지 않기 위해 DTO에 담는다.
        return new PageImpl(boards, PageRequest.of(0, pageDTO.getSize()
                , Sort.by("createdDate").descending()),boards.size());
    }

    /**
     * 최근활동
     * @param userId
     * @return
     */
    public List<BoardDTO> recentBoardList(String userId) {
        QBoardEntity qBoardEntity = new QBoardEntity("b");
        QCommentEntity qCommentEntity = new QCommentEntity("c");
        List<BoardDTO> boards = query
                .select(Projections.constructor(BoardDTO.class, qBoardEntity, qCommentEntity))
                .from(qBoardEntity)
                .join(qCommentEntity).on(qCommentEntity.boardEntity.eq(qBoardEntity))
                .where(qBoardEntity.writer.contains(userId))
                .limit(10)
                .fetch();
        return boards;
    }

    public BoardDTO findByIdNoTran(Long id) {
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
    public BoardDTO findById(Long id) {
        Optional<BoardEntity> boardEntity1 = boardRepository.findById(id);
        if (boardEntity1.isEmpty()) {
            throw new BoardDetailNotException();
        }
        String userId = boardEntity1.get().getWriter();
        alarmHistRepository.deleteByUserIdAndEventType(userId, AlarmHistEntity.EventType.EVT_BC);
        alarmHistRepository.deleteByUserIdAndEventType(userId, AlarmHistEntity.EventType.EVT_BL);
        alarmHistRepository.deleteByUserIdAndEventType(userId, AlarmHistEntity.EventType.EVT_BD);

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
        boardDTO.setBoard_type(boardEntity.getBoard_type());

        return boardDTO;
    }

//    @Transactional
    public Long insert(BoardReqWriteDTO boardDTO) {

        BOARD_TYPE board_type = BOARD_TYPE.FREE;
        // FREE, NOTICE, QNA, JOB_OFFER, JOB_SEARCH, SECRET
        if (boardDTO.getBoard_type().equals("FREE")) {
            board_type = BOARD_TYPE.FREE;
        } else if (boardDTO.getBoard_type().equals("NOTICE")) {
            board_type = BOARD_TYPE.NOTICE;
        } else if (boardDTO.getBoard_type().equals("QNA")) {
            board_type = BOARD_TYPE.QNA;
        } else if (boardDTO.getBoard_type().equals("JOB_OFFER")) {
            board_type = BOARD_TYPE.JOB_OFFER;
        } else if (boardDTO.getBoard_type().equals("JOB_SEARCH")) {
            board_type = BOARD_TYPE.JOB_SEARCH;
        } else if (boardDTO.getBoard_type().equals("SECRET")) {
            board_type = BOARD_TYPE.SECRET;
        }

        BoardEntity boardEntity = boardRepository.save(BoardEntity.builder()
                .title(boardDTO.getTitle())
                .contents(boardDTO.getContents())
                .viewCount(0L)
                .rowLike(0L)
                .rowDisLike(0L)
                .board_type(board_type) // 게시판 타입 추가
                .writer(boardDTO.getWriter())
                .build());

        if (boardEntity != null) {
            // 점수 저장
            ActivityScoreEntity activityScoreEntity = new ActivityScoreEntity();
            activityScoreEntity.setType(ActivityScoreEntity.ScoreType.BOARD);
            activityScoreEntity.setUserId(boardDTO.getWriter());
            activityScoreEntity.setScore(Long.parseLong(ActivityScoreConst.BOARD_ACTIVITY_SCORE+""));
            activityScoreRepository.save(activityScoreEntity);

            try {
                userService.updateActivityScore(boardEntity.getWriter(), ActivityScoreConst.BOARD_ACTIVITY_SCORE);
            } catch(Exception e) {
                log.error(e.getClass().toString(), e);
            }

        }

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
