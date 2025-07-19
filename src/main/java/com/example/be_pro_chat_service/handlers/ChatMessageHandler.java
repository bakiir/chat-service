package com.example.be_pro_chat_service.handlers;

import com.example.be_pro_chat_service.dto.CustomChatMessage;
import com.example.be_pro_chat_service.service.ChatMessageService;
import com.example.be_pro_chat_service.session.UserSession;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatMessageHandler extends AbstractWebSocketHandler {

    private final Map<String, UserSession> sessions = new ConcurrentHashMap<>();
    private final Map<String, String> usernameToSessionId =  new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;
    private final ChatMessageService chatMessageService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String username = null;
        String query = session.getUri().getQuery();
        if(query != null){
            String []queryParams = query.split("&");
            for (String param : queryParams){
                String []keyValue =  param.split("=");
                if ("username".equals(keyValue[0]) && keyValue.length > 1) {
                    username = keyValue[1];
                    break;
                }

            }
            if (username!= null){
                sessions.put(session.getId(), new UserSession(session, username));
                usernameToSessionId.put(username, session.getId());
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
        chatMessageService.saveMessage(sender, receiver, message.getContent());
        String sessionId = usernameToSessionId.get(receiver);
        if(sessionId!=null){
            UserSession userSession = sessions.get(sessionId);
            if (userSession!=null){
                try {
                    userSession.getSession().sendMessage(new TextMessage(message.getContent()));
                }catch (Exception e){
                    System.err.println("Error on sending chatMessage: "+ e.getMessage());
                }
            }
        }
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
