package com.simple.portal.biz.v1.board.api;

import com.simple.portal.biz.v1.board.entity.BoardEntity;
import com.simple.portal.biz.v1.board.service.BoardMemService;
import com.simple.portal.biz.v1.board.service.BoardService;
import com.simple.portal.common.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/api/mem")
public class BoardMemAPI {

    @Autowired
    private BoardMemService boardService;

    private ApiResponse getApiResponse() {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode("200");
        apiResponse.setMsg("success");
        return apiResponse;
    }

    /**
     * 게시판 상세보기
     * @param seq
     * @return
     */
    @GetMapping("/board/{seq}")
    public ResponseEntity<ApiResponse> detail(@PathVariable int seq) {

        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setId(seq);

        ApiResponse apiResponse = getApiResponse();
        apiResponse.setBody(boardService.detail(boardEntity));
        return new ResponseEntity(apiResponse, HttpStatus.OK);

    }

    /**
     * 게시판 목록보기
     * todo: 순번/제목/좋아요수/싫어요수/조회수
     * @return
     */
    @GetMapping("/board")
    public ResponseEntity<ApiResponse> list() {
        ApiResponse apiResponse = getApiResponse();
        apiResponse.setBody(boardService.list());
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    /**
     * 게시판 글쓰기
     * @return
     */
    @PostMapping("/board")
    public ResponseEntity<ApiResponse> write(@Valid @RequestBody BoardEntity boardEntity, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new RuntimeException("필수값 없습니다.");
        }

        boardService.insert(boardEntity);
        ApiResponse apiResponse = getApiResponse();
        apiResponse.setBody("");
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    /**
     * 게시판 수정하기
     * @return
     */
    @PutMapping("/board")
    public ResponseEntity<ApiResponse> edit(@RequestBody BoardEntity boardEntity) {
        boardService.update(boardEntity);
        ApiResponse apiResponse = getApiResponse();
        apiResponse.setBody("");
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    /**
     * 게시판 삭제하기
     * @return
     */
    @DeleteMapping("/board")
    public ResponseEntity<ApiResponse> remove(@RequestBody BoardEntity boardEntity) {
        boardService.delete(boardEntity);
        ApiResponse apiResponse = getApiResponse();
        apiResponse.setBody("");
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    // todo: 좋아요/싫어요
    @PostMapping("/board/event/{like}")
    public ResponseEntity<ApiResponse> like(@PathVariable int like) {
        ApiResponse apiResponse = getApiResponse();
        apiResponse.setBody("");
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }




}
