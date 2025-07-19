package com.example.be_pro_chat_service.repo;

import com.example.be_pro_chat_service.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepo extends JpaRepository<ChatMessage, Long> {
}
