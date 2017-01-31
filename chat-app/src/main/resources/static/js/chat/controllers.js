(function() {
	'use strict';
	angular.module('chatApp').controller('ChatController', ChatController);
	ChatController.$inject = [ "ChatService", "$scope" ];
	function ChatController(ChatService, $scope) {

		$scope.messages = [];
		$scope.showUsernameInput = "chat_interface_user";
		$scope.chatWindowEnabled = "";

		$scope.setUsername = function() {

			$scope.showUsernameInput = "chat_interface_user disabled_chat";
			$scope.chatWindowEnabled = "enabled_chat";
		};

		$scope.changeUsername = function() {
			$scope.username = "";
			$scope.showUsernameInput = "chat_interface_user";
			$scope.chatWindowEnabled = "";
		};

		$scope.sendMessage = function() {
			ChatService.send($scope.message, $scope.username);
			$scope.message = "";
		};

		ChatService.receive().then(null, null, function(message) {
			$scope.messages.push(message);
		});
	}
})();
