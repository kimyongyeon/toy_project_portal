package com.simple.portal.common.socket;

import com.simple.portal.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@Slf4j
public class SocketAPI {

    public static Set<SocketDTO> sockets = new CopyOnWriteArraySet<>();
    private static int onlineCount = 0;

    @Autowired
    private SimpMessagingTemplate template;

    @RequestMapping(path="/greetings/{path}", method=GET) // path = greetings
    public void greet(String greeting, @PathVariable String path) {
        this.template.convertAndSend("/topic/" + path, greeting);
    }

    @MessageMapping("/fleet/{fleetId}/driver/{driverId}")
    public void simplePub(@DestinationVariable String fleetId, @DestinationVariable String driverId) {
        template.convertAndSend("/topic/fleet/" + fleetId, new SocketDTO(fleetId, driverId));
    }

    @SubscribeMapping("/fleet/{fleetId}/driver/{driverId}")
    public SocketDTO simpleSub(@DestinationVariable String fleetId, @DestinationVariable String driverId) {
        return new SocketDTO(fleetId, driverId);
    }

    @MessageMapping("/userName/{userNameId}/content/{contentId}")
    @SendTo("/topic/userName/{userNameId}")
    public SocketDTO simplePubSub(@DestinationVariable String userNameId, @DestinationVariable String contentId) {
        return new SocketDTO(userNameId, contentId);
    }

    @MessageExceptionHandler
    @SendToUser(destinations="/queue/errors", broadcast=false)
    public ResponseEntity<?> handleException(RuntimeException exception) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(exception.getCause().hashCode() + "");
        apiResponse.setBody(exception.getMessage());
        apiResponse.setMsg("");
        return new ResponseEntity(apiResponse, HttpStatus.BAD_REQUEST);
    }

    // @RequestMapping == @MessageMapping
    // @SendTo 목적지 구별
    // return시 STOMP에서 현재 구독(subscribe)중인 주소에 맵핑(@SendTo)하여 전송
    // 중간 중계과정을 하게 되므로 중요하고 올바르게 주소와 목적지를 설정

    @MessageMapping("/connect") //설정한 prefix를 포함한 /app/hello이다.//
    @SendTo("/topic/connect") //전달할려는 곳의 subscribe//
    public SocketDTO connect(SocketDTO socketDTO) {

        onlineCount++;
        sockets.add(socketDTO);

        String userName = socketDTO.getUserName();
        String content = socketDTO.getContent();
        SocketDTO result = new SocketDTO(userName, content);

        return result;
    }

    @MessageMapping("/userNameInsert") //설정한 prefix를 포함한 /app/userNameInsert.//
    @SendTo("/topic/userNameInsert") //전달할려는 곳의 subscribe//
    public SocketDTO userNameInsert(SocketDTO socketDTO) {
        String userName = socketDTO.getUserName();
        String content = socketDTO.getContent();
        SocketDTO result = new SocketDTO(userName, content);
        return result;
    }

    // 각각의 메세지 유형에 따라 Mapping을 추가해 줄 수 있다.//
    @MessageMapping("/out")
    @SendTo("/topic/out")
    public SocketDTO outroom(SocketDTO socketDTO) {

        onlineCount--;
        sockets.remove(socketDTO);

        String userName = socketDTO.getUserName();
        String content = socketDTO.getContent();
        SocketDTO result = new SocketDTO(userName, content);
        return result;
    }

    @MessageMapping("/in")
    @SendTo("/topic/in")
    public SocketDTO inRoom(SocketDTO socketDTO) {
        String userName = socketDTO.getUserName();
        String content = socketDTO.getContent();
        SocketDTO result = new SocketDTO(userName, content);
        return result;
    }


}
