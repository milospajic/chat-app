package rs.bg.chat.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import rs.bg.chat.model.ChatMessage;

@Controller
public class ChatController {	
	
    @MessageMapping("/chat")
    @SendTo("/topic/chat")
    public ChatMessage receiveAndSendMessage(ChatMessage message) throws Exception {
        return new ChatMessage(message.getContent());
    }

}
