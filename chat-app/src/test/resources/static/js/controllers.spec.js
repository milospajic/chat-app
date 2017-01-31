describe('ChatController test', function() {
	var $controller;
	var $scope = {};
	var ChatService;

	beforeEach(module('chatApp'));

	beforeEach(inject(function(_$controller_, _ChatService_) {

		ChatService = _ChatService_;

		$controller = _$controller_('ChatController', {
			ChatService : ChatService,
			$scope : $scope
		});

	}));

	it('controller should be defined', function() {
		expect($controller).toBeDefined();
	})

	it('should hide username input and enable chat', function() {		
		$scope.setUsername();
		
		expect($scope.showUsernameInput).toEqual(
				'chat_interface_user disabled_chat');
		expect($scope.chatWindowEnabled).toEqual('enabled_chat');
	})

	it('should hide chat and enable username input', function() {
		$scope.changeUsername();
		
		expect($scope.username).toBe("");
		expect($scope.showUsernameInput).toEqual('chat_interface_user');
		expect($scope.chatWindowEnabled).toBe("");
	})

	it('$scope.message should be empty after sendMessage call', function() {
		$scope.message = "not empty";
		
		spyOn(ChatService, 'send').and.callFake(function() {
		});

		$scope.sendMessage();

		expect($scope.message).toBe("");
	});

});