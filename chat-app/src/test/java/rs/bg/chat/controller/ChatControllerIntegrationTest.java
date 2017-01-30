package rs.bg.chat.controller;

import java.lang.reflect.Type;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import rs.bg.chat.config.WebSocketConfig;
import rs.bg.chat.model.ChatMessage;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChatControllerIntegrationTest {

    @LocalServerPort
    private int port;

    WebSocketStompClient stompClient;

    ChatMessage messageToSend ;
    
    String websocketUri ;
    
    CountDownLatch latch;
    
    @Before
    public void setup() {
        stompClient = new WebSocketStompClient(new SockJsClient(
                java.util.Arrays.asList(new WebSocketTransport(new StandardWebSocketClient()))));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        websocketUri = "ws://localhost:"+port+WebSocketConfig.WEBSOCKET_ENDPOINT;
        messageToSend = new ChatMessage("MESSAGE TEST","author");
        latch  = new CountDownLatch(1);
    }

    @Test
    public void shouldReceiveAMessageFromCorrectTopic() throws Exception {
    	StompSession session = stompClient
                .connect(websocketUri, new StompSessionHandlerAdapter() {})
                .get(1, TimeUnit.SECONDS);
        DefaultStompFrameHandler handler = new DefaultStompFrameHandler();
        
		session.subscribe(ChatController.TOPIC_CHAT, handler);

        session.send(ChatController.TOPIC_CHAT, messageToSend);

        boolean isMessageReceived = latch.await(3, TimeUnit.SECONDS);
		Assert.assertTrue(isMessageReceived);    }

    @Test
    public void shouldNotReceiveAMesssageFromDifferentTopic() throws Exception {
        StompSession session = stompClient
                .connect(websocketUri, new StompSessionHandlerAdapter() {})
                .get(1, TimeUnit.SECONDS);
    	
        DefaultStompFrameHandler handler = new DefaultStompFrameHandler();        
		session.subscribe(ChatController.TOPIC_CHAT, handler);

		String differentTopic = ChatController.TOPIC_CHAT+"different"; 
        session.send(differentTopic, messageToSend);    
        
        boolean isMessageReceived = latch.await(3, TimeUnit.SECONDS);
        Assert.assertFalse(isMessageReceived);
    }

    @After
    public void after(){
    	latch.countDown();
    }    

    class DefaultStompFrameHandler implements StompFrameHandler {

    	@Override
        public Type getPayloadType(StompHeaders stompHeaders) {
            return ChatMessage.class;
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object payload) {
        	ChatMessage receivedMessage = (ChatMessage)payload;
			Assert.assertEquals(messageToSend.getContent(), receivedMessage.getContent());	
        	latch.countDown();
        }        
    }
    

}
   	 
