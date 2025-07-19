package com.example.be_pro_chat_service.config;

import com.example.be_pro_chat_service.handlers.ChatMessageHandler;
import com.example.be_pro_chat_service.handlers.GroupMessageHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSecketConfig implements WebSocketConfigurer {

    private final ChatMessageHandler chatMessageHandler;
    private final GroupMessageHandler groupMessageHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatMessageHandler, "/private-chat");
        registry.addHandler(groupMessageHandler, "/group-chat");
    }
}
