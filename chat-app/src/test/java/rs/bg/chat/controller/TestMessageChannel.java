package rs.bg.chat.controller;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.AbstractSubscribableChannel;

import java.util.ArrayList;
import java.util.List;
/**
 *
 *
 */
class TestMessageChannel extends AbstractSubscribableChannel {

	private static final List<Message<?>> messages = new ArrayList<>();


	public List<Message<?>> getMessages() {
		return messages;
	}

	@Override
	protected boolean sendInternal(Message<?> message, long timeout) {
		messages.add(message);
		return true;
	}

}
