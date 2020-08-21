package com.simple.portal.biz.v1.socket.conifg;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class SocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stomp").setAllowedOrigins("*").withSockJS();

        /*registry.addEndpoint("/websockethandler").setAllowedOrigins("*").withSockJS(); // 여러가지 End Point 설정//*/
    }
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/socket/sub");
        config.setApplicationDestinationPrefixes("/socket/pub");

        /*config.enableSimpleBroker("/topic"); // 메모리 기반 메세지 브로커가 해당 api 구독하고 있는 클라이언트에게 메세지 전달//
        config.setApplicationDestinationPrefixes("/app"); // 서버에서 클라이언트로부터의 메세지를 받을 api의 prefix//*/
    }
}
