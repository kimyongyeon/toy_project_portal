package com.simple.portal.biz.v1.note;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NoteAPI {

    // todo: 보낸쪽지함 (제목,내용,시간,글쓴이(나))

    // todo: 받은쪽지함 (제목,내용,글쓴이,시간(몇초전,몇분전,몇시간전...트위터처럼 표현))
    @GetMapping("/note")
    public ResponseEntity note() {
        return ResponseEntity.ok().body("list note");
    }

    // todo: 쪽지입력
    @PostMapping("/note")
    public ResponseEntity notePost() {
        return ResponseEntity.ok().body("post note");
    }
}
