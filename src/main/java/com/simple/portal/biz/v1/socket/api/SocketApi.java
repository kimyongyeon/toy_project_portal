
package com.simple.portal.biz.v1.socket.api;

import com.simple.portal.biz.v1.note.service.NoteService;
import com.simple.portal.biz.v1.socket.service.SocketService;
import com.simple.portal.common.ApiResponse;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/v1/api/socket")
public class SocketApi {

    @Autowired
    SocketService socketService;

    @Autowired
    ApiResponse apiResponse;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

 /*   //SentTo로 Message 보내는 방법(Convert가능)
    @MessageMapping("/note/notread/{userId}")
    public void simplePub(@DestinationVariable String userId) {
        simpMessagingTemplate.convertAndSend("/socket/sub/comment/notread/"+userId,"");
    }*/


/**
     * 안읽은 쪽지갯수 - 구독한 전체 Socket 다( 전체 쪽지일 경우 사용)
     * @param userId
     * @return
     */

    @MessageMapping("/comment/notread")
    @SendTo("/socket/sub/comment/notread")
    public ResponseEntity<ApiResponse> allNotRead(@RequestParam String userId){

        apiResponse.setBody(socketService.findNotReadNote(userId));
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }


/**
     * 안읽은 쪽지갯수-개인 (쪽지갯수 리턴)
     * @DestinationVariable userId
     * @return
     */

    @MessageMapping("/comment/notread/{userId}")
    @SendTo("/socket/sub/comment/notread/{userId}")
    public ResponseEntity<ApiResponse> noteNotRead(@DestinationVariable  String userId){
        apiResponse.setBody(socketService.findNotReadNote(userId));
        return new ResponseEntity(apiResponse, HttpStatus.OK);

    }

    @MessageMapping("/board/comment/list/{userId}")
    @SendTo("/socket/sub/board/comment/list/{userId}")
    public ResponseEntity<ApiResponse> boardCommentList(@DestinationVariable String userId){
        apiResponse.setBody(socketService.findNotReadComment(userId));
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }

    @MessageMapping("/user/follow/{userId}")
    @SendTo("/socket/sub/user/follow/{userId}")
    public ResponseEntity<ApiResponse> followList(@DestinationVariable String UserId){
        apiResponse.setBody(socketService.findNotConfirmFollowing(UserId));
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }
}

