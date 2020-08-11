package com.simple.portal.biz.v1.socket.api;

import com.simple.portal.biz.v1.note.service.NoteService;
import com.simple.portal.biz.v1.socket.service.SocketService;
import com.simple.portal.biz.v1.user.service.UserService;
import com.simple.portal.common.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/v1/api/socket")
public class SocketApi {

    @Autowired
    SocketService socketService;

    @Autowired
    NoteService noteService;
    @Autowired
    ApiResponse apiResponse;

    /**
     * 안읽은 쪽지갯수 (쪽지갯수 리턴)
     * @param UserId
     * @return
     */
    @MessageMapping("/comment/notread")
    @SendTo("/socket/sub/comment/notread")
    public ResponseEntity<ApiResponse> notread(String UserId){
        //apiResponse.setBody(socketService.findNotReadNote(UserId));
        apiResponse.setBody(noteService.findDetail((long)1));
        return new ResponseEntity(apiResponse, HttpStatus.OK);

    }

    @MessageMapping("/board/comment/list")
    @SendTo("/socket/sub/board/comment/list")
    public ResponseEntity<ApiResponse> boardCommentList(String UserId){
        apiResponse.setBody(socketService);
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    @MessageMapping("/user/follow")
    @SendTo("/socket/sub/user/follow")
    public ResponseEntity<ApiResponse> getFollow(String UserId){
        apiResponse.setBody(socketService);
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }
}
