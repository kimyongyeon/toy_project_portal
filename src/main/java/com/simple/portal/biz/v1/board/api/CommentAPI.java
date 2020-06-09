package com.simple.portal.biz.v1.board.api;

import com.simple.portal.biz.v1.board.dto.CommentDTO;
import com.simple.portal.biz.v1.board.entity.BoardEntity;
import com.simple.portal.biz.v1.board.entity.CommentEntity;
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
@RequestMapping("/v1/api")
public class CommentAPI {

    @Autowired
    CommentService commentService;

    /**
     * Response 공통 처리
     * @return
     */
    private ApiResponse getApiResponse() {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode("200");
        apiResponse.setMsg("success");
        return apiResponse;
    }

    // todo: 댓글목록 출력
    @GetMapping("/comment")
    public ResponseEntity<ApiResponse> list(CommentEntity commentEntity) {
        ApiResponse apiResponse = getApiResponse();
        apiResponse.setBody(commentService.listComment(commentEntity));
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    // todo: 댓글 입력
    @PostMapping("/comment")
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

        ApiResponse apiResponse = getApiResponse();
        apiResponse.setBody("");
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    // todo: 댓글 좋아요:싫어요
    @PostMapping("/comment/event/{click}")
    public ResponseEntity<ApiResponse> rowClickItem(@PathVariable String click, @Valid @RequestBody CommentDTO commentDTO, BindingResult bindingResult) {

        ApiResponse apiResponse = getApiResponse();

        if (bindingResult.hasErrors()) {
            throw new RuntimeException("필수값을 입력 하세요.");
        }

        if ("like".equals(click) || "dislike".equals(click)) {
            CommentEntity commentEntity = commentService.findByIdComment(commentDTO.getId());
            BoardEntity boardEntity = new BoardEntity();
            boardEntity.setId(commentDTO.getBoardId());
            commentEntity.setBoardEntity(boardEntity);

            if ("L".equals(commentDTO.getItemGb())) { // 좋아요
                commentEntity.setRowLike(commentEntity.getRowLike() + 1);
                commentEntity.setRowDisLike(commentEntity.getRowDisLike() - 1);
                commentService.addLike(commentEntity);
            } else if("D".equals(commentDTO.getItemGb())) { // 싫어요
                commentEntity.setRowLike(commentEntity.getRowLike() - 1);
                commentEntity.setRowDisLike(commentEntity.getRowDisLike() + 1);
                commentService.addDislike(commentEntity);
            } else {
                throw new RuntimeException("좋아요와 싫어요 중 하나를 선택하세요.");
            }

        } else {
            apiResponse.setBody("올바른 context path를 입력하시요.");
        }

        apiResponse.setBody("");
        return new ResponseEntity(apiResponse, HttpStatus.OK);

    }
}
