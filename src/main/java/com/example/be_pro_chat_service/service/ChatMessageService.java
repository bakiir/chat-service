package com.example.be_pro_chat_service.service;

import com.example.be_pro_chat_service.model.ChatMessage;
import com.example.be_pro_chat_service.repo.ChatMessageRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepo chatMessageRepo;

    public void saveMessage(String sender, String content, Boolean isGroup, Long groupId, String receiver){
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender(sender);
        chatMessage.setTimestamp(LocalDateTime.now());
        chatMessage.setContent(content);

        if (isGroup){
            chatMessage.setGroupId(groupId);
            chatMessage.setIsGroup(true);
        }else {
            chatMessage.setIsGroup(false);
            chatMessage.setReceiver(receiver);
        };

        chatMessageRepo.save(chatMessage);
    }
}
