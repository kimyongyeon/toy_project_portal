package com.simple.portal.biz.v1.board.api;

import com.simple.portal.biz.v1.board.BoardConst;
import com.simple.portal.biz.v1.board.dto.*;
import com.simple.portal.biz.v1.board.exception.InputRequiredException;
import com.simple.portal.biz.v1.board.service.BoardComponent;
import com.simple.portal.biz.v1.board.service.BoardService;
import com.simple.portal.common.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/api/board")
@Api(tags = "BoardAPI", description = "게시판 API")
@Slf4j
public class BoardAPI {

    @Autowired
    BoardService boardService;

    @Autowired
    ApiResponse apiResponse;

    /**
     * 최근활동 조회 - 게시판, 댓글
     * @param userId
     * @return
     */
    @GetMapping("/recent/{userId}")
    @ApiOperation(value="최근활동 글 조회")
    public ResponseEntity<ApiResponse> recentBoardList(@PathVariable String userId) {
        apiResponse.setBody(boardService.recentBoardList(userId));
        apiResponse.setMsg(BoardConst.SUCCESS_MSG);
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    /**
     * 내가 올린 게시물 - 게시판만
     * @param userId
     * @return
     */
    @GetMapping("/userid/{userId}")
    @ApiOperation(value="내가 올린 글 조회")
    public ResponseEntity<ApiResponse> userBoardList(@PathVariable String userId) {
        apiResponse.setBody(boardService.userBoardList(userId));
        apiResponse.setMsg(BoardConst.SUCCESS_MSG);
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    /**
     * 스크랩
     * @param userId
     * @return
     */
    @GetMapping("/scrap/{userId}")
    @ApiOperation(value="내가 스크랩한 글 조회")
    public ResponseEntity<ApiResponse> query(@PathVariable String userId) {
        apiResponse.setBody(boardService.myScrap(userId));
        apiResponse.setMsg(BoardConst.SUCCESS_MSG);
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    /**
     * 게시판 페이징 / 검색
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value="게시글 페이징/검색/정렬")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "키워드", required = false, dataType = "string", paramType = "query", defaultValue = ""),
            @ApiImplicitParam(name = "gb", value = "조회구분[title(제목)/contents(내용)/writer(글쓴이)]", required = true, dataType = "string", paramType = "query", defaultValue = "title"),
            @ApiImplicitParam(name = "sort", value = "정렬[date(최신순)/like(좋아요)/viewCount(조회수)/commentCnt(댓글순)]", required = true, dataType = "string", paramType = "query", defaultValue = "like"),
            @ApiImplicitParam(name = "currentPage", value = "페이지번호, default: 1", required = true, dataType = "string", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "size", value = "한페이지당 20개씩 출력, default: 20", required = true, dataType = "string", paramType = "query", defaultValue = "20"),
            @ApiImplicitParam(name = "boardType", value = "FREE, NOTICE, QNA, JOB_OFFER, JOB_SEARCH, SECRET", required = true, dataType = "string", paramType = "query", defaultValue = "FREE"),
    })
    public ResponseEntity<ApiResponse> page(BoardSearchDTO boardSearchDTO) {
        apiResponse.setBody(boardService.pageList(boardSearchDTO));
        apiResponse.setMsg(BoardConst.SUCCESS_MSG);
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/main")
    @ApiOperation(value="공지사항")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "boardType", value = "FREE, NOTICE, QNA, JOB_OFFER, JOB_SEARCH, SECRET", required = true, dataType = "string", paramType = "query", defaultValue = "FREE"),
    })
    public ResponseEntity<ApiResponse> main(BoardSearchDTO boardSearchDTO) {
        boardSearchDTO.setCurrentPage(1);
        boardSearchDTO.setSize(5);
        boardSearchDTO.setSort("date"); // 최신순
        boardSearchDTO.setKeyword("");
        boardSearchDTO.setGb("title");
        apiResponse.setBody(boardService.pageList(boardSearchDTO));
        apiResponse.setMsg(BoardConst.SUCCESS_MSG);
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }



    /**
     * 게시글 상세
     * @return
     */
    @GetMapping("/detail/{id}")
    @ApiOperation(value="게시글 상세")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "상세글기본키", required = true, dataType = "string", paramType = "path", defaultValue = ""),
    })
    public ResponseEntity<ApiResponse> findOne(@PathVariable String id) {
        apiResponse.setBody(boardService.findById(Long.parseLong(id)));
        apiResponse.setMsg(BoardConst.SUCCESS_MSG);
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    /**
     * 게시글 등록
     * @param boardDTO
     * @return
     */
    @PostMapping("/write")
    @ApiOperation(value="게시글 등록")
    public ResponseEntity<ApiResponse> reg(@RequestBody @Valid BoardReqWriteDTO boardDTO, BindingResult bindingResult) {

        isBinding(bindingResult);

        Long id = boardService.insert(boardDTO);
        apiResponse.setBody(boardService.findById(id));
        apiResponse.setMsg(BoardConst.SUCCESS_MSG);
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    /**
     * 게시글 수정
     * @param boardDTO
     * @return
     */
    @PostMapping("/edit")
    @ApiOperation(value="게시글 수정")
    public ResponseEntity<ApiResponse> edit(@RequestBody @Valid BoardReqUpdateDTO boardDTO, BindingResult bindingResult) {

        isBinding(bindingResult);

        boardService.updateTitleOrContents(boardDTO);
        apiResponse.setBody(BoardConst.BODY_BLANK);
        apiResponse.setMsg(BoardConst.SUCCESS_MSG);
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    /**
     * 게시글 단건삭제
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation(value="게시글 단건삭제")
    public ResponseEntity<ApiResponse> remove(@Valid @RequestBody BoardIdDTO boardIdDTO, BindingResult bindingResult) {
        isBinding(bindingResult);
        boardService.idDelete(boardIdDTO.getId());
        apiResponse.setBody(BoardConst.BODY_BLANK);
        apiResponse.setMsg(BoardConst.SUCCESS_MSG);
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    /**
     * 게시글 멀티삭제
     * @return
     */
    @PostMapping("/bulk/delete")
    @ApiOperation(value="게시글 멀티삭제")
    public ResponseEntity<ApiResponse> bulkRemove(@Valid @RequestBody List<BoardIdDTO> boardIdDTOS, BindingResult bindingResult) {

        isBinding(bindingResult);

        for(BoardIdDTO boardIdDTO: boardIdDTOS) {
            boardService.idDelete(boardIdDTO.getId());
        }
        apiResponse.setBody(BoardConst.BODY_BLANK);
        apiResponse.setMsg(BoardConst.SUCCESS_MSG);
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    /**
     * 좋아요/싫어요
     * @param boardLikeDTO
     * @param bindingResult
     * @return
     */
    @PostMapping("/event/like")
    @ApiOperation(value="게시글 좋아요/싫어요 이벤트")
    public ResponseEntity<ApiResponse> rowClickItem(@Valid @RequestBody BoardLikeDTO boardLikeDTO, BindingResult bindingResult) {

        isBinding(bindingResult);
        boardService.setLikeAndDisLike(boardLikeDTO);
        apiResponse.setBody(boardService.findByIdNoTran(boardLikeDTO.getId()));
        apiResponse.setMsg(BoardConst.SUCCESS_MSG);
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    private void isBinding(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InputRequiredException();
        }
    }

    /**
     * 게시글 스크랩
     * @param scrapDTO
     * @return
     */
    @PostMapping("/scrap")
    @ApiOperation(value="게시글 스크랩")
    public ResponseEntity<ApiResponse> scrap(@RequestBody @Valid ScrapDTO scrapDTO, BindingResult bindingResult) {

        isBinding(bindingResult);

        apiResponse.setBody(boardService.saveScrap(scrapDTO));
        apiResponse.setMsg(BoardConst.SUCCESS_MSG);
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    /**
     * 게시글 스크랩 해제
     * @param scrapDTO
     * @return
     */
    @PostMapping("/scrap/delete")
    @ApiOperation(value="게시글 스크랩 해제")
    public ResponseEntity<ApiResponse> scrapDelete(@RequestBody @Valid ScrapDTO scrapDTO, BindingResult bindingResult) {

        isBinding(bindingResult);

        boardService.removeScrap(scrapDTO);
        apiResponse.setBody(BoardConst.BODY_BLANK);
        apiResponse.setMsg(BoardConst.SUCCESS_MSG);
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

}
