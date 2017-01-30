package rs.bg.chat.controller;


import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import rs.bg.chat.config.WebSocketConfig;
import rs.bg.chat.controller.ChatController;
import rs.bg.chat.model.ChatMessage;

public class ChatControllerTests {

	private static final String UNEXISTING_MESSAGE_MAPPING = "/unexisting_message_mapping";

	private TestMessageChannel clientOutboundChannel;

	private TestAnnotationMethodHandler simpleAnnotationMethodHandler;

	ChatMessage messageToSend = new ChatMessage("messageText","username");

	ChatController controller = new ChatController();

	@Before
	public void setup() {

		this.clientOutboundChannel = new TestMessageChannel();

		this.simpleAnnotationMethodHandler = new TestAnnotationMethodHandler(
				new TestMessageChannel(), clientOutboundChannel, new SimpMessagingTemplate(new TestMessageChannel()));

		this.simpleAnnotationMethodHandler.registerHandler(controller);
		this.simpleAnnotationMethodHandler.setDestinationPrefixes(Arrays.asList(WebSocketConfig.APPLICATION_DESTINATION_PREFIX));
		this.simpleAnnotationMethodHandler.setMessageConverter(new MappingJackson2MessageConverter());
		this.simpleAnnotationMethodHandler.setApplicationContext(new StaticApplicationContext());
		this.simpleAnnotationMethodHandler.afterPropertiesSet();
	}


	@Test
	public void shouldReceiveTheSameMessageWhichWasSentToCorrectTopic() throws Exception {

		String destination = WebSocketConfig.APPLICATION_DESTINATION_PREFIX+ChatController.MESSAGE_MAPPING;
				
		sendMessageTo(destination);

		assertEquals(1, this.clientOutboundChannel.getMessages().size());
		
		Message<?> reply = this.clientOutboundChannel.getMessages().get(0);

		StompHeaderAccessor replyHeaders = StompHeaderAccessor.wrap(reply);
		assertEquals(ChatController.TOPIC_CHAT, replyHeaders.getDestination());

		ChatMessage receivedMessage = (ChatMessage)reply.getPayload();
		assertEquals(messageToSend.getContent(), receivedMessage.getContent());
		assertEquals(messageToSend.getChatAccount(), receivedMessage.getChatAccount());
				
	}


	@Test
	public void shouldNotReceiveMessageSentToUnexistingMapping() throws Exception {

		String destination = WebSocketConfig.APPLICATION_DESTINATION_PREFIX+UNEXISTING_MESSAGE_MAPPING;
		
		sendMessageTo(destination);

		assertEquals(0, this.clientOutboundChannel.getMessages().size());
	}
	
	@After
	public void after(){
		clientOutboundChannel.getMessages().clear();
	}


	private void sendMessageTo(String destination) throws JsonProcessingException {
		
		StompHeaderAccessor headers = StompHeaderAccessor.create(StompCommand.SUBSCRIBE);
		headers.setDestination(destination);
		headers.setSessionId("0");
		headers.setSessionAttributes(new HashMap<String, Object>());


		byte[] payload = new ObjectMapper().writeValueAsBytes(messageToSend);
		Message<byte[]> message = MessageBuilder.withPayload(payload).setHeaders(headers).build();

		this.simpleAnnotationMethodHandler.handleMessage(message);
	}

}
