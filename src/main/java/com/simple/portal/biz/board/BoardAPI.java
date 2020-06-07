package com.simple.portal.biz.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
public class BoardAPI {

    @Autowired
    private BoardService boardService;

    /**
     * 게시판 상세보기
     * @param seq
     * @return
     */
    @GetMapping("/board/{seq}")
    public ResponseEntity detail(@PathVariable int seq) {

        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setId(seq);
        return ResponseEntity.ok(boardService.detail(boardEntity));
    }

    /**
     * 게시판 목록보기
     * todo: 순번/제목/좋아요수/싫어요수/조회수
     * @return
     */
    @GetMapping("/board")
    public ResponseEntity list() {
        return ResponseEntity.ok(boardService.list());
    }

    /**
     * 게시판 글쓰기
     * @return
     */
    @PostMapping("/board")
    public ResponseEntity write(@Valid @RequestBody BoardEntity boardEntity, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new RuntimeException("필수값 없습니다.");
        }

        boardService.insert(boardEntity);
        return ResponseEntity.ok("insert ok");
    }

    /**
     * 게시판 수정하기
     * @return
     */
    @PutMapping("/board")
    public ResponseEntity edit(@RequestBody BoardEntity boardEntity) {
        boardService.update(boardEntity);
        return ResponseEntity.ok("edit ok");
    }

    /**
     * 게시판 삭제하기
     * @return
     */
    @DeleteMapping("/board")
    public ResponseEntity remove(@RequestBody BoardEntity boardEntity) {
        boardService.delete(boardEntity);
        return ResponseEntity.ok("remove ok");
    }

    // todo: 좋아요/싫어요
    @PostMapping("/board/evt/{like}")
    public void like(@PathVariable int like) {

    }




}
