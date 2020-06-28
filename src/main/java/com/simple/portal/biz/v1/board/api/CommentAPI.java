package com.simple.portal.biz.v1.board.api;

import com.simple.portal.biz.v1.board.BoardConst;
import com.simple.portal.biz.v1.board.dto.CommentDTO;
import com.simple.portal.biz.v1.board.entity.BoardEntity;
import com.simple.portal.biz.v1.board.entity.CommentEntity;
import com.simple.portal.biz.v1.board.exception.InputRequiredException;
import com.simple.portal.biz.v1.board.exception.ItemGubunExecption;
import com.simple.portal.biz.v1.board.service.CommentService;
import com.simple.portal.common.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

        if (bindingResult.hasErrors()) {
            throw new RuntimeException("필수값을 입력 하세요.");
        }

        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setId(commentDTO.getBoardId());
        commentService.writeComment(CommentEntity.builder()
                .id(commentDTO.getId())
                .title(commentDTO.getContent())
                .contents(commentDTO.getTitle())
                .boardEntity(boardEntity)
                .build());

        apiResponse.setBody("");
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    /**
     * 댓글 좋아요:싫어요
     * @param click
     * @param commentDTO
     * @param bindingResult
     * @return
     */
    @PostMapping("/event/{click}")
    public ResponseEntity<ApiResponse> rowClickItem(@PathVariable String click, @Valid @RequestBody CommentDTO commentDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new InputRequiredException();
        }

        if (BoardConst.isEventPath(click)) {
            commentService.setLike(commentDTO);
        } else {
            apiResponse.setBody(BoardConst.FAIL_PATH);
        }

        apiResponse.setBody("");
        return new ResponseEntity(apiResponse, HttpStatus.OK);

    }
}
