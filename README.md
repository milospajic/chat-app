## Chat application based on spring boot, websockets and angularJS

First you're offered to enter your chat name. After completing the first step you enter the chat. You can change your chat name anytime by clicking on icon in lower left part of the chat window. 

## Installation

It's maven application, so just do maven build. Open ChatApplication class and run the app as java aplication. Open http://localhost:8080 in your favourite browser and enjoy

## Tests

There are java unit (ChatControllerTests.java) and integration (ChatControllerIntegrationTest.java) tests. You can run them as JUnit tests or by mvn test

There are also AngularJS tests (controllers.spec.js) written in Jasmine framework. You can run them by starting AngularTest.java as java application and opening http://localhost:9999 page. You can also run them together with java tests. To do so, you just have to uncomment phantomjs-maven-plugin and jasmine-maven-plugin and run mvn test
