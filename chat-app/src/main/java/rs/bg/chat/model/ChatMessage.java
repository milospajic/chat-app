package rs.bg.chat.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChatMessage {
	
	private String content;
	private String chatAccount;

	public ChatMessage() {
	}

	public ChatMessage(String content, String chatName) {
		this.content=content;
		chatAccount=chatName;
	}

	public String getContent() {
		return content;
	}
	
	@JsonProperty("chat_account") 
	public String getChatAccount() {
		return chatAccount;
	}
}
