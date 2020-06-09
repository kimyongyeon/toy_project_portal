package com.simple.portal.biz.v1.board.api;

import com.simple.portal.biz.v1.board.dto.BoardDTO;
import com.simple.portal.biz.v1.board.entity.BoardEntity;
import com.simple.portal.biz.v1.board.service.BoardService;
import com.simple.portal.common.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/api")
public class BoardAPI {
    @Autowired
    BoardService boardService;

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

    /**
     * 게시글 검색
     * @param boardDTO
     * @return
     */
    @GetMapping("/board/search")
    public ResponseEntity<ApiResponse> search(BoardDTO boardDTO) {
        ApiResponse apiResponse = getApiResponse();
        apiResponse.setBody(boardService.search(boardDTO));
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    /**
     * 게시글 목록
     * @return
     */
    @GetMapping("/board")
    public ResponseEntity<ApiResponse> list() {
        ApiResponse apiResponse = getApiResponse();
        apiResponse.setBody(boardService.list());
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    /**
     * 게시글 상세
     * @param boardDTO
     * @return
     */
    @GetMapping("/board/{seq}")
    public ResponseEntity<ApiResponse> findOne(BoardDTO boardDTO) {
        ApiResponse apiResponse = getApiResponse();
        apiResponse.setBody(boardService.findById(boardDTO.getId()));
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    /**
     * 게시글 등록
     * @param boardDTO
     * @return
     */
    @PostMapping("/board")
    public ResponseEntity<ApiResponse> reg(BoardDTO boardDTO) {
        ApiResponse apiResponse = getApiResponse();
        boardService.insert(boardDTO);
        apiResponse.setBody("");
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    /**
     * 게시글 수정
     * @param boardDTO
     * @return
     */
    @PutMapping("/board")
    public ResponseEntity<ApiResponse> edit(BoardDTO boardDTO) {
        ApiResponse apiResponse = getApiResponse();
        boardService.titleOrContentsUpdate(boardDTO);
        apiResponse.setBody("");
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    /**
     * 게시글 삭제
     * @param boardDTO
     * @return
     */
    @DeleteMapping("/board")
    public ResponseEntity<ApiResponse> remove(BoardDTO boardDTO) {
        ApiResponse apiResponse = getApiResponse();
        boardService.idDelete(boardDTO);
        apiResponse.setBody("");
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    /**
     * 좋아요/싫어요
     * @param boardDTO
     * @param bindingResult
     * @return
     */
    @PostMapping("/board/event/{click}")
    public ResponseEntity<ApiResponse> rowClickItem(@PathVariable String click, @Valid @RequestBody BoardDTO boardDTO, BindingResult bindingResult) {

        ApiResponse apiResponse = getApiResponse();

        if (bindingResult.hasErrors()) {
            throw new RuntimeException("필수값을 입력 하세요.");
        }

        if ("like".equals(click) || "dislike".equals(click)) {
            BoardEntity boardEntity = boardService.findById(boardDTO.getId());
            if ("L".equals(boardDTO.getItemGb())) { // 좋아요
                boardEntity.setRowLike(boardEntity.getRowLike() + 1);
                boardEntity.setRowDisLike(boardEntity.getRowDisLike() - 1);
                boardService.addLike(boardEntity);
            } else if ("D".equals(boardDTO.getItemGb())){ // 싫어요
                boardEntity.setRowLike(boardEntity.getRowLike() - 1);
                boardEntity.setRowDisLike(boardEntity.getRowDisLike() + 1);
                boardService.addDislike(boardEntity);
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
