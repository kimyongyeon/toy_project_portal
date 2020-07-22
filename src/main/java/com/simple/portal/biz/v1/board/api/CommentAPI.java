package com.simple.portal.biz.v1.board.api;

import com.simple.portal.biz.v1.board.BoardConst;
import com.simple.portal.biz.v1.board.dto.CommentDTO;
import com.simple.portal.biz.v1.board.dto.CommentLikeDTO;
import com.simple.portal.biz.v1.board.entity.BoardEntity;
import com.simple.portal.biz.v1.board.entity.CommentEntity;
import com.simple.portal.biz.v1.board.exception.InputRequiredException;
import com.simple.portal.biz.v1.board.service.BoardComponent;
import com.simple.portal.biz.v1.board.service.CommentService;
import com.simple.portal.common.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/api/comment")
public class CommentAPI {

    @Autowired
    CommentService commentService;

    @Autowired
    ApiResponse apiResponse;

    /**
     * 댓글목록 출력
     * @return
     */
    @GetMapping("/")
    public ResponseEntity<ApiResponse> list() {
        apiResponse.setBody(commentService.listComment());
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    /**
     * 댓글 입력
     * @param commentDTO
     * @param bindingResult
     * @return
     */
    @PostMapping("/")
    public ResponseEntity<ApiResponse> comment (@Valid @RequestBody CommentDTO commentDTO, BindingResult bindingResult) {

        isBinding(bindingResult);

        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setId(commentDTO.getBoardId());
        commentService.writeComment(CommentEntity.builder()
                .id(commentDTO.getId())
                .title(commentDTO.getContent())
                .contents(commentDTO.getTitle())
                .writer(commentDTO.getWriter())
                .boardEntity(boardEntity)
                .build());

        apiResponse.setBody(BoardConst.BODY_BLANK);
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    /**
     * 댓글 수정
     * @param commentDTO
     * @param bindingResult
     * @return
     */
    @PutMapping("/")
    public ResponseEntity<ApiResponse> commentUpdate (@Valid @RequestBody CommentDTO commentDTO, BindingResult bindingResult) {

        isBinding(bindingResult);

        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setId(commentDTO.getBoardId());
        commentService.updateComment(CommentEntity.builder()
                .id(commentDTO.getId())
                .title(commentDTO.getContent())
                .contents(commentDTO.getTitle())
                .build());

        apiResponse.setBody(BoardConst.BODY_BLANK);
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    /**
     * 댓글 좋아요:싫어요
     * @param click
     * @param commentLikeDTO
     * @param bindingResult
     * @return
     */
    @PostMapping("/event/{click}")
    public ResponseEntity<ApiResponse> rowClickItem(@PathVariable String click, @Valid @RequestBody CommentLikeDTO commentLikeDTO, BindingResult bindingResult) {

        isBinding(bindingResult);

        if (BoardComponent.isEventPath(click)) {
            commentService.setLikeAndDisLike(commentLikeDTO);
        } else {
            apiResponse.setBody(BoardConst.FAIL_PATH);
        }

        apiResponse.setBody(BoardConst.BODY_BLANK);
        return new ResponseEntity(apiResponse, HttpStatus.OK);

    }

    private void isBinding(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InputRequiredException();
        }
    }
}
