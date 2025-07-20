package com.example.be_pro_chat_service.handlers;

import com.example.be_pro_chat_service.dto.CustomChatMessage;
import com.example.be_pro_chat_service.service.ChatMessageService;
import com.example.be_pro_chat_service.service.OnlineCasheService;
import com.example.be_pro_chat_service.session.UserSession;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatMessageHandler extends AbstractWebSocketHandler {

    private final Map<String, UserSession> sessions = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> usernameToSessionIds = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;
    private final ChatMessageService chatMessageService;
    private final OnlineCasheService onlineCasheService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String username = extractUsername(session);
        if (username!= null){
            sessions.put(session.getId(), new UserSession(session, username));
            usernameToSessionIds.computeIfAbsent(username, k -> ConcurrentHashMap.newKeySet()).add(session.getId());
            onlineCasheService.markOnline(username);
        }

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String sessionId = session.getId();
        UserSession userSession = sessions.remove(sessionId);
        if (userSession != null) {
            String username = userSession.getUsername();
            Set<String> sessionIds = usernameToSessionIds.get(username);
            if (sessionIds != null) {
                sessionIds.remove(sessionId);
                if (sessionIds.isEmpty()) {
                    usernameToSessionIds.remove(username);
                    onlineCasheService.markOffine(username);
                }
            }
        }
    }
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String payload = message.getPayload().toString();
        CustomChatMessage parsedMessage = parseMessage(payload);

        if(parsedMessage!=null){
            handleChatMessage(session, parsedMessage);
        }else {
            System.err.println("User is not found");
        }
    }

    private void handleChatMessage(WebSocketSession session, CustomChatMessage message){

        String sender = sessions.get(session.getId()).getUsername();
        String receiver = message.getReceiver();
        chatMessageService.saveMessage(sender, message.getContent(), false, null, receiver);
        Set<String> sessionIds = usernameToSessionIds.get(receiver);
        if (sessionIds != null) {
            for (String sid : sessionIds) {
                UserSession userSession = sessions.get(sid);
                if (userSession != null) {
                    try {
                        userSession.getSession().sendMessage(
                                new TextMessage(sender + ": " + message.getContent()));
                    } catch (Exception e) {
                        log.error("Error sending message to {}: {}", userSession.getUsername(), e.getMessage());
                    }
                }
            }
        }
    }

    private String extractUsername(WebSocketSession session) {
        String query = session.getUri().getQuery();
        if (query != null) {
            for (String param : query.split("&")) {
                String[] keyValue = param.split("=");
                if ("username".equals(keyValue[0]) && keyValue.length > 1) {
                    return keyValue[1];
                }
            }
        }
        return null;
    }

    private CustomChatMessage parseMessage(String payload){
        try{
            return objectMapper.readValue(payload, CustomChatMessage.class);
        }catch (JsonProcessingException e){
            log.info("Error on parsing chatMesage");
            return null;
        }
    }


}
