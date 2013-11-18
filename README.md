academic-COMET
==============

The goal of this project is to provide a web interface for managing tasks in the MyDropBox service. This new interface will be integrated with the previous architecture, ensuring interoperability between TCP, RMI and Web clients. This is a mandatory requirement. 
Users can log in the web-interface to conduct the following tasks:

- View List of Tasks
- Add a New Task
- Edit a Task
- Delete a Task
- View Online Users

For the sake of simplicity, lets keep out of this the functionality related with file upload/download.
The web app should be able to receive asynchronous notifications in the web-browser. For instance, when a new user (X) logs-in the application all the online users should receive a notification “user X is now online”. When user Y logs out, all the others should receive an instant notification: “user Y quit the application”. When a user X inserts a new task, all the others receive “User X inserted a new task”. The same rational for the other actions related with the tasks (edit, delete).
In order to notify immediately the web-browsers of the clients when these events occur you should make use of COMET, a technology that enables the server to push data directly to the browser.
Another part of this project is to be able to synchronize your tasks with a 3rd party service: Google Tasks. The interoperability between web services is how the web is connected today. In order to interact with Google Tasks, you will make use of its REST API.
