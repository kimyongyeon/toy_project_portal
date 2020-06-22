package com.simple.portal.biz.v1.board.api;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.simple.portal.biz.v1.board.BoardConst;
import com.simple.portal.biz.v1.board.dto.BoardDTO;
import com.simple.portal.biz.v1.board.entity.BoardEntity;
import com.simple.portal.biz.v1.board.entity.QBoardEntity;
import com.simple.portal.biz.v1.board.entity.QCommentEntity;
import com.simple.portal.biz.v1.board.exception.ItemGubunExecption;
import com.simple.portal.biz.v1.board.service.BoardService;
import com.simple.portal.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/api/board")
@Slf4j
public class BoardAPI {

    @Autowired
    BoardService boardService;

    @Autowired
    ApiResponse apiResponse;

    /**
     * 최근활동 조회 - 게시판, 댓글
     * @param userId
     * @return
     */
    @GetMapping("/recent/{userId}")
    public ResponseEntity<ApiResponse> recentBoardList(@PathVariable String userId) {
        apiResponse.setBody(boardService.recentBoardList(userId));
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    /**
     * 내가 올린 게시물 - 게시판만
     * @param userId
     * @return
     */
    @GetMapping("/userid/{userId}")
    public ResponseEntity<ApiResponse> userBoardList(@PathVariable String userId) {
        apiResponse.setBody(boardService.userBoardList(userId));
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    /**
     * 스크랩
     * @param userId
     * @return
     */
    @GetMapping("/scrap/{userId}")
    public ResponseEntity<ApiResponse> query(@PathVariable String userId) {
        apiResponse.setBody(boardService.myScrap(userId));
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    /**
     * 게시글 검색
     * @param boardDTO
     * @return
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse> search(BoardDTO boardDTO) {
        apiResponse.setBody(boardService.search(boardDTO));
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    /**
     * 게시판 페이징
     * @param title
     * @param pageable
     * @return
     */
    @GetMapping("/page")
    public ResponseEntity<ApiResponse> page(String title, Pageable pageable) {
        apiResponse.setBody(boardService.pageList(title, pageable));
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    /**
     * 게시글 상세
     * @param boardDTO
     * @return
     */
    @GetMapping("/detail")
    public ResponseEntity<ApiResponse> findOne(BoardDTO boardDTO) {
        apiResponse.setBody(boardService.findById(boardDTO.getId()));
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    /**
     * 게시글 등록
     * @param boardDTO
     * @return
     */
    @PostMapping("/")
    public ResponseEntity<ApiResponse> reg(BoardDTO boardDTO) {
        boardService.insert(boardDTO);
        apiResponse.setBody("");
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    /**
     * 게시글 수정
     * @param boardDTO
     * @return
     */
    @PutMapping("/")
    public ResponseEntity<ApiResponse> edit(BoardDTO boardDTO) {
        boardService.titleOrContentsUpdate(boardDTO);
        apiResponse.setBody("");
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    /**
     * 게시글 단건삭제
     * @param boardDTO
     * @return
     */
    @DeleteMapping("/")
    public ResponseEntity<ApiResponse> remove(BoardDTO boardDTO) {
        boardService.idDelete(boardDTO);
        apiResponse.setBody("");
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    /**
     * 게시글 멀티삭제
     * @param boardDTOList
     * @return
     */
    @DeleteMapping("/bulk/delete")
    public ResponseEntity<ApiResponse> bulkRemove(List<BoardDTO> boardDTOList) {
        for(BoardDTO boardDTO: boardDTOList) {
            boardService.idDelete(boardDTO);
        }
        apiResponse.setBody("");
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    /**
     * 좋아요/싫어요
     * @param boardDTO
     * @param bindingResult
     * @return
     */
    @PostMapping("/event/{click}")
    public ResponseEntity<ApiResponse> rowClickItem(@PathVariable String click, @Valid @RequestBody BoardDTO boardDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new RuntimeException(bindingResult.getAllErrors().toString());
        }

        if (BoardConst.isEventPath(click)) {
            boardService.setLike(boardDTO);
        } else {
            apiResponse.setBody(BoardConst.FAIL_PATH);
        }

        apiResponse.setBody(BoardConst.BODY_BLANK);
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

}
