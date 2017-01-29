(function() {
	'use strict';
	angular.module('chatApp').service('chatService', chatService);
	chatService.$inject = [ "$q", "$timeout" ];

	function chatService($q, $timeout) {

		var service = {}, listener = $q.defer(), socket = {
			client : null,
			stomp : null
		};

		service.URL = "/chat-websocket";
		service.TOPIC = "/topic/chat";
		service.BROKER = "/app/chat";
		service.RECONNECT_TIMEOUT = 20000;

		service.receive = function() {
			return listener.promise;
		};

		service.send = function(message, username) {
			socket.stomp.send(service.BROKER, {}, JSON.stringify({
				content : message,
				chat_account : username
			}));

		};

		var reconnect = function() {
			$timeout(function() {
				initialize();
			}, this.RECONNECT_TIMEOUT);
		};

		var getMessage = function(data) {
			var message = JSON.parse(data), out = {};
			out.content = message.content;
			out.chat_account = message.chat_account;
			out.time = new Date();
			return out;
		};

		var startListener = function() {
			socket.stomp.subscribe(service.TOPIC, function(data) {
				listener.notify(getMessage(data.body));
			});
		};

		var initialize = function() {
			socket.client = new SockJS(service.URL);
			socket.stomp = Stomp.over(socket.client);
			socket.stomp.connect({}, startListener);
			socket.stomp.onclose = reconnect;
		};

		initialize();
		return service;
	}
})();