package com.simple.portal.biz.v1.note.api;

import com.simple.portal.biz.v1.note.NoteConst;
import com.simple.portal.biz.v1.note.dto.NoteDTO;
import com.simple.portal.biz.v1.note.dto.NoteSaveDTO;
import com.simple.portal.biz.v1.note.exception.InputRequiredException;
import com.simple.portal.biz.v1.note.service.NoteService;
import com.simple.portal.common.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/api/note")
public class NoteAPI {

    @Autowired
    NoteService noteService;

    @Autowired
    ApiResponse apiResponse;

    /**
     * 보낸쪽지함 (제목,내용,시간,글쓴이(나))
     * @param userId
     * @return
     */
    @GetMapping("/send")
    public ResponseEntity<ApiResponse> sendNote(String userId) {
        apiResponse.setBody(noteService.findAll(userId, "s"));
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    /**
     * 받은쪽지함 (제목,내용,글쓴이,시간(몇초전,몇분전,몇시간전...트위터처럼 표현))
     * @param userId
     * @return
     */
    @GetMapping("/receive")
    public ResponseEntity<ApiResponse> receiveNote(String userId) {
        apiResponse.setBody(noteService.findAll(userId, "r"));
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    /**
     * 쪽지 상세보기
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> noteDetail(@PathVariable Long id) {
        apiResponse.setBody(noteService.findDetail(id));
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    /**
     * 쪽지쓰기
      * @param noteDTO
     * @return
     */
    @PostMapping("/")
    public ResponseEntity<ApiResponse> notePost(@Valid @RequestBody NoteSaveDTO noteDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new InputRequiredException();
        }

        noteService.save(noteDTO);
        apiResponse.setBody(NoteConst.BODY_BLANK);
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    /**
     * 쪽지삭제
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delPost(@PathVariable Long id) {
        noteService.delete(id);
        apiResponse.setBody(NoteConst.BODY_BLANK);
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    /**
     * 쪽지수정
     * @param noteDTO
     * @param bindingResult
     * @return
     */
    @PutMapping("/")
    public ResponseEntity<ApiResponse> editPost(@Valid @RequestBody NoteSaveDTO noteDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new InputRequiredException();
        }

        noteService.update(noteDTO);
        apiResponse.setBody(NoteConst.BODY_BLANK);
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }
}
