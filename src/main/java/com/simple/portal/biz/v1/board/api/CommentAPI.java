package com.simple.portal.biz.v1.board.api;

import com.simple.portal.biz.v1.board.BoardConst;
import com.simple.portal.biz.v1.board.dto.*;
import com.simple.portal.biz.v1.board.entity.BoardEntity;
import com.simple.portal.biz.v1.board.entity.CommentEntity;
import com.simple.portal.biz.v1.board.exception.InputRequiredException;
import com.simple.portal.biz.v1.board.service.BoardComponent;
import com.simple.portal.biz.v1.board.service.CommentService;
import com.simple.portal.common.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/api/comment")
@Api(tags = "CommentAPI", description = "댓글 API")
public class CommentAPI {

    @Autowired
    CommentService commentService;

    @Autowired
    ApiResponse apiResponse;

    /**
     * 댓글목록 출력
     *
     * @return
     */
    @GetMapping("/page/{boardId}")
    @ApiOperation(value = "댓글 조회")
    public ResponseEntity<ApiResponse> list(@PathVariable Long boardId) {
        apiResponse.setBody(commentService.listComment(boardId));
        apiResponse.setMsg(BoardConst.SUCCESS_MSG);
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    /**
     * 댓글 입력
     *
     * @param commentDTO
     * @param bindingResult
     * @return
     */
    @PostMapping("/write")
    @ApiOperation(value = "댓글 쓰기")
    public ResponseEntity<ApiResponse> comment(@Valid @RequestBody CommentWriteDTO commentDTO, BindingResult bindingResult) {

        isBinding(bindingResult);

        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setId(commentDTO.getBoardId());
        CommentEntity commentEntity = commentService.writeComment(CommentEntity.builder()
//                .id(commentDTO.getId())
                .title(commentDTO.getContent())
                .contents(commentDTO.getTitle())
                .writer(commentDTO.getWriter())
                .boardEntity(boardEntity)
                .rowLike(0L)
                .rowDisLike(0L)
                .viewCount(0L)
                .build());
        apiResponse.setBody(commentEntity);
        apiResponse.setMsg(BoardConst.SUCCESS_MSG);
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    /**
     * 댓글 수정
     *
     * @param commentDTO
     * @param bindingResult
     * @return
     */
    @PostMapping("/edit")
    @ApiOperation(value = "댓글 수정")
    public ResponseEntity<ApiResponse> commentUpdate(@Valid @RequestBody CommentEditDTO commentDTO, BindingResult bindingResult) {

        isBinding(bindingResult);
        Long id = commentService.updateComment(CommentEntity.builder()
                .id(commentDTO.getId())
                .title(commentDTO.getContent())
                .contents(commentDTO.getTitle())
                .build());

        apiResponse.setBody(commentService.findById(id));
        apiResponse.setMsg(BoardConst.SUCCESS_MSG);
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    /**
     * 댓글 좋아요:싫어요
     *
     * @param commentLikeDTO
     * @param bindingResult
     * @return
     */
    @PostMapping("/event/like")
    @ApiOperation(value = "댓글 좋아요/싫어요 이벤트")
    public ResponseEntity<ApiResponse> rowClickItem(@Valid @RequestBody CommentLikeDTO commentLikeDTO, BindingResult bindingResult) {

        isBinding(bindingResult);

        commentService.setLikeAndDisLike(commentLikeDTO);
        apiResponse.setBody(commentService.findById(commentLikeDTO.getId()));
        apiResponse.setMsg(BoardConst.SUCCESS_MSG);
        return new ResponseEntity(apiResponse, HttpStatus.OK);

    }

    @PostMapping("/remove")
    @ApiOperation(value = "댓글 삭제")
    public ResponseEntity<ApiResponse> rowRemove(@Valid @RequestBody CommentRemoveDTO commentRemoveDTO, BindingResult bindingResult) {

        isBinding(bindingResult);

        commentService.remove(commentRemoveDTO.getId());
        apiResponse.setBody(BoardConst.BODY_BLANK);
        apiResponse.setMsg(BoardConst.SUCCESS_MSG);
        return new ResponseEntity(apiResponse, HttpStatus.OK);

    }

    private void isBinding(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InputRequiredException();
        }
    }
}
