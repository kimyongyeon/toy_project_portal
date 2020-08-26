package com.simple.portal.biz.v1.socket.conifg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import java.util.Date;


@Configuration
@EnableWebSocketMessageBroker
//@EnableScheduling
public class SocketConfig implements WebSocketMessageBrokerConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(SocketConfig.class);

    private SimpMessagingTemplate messagingTemplate;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stomp").setAllowedOrigins("*").withSockJS();// 여러가지 End Point 설정//*/

    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {

        config.enableSimpleBroker("/socket/sub");// 메모리 기반 메세지 브로커가 해당 api 구독하고 있는 클라이언트에게 메세지 전달//
        config.setApplicationDestinationPrefixes("/socket/pub");// 서버에서 클라이언트로부터의 메세지를 받을 api의 prefix//*/
    }

    //@Scheduled(cron="*/5 * * * * *")     //5초마다 수행하도록 설정
    /*public void checkNotice(){
        logger.info("checkNotice call");
        try{
            System.out.println(123);
            messagingTemplate.setMessageConverter(new StringMessageConverter());
            messagingTemplate.convertAndSend("/socket/sub/comment/notread/khl1342", "");

        }catch(Exception ex){
            logger.error(ex.getMessage());
        }
    }*/

}
