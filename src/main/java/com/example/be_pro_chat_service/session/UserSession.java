package com.example.be_pro_chat_service.session;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserSession {
    private WebSocketSession session;
    private String username;
}
