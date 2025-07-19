package com.example.be_pro_chat_service.handlers;

import com.example.be_pro_chat_service.dto.GroupMessage;
import com.example.be_pro_chat_service.model.Group;
import com.example.be_pro_chat_service.service.ChatMessageService;
import com.example.be_pro_chat_service.service.GroupService;
import com.example.be_pro_chat_service.session.UserSession;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
@RequiredArgsConstructor
public class GroupMessageHandler extends AbstractWebSocketHandler {
    private final Map<String, UserSession> sessions = new ConcurrentHashMap<>();
    private final GroupService groupService;
    private final ChatMessageService chatMessageService;
    private final ObjectMapper objectMapper;

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
            if (username != null) {
                sessions.put(session.getId(), new UserSession(session, username));
                joinGroup(session, username, 1L);
            }
        }
    }


    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        GroupMessage groupMessage = objectMapper.readValue(message.getPayload().toString(), GroupMessage.class);
        String username = sessions.get(session.getId()).getUsername();

        switch (groupMessage.getType()){
            case "JOIN_GROUP" -> joinGroup(session, username, groupMessage.getGroup());
            case "GROUP_MESSAGE" -> sendToGroup(session, groupMessage.getGroup(), username + ": " + groupMessage.getContent());
        }
    }

    private void sendToGroup(WebSocketSession senderSession, Long groupId, String message) {
        String sender = sessions.get(senderSession.getId()).getUsername();
        Group group = groupService.getGroupByIdWithUsers(groupId);

        // Сохраняем в БД
        chatMessageService.saveMessage(sender, message, true, groupId, null);

        List<String> users = group.getUsers();
        for (UserSession userSession : sessions.values()) {
            if (users.contains(userSession.getUsername())) {
                try {
                    if (userSession.getSession().isOpen()) {
                        userSession.getSession().sendMessage(new TextMessage(message));
                    } else {
                        log.warn("Cannot send to {}: session closed", userSession.getUsername());
                    }
                } catch (IOException e) {
                    log.warn("Failed to send message to {}", userSession.getUsername(), e);
                }
            }
        }
    }

    private void joinGroup(WebSocketSession senderSession,String username, Long groupId){
        Group group = groupService.getGroupByIdWithUsers(groupId);

        groupService.addUserToGroup(group.getId(), username);
        String sender = sessions.get(senderSession.getId()).getUsername();

        String message = ("User: " + sender + " joined to the group");
        // Сохраняем в БД
        chatMessageService.saveMessage(sender, message, true, groupId, null);

        List<String> users = group.getUsers();
        for (UserSession userSession : sessions.values()) {
            if (users.contains(userSession.getUsername())) {
                try {
                    if (userSession.getSession().isOpen()) {
                        userSession.getSession().sendMessage(new TextMessage(message));
                    } else {
                        log.warn("Cannot send to {}: session closed", userSession.getUsername());
                    }
                } catch (IOException e) {
                    log.warn("Failed to send message to {}", userSession.getUsername(), e);
                }
            }
        }

        log.info("user {} joined to group {}", username, group);
    }


}
