package rs.bg.chat.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import rs.bg.chat.model.ChatMessage;

@Controller
public class ChatController {	
	
    public static final String MESSAGE_MAPPING = "/chat";
	public static final String TOPIC_CHAT = "/topic/chat";

	@MessageMapping(MESSAGE_MAPPING)
    @SendTo(TOPIC_CHAT)
    public ChatMessage receiveAndSendMessage(ChatMessage message) throws Exception {
    	return new ChatMessage(message.getContent(), message.getChatAccount());
    }

}
