package com.simple.portal.biz.board;

import org.springframework.web.bind.annotation.*;

@RestController
public class CommentAPI {

    // todo: 댓글목록 출력
    @GetMapping("/comment")
    public void list() {

    }

    // todo: 댓글 입력
    @PostMapping("/comment")
    public void comment () {

    }

    // todo: 댓글 좋아요:싫어요
    @PostMapping("/comment/{like}")
    public void like(@PathVariable int like) {

    }
}
