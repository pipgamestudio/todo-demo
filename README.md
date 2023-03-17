# todo-demo
A very simple Todo demo using Springboot OAuth2 client (InMemoryOAuth2AuthorizedClientService) and Google Firestore Database for learning and testing purpose.<br />
The supported OAuth2 clients: Google, Facebook and Github.<br />
(Used some source code from <a href="https://github.com/sohamds1/spring-boot-todo-app">spring-boot-todo-app</a>)<br />
<br />
<b>Prerequisites:</b><br />
- Put your server hostname, Google Cloud / Facebook / Github app client id, secret in "application.properties" file.<br />
- Copy your Google Firebase Service Account private key file and rename to "firebase-service-account.json", put it under "src/main/resources".<br />
- Google Firestore Database structure (but no need to set it up):<br />
<img src="https://firebasestorage.googleapis.com/v0/b/todo-5b5f4.appspot.com/o/todo-demo%2Ftodo-firebase-db-structure.jpg?alt=media&token=560f4d22-b4bb-47b2-af25-05912d4201dd" /><br /><br />
<b>Intro:</b><br />
- There are 2 Todo lists: Not Complete List and Completed List<br />
- You can only add a new todo item to Not Complete Todo List<br />
- Then can mark complete that todo item, it will be moved to Completed Todo List<br />
- You can only delete a todo item in Completed Todo List<br /><br />
<b>Build and run locally:</b><br />
- mvnw clean compile<br />
- mvnw spring-boot:run<br />
- http://localhost:8080<br /><br />
<b>Online demo:</b><br />
- <a href="https://pip-todo-thkfg6rqga-uc.a.run.app">PIP TODO</a><br />
(My Google Firesbase Account is "Spark" plan, so if the transactions exceed the limit, there may be some problems encountered when accessing the demo; then need to try again later)
