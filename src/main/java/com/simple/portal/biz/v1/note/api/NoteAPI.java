package com.simple.portal.biz.v1.note.api;

import com.simple.portal.biz.v1.board.BoardConst;
import com.simple.portal.biz.v1.note.NoteConst;
import com.simple.portal.biz.v1.note.dto.NoteDTO;
import com.simple.portal.biz.v1.note.dto.NoteListDTO;
import com.simple.portal.biz.v1.note.dto.NoteRemoveDTO;
import com.simple.portal.biz.v1.note.dto.NoteSaveDTO;
import com.simple.portal.biz.v1.note.exception.InputRequiredException;
import com.simple.portal.biz.v1.note.service.NoteService;
import com.simple.portal.common.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/api/note")
@Api(tags = "NoteAPI", description = "쪽지 API")
public class NoteAPI {

    @Autowired
    NoteService noteService;

    @Autowired
    ApiResponse apiResponse;

    /**
     * 보낸쪽지함 (제목,내용,시간,글쓴이(나))
     * @return
     */
    @GetMapping("/send")
    @ApiOperation(value="쪽지 보낸쪽지함")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "보낸아이디", required = true, dataType = "string", paramType = "query", defaultValue = ""),
            @ApiImplicitParam(name = "gb", value = "R:받는사람,S:보낸사람", required = true, dataType = "string", paramType = "query", defaultValue = "title"),
            @ApiImplicitParam(name = "currentPage", value = "페이지번호, default: 1", required = true, dataType = "string", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "size", value = "한페이지당 20개씩 출력, default: 20", required = true, dataType = "string", paramType = "query", defaultValue = "20"),
    })
    public ResponseEntity<ApiResponse> sendNote(NoteListDTO noteListDTO) {
        apiResponse.setBody(noteService.findAll(noteListDTO));
        apiResponse.setMsg(BoardConst.SUCCESS_MSG);
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    /**
     * 받은쪽지함 (제목,내용,글쓴이,시간(몇초전,몇분전,몇시간전...트위터처럼 표현))
     * @return
     */
    @GetMapping("/receive")
    @ApiOperation(value="쪽지 받은쪽지함")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "받는아이디", required = true, dataType = "string", paramType = "query", defaultValue = ""),
            @ApiImplicitParam(name = "gb", value = "R:받는사람,S:보낸사람", required = true, dataType = "string", paramType = "query", defaultValue = "title"),
            @ApiImplicitParam(name = "currentPage", value = "페이지번호, default: 1", required = true, dataType = "string", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "size", value = "한페이지당 20개씩 출력, default: 20", required = true, dataType = "string", paramType = "query", defaultValue = "20"),
    })
    public ResponseEntity<ApiResponse> receiveNote(NoteListDTO noteListDTO) {
        apiResponse.setBody(noteService.findAll(noteListDTO));
        apiResponse.setMsg(BoardConst.SUCCESS_MSG);
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    /**
     * 쪽지 상세보기
     * @param id
     * @return
     */
    @GetMapping("/detail/{gb}/{id}")
    @ApiOperation(value="쪽지 상세보기")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gb", value = "구분(R:받는편지함,S:보낸편지함)", required = true, dataType = "string", paramType = "path", defaultValue = ""),
            @ApiImplicitParam(name = "id", value = "편지함기본키", required = true, dataType = "string", paramType = "path", defaultValue = ""),
    })
    public ResponseEntity<ApiResponse> noteDetail(@PathVariable Long id, @PathVariable String gb) {
        apiResponse.setBody(noteService.findDetail(id, gb));
        apiResponse.setMsg(BoardConst.SUCCESS_MSG);
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    /**
     * 쪽지쓰기
      * @param noteDTO
     * @return
     */
    @PostMapping("/write")
    @ApiOperation(value="쪽지 쓰기")
    public ResponseEntity<ApiResponse> notePost(@Valid @RequestBody NoteSaveDTO noteDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new InputRequiredException();
        }

        Long id = noteService.save(noteDTO);
        apiResponse.setBody(noteService.findDetail(id, "S"));
        apiResponse.setMsg(BoardConst.SUCCESS_MSG);
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    /**
     * 단체 쪽지쓰기
     * @param noteDTOs
     * @return
     */
    @PostMapping("/writes")
    @ApiOperation(value="단체 쪽지 쓰기")
    public ResponseEntity<ApiResponse> newPosts(@Valid @RequestBody List<NoteSaveDTO> noteDTOs, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new InputRequiredException();
        }

        for (NoteSaveDTO noteDTO: noteDTOs) {
            Long id = noteService.save(noteDTO);
            apiResponse.setBody(noteService.findDetail(id, "S"));
        }
        apiResponse.setMsg(BoardConst.SUCCESS_MSG);
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    /**
     * 쪽지삭제
     * @return
     */
    @PostMapping("/remove/{gb}/{id}")
    @ApiOperation(value="쪽지 삭제")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gb", value = "구분(R:받는편지함,S:보낸편지함)", required = true, dataType = "string", paramType = "path", defaultValue = ""),
            @ApiImplicitParam(name = "id", value = "편지함기본키", required = true, dataType = "string", paramType = "path", defaultValue = ""),
    })
    public ResponseEntity<ApiResponse> delPost(@PathVariable Long id, @PathVariable String gb) {
        noteService.delete(id, gb);
        apiResponse.setBody(NoteConst.BODY_BLANK);
        apiResponse.setMsg(BoardConst.SUCCESS_MSG);
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    /**
     * 쪽지 멀티삭제
     * @return
     */
    @PostMapping("/remove/bulk")
    @ApiOperation(value="쪽지 멀티삭제")
    public ResponseEntity<ApiResponse> delPost(@Valid @RequestBody NoteRemoveDTO noteRemoveDTO, BindingResult bindingResult) {

        isBinding(bindingResult);

        for (Long id : noteRemoveDTO.getId()) {
            noteService.delete(id, noteRemoveDTO.getGb());
        }
        apiResponse.setBody(NoteConst.BODY_BLANK);
        apiResponse.setMsg(BoardConst.SUCCESS_MSG);
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    private void isBinding(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new com.simple.portal.biz.v1.board.exception.InputRequiredException();
        }
    }

}
