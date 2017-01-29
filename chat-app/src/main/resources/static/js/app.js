(function() {
  angular.module("chatApp",["ngRoute","luegg.directives"]).config(routes);
  
  routes.$inject = ["$routeProvider"];

  function routes ($routeProvider) {
	  $routeProvider
	    .when("/", {
	        templateUrl : "chat.html",
	        controller : "ChatController"
	    })
}})();