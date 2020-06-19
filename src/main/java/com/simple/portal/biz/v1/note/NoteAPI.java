package com.simple.portal.biz.v1.note;

import com.simple.portal.common.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/api")
public class NoteAPI {

    @Autowired
    NoteService noteService;

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
     * 보낸쪽지함 (제목,내용,시간,글쓴이(나))
     * @param userId
     * @return
     */
    @GetMapping("/note/send")
    public ResponseEntity<ApiResponse> sendNote(String userId) {
        ApiResponse apiResponse = getApiResponse();
        apiResponse.setBody(noteService.findAll(userId));
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    /**
     * 받은쪽지함 (제목,내용,글쓴이,시간(몇초전,몇분전,몇시간전...트위터처럼 표현))
     * @param userId
     * @return
     */
    @GetMapping("/note/receive")
    public ResponseEntity<ApiResponse> receiveNote(String userId) {
        ApiResponse apiResponse = getApiResponse();
        apiResponse.setBody(noteService.findAll(userId));
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    /**
     * 쪽지 상세보기
     * @param id
     * @return
     */
    @GetMapping("/note/{id}")
    public ResponseEntity<ApiResponse> noteDetail(@PathVariable Long id) {
        ApiResponse apiResponse = getApiResponse();
        apiResponse.setBody(noteService.findDetail(id));
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    /**
     * 쪽지쓰기
      * @param noteDTO
     * @return
     */
    @PostMapping("/note")
    public ResponseEntity<ApiResponse>  notePost(@Valid @RequestBody NoteDTO noteDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new RuntimeException(bindingResult.getAllErrors().toString());
        }
        noteService.save(noteDTO);
        ApiResponse apiResponse = getApiResponse();
        apiResponse.setBody("");
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    /**
     * 쪽지삭제
     * @param noteDTO
     * @param bindingResult
     * @return
     */
    @DeleteMapping("/note")
    public ResponseEntity<ApiResponse>  delPost(@Valid @RequestBody NoteDTO noteDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new RuntimeException(bindingResult.getAllErrors().toString());
        }

        noteService.delete(noteDTO);
        ApiResponse apiResponse = getApiResponse();
        apiResponse.setBody("");
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    /**
     * 쪽지수정
     * @param noteDTO
     * @param bindingResult
     * @return
     */
    @PutMapping("/note")
    public ResponseEntity<ApiResponse>  editPost(@Valid @RequestBody NoteDTO noteDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new RuntimeException(bindingResult.getAllErrors().toString());
        }

        noteService.update(noteDTO);
        ApiResponse apiResponse = getApiResponse();
        apiResponse.setBody("");
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }
}
