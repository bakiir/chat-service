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

    public void saveMessage(String sender, String receiver, String content){
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender(sender);
        chatMessage.setReceiver(receiver);
        chatMessage.setTimestamp(LocalDateTime.now());
        chatMessage.setContent(content);
        chatMessageRepo.save(chatMessage);
    }
}
