Welcome to AQL TEnmo Money Transfer


## !!Application Overview!!
 - Technologies involved: Spring, Spring JPA, Spring Security, Java, Jenkins, Docker, Maven.
 
 - Summary : A peer-to-peer money transfer application with a Spring/SQL backend and a Command-line front end.
 The server is a RESTful API ,so you can also interact with it via Postman/Insomnia
 

## TEnmo Client
A command line interface used to interact with the server API 
This is the menu layout of the application

[Register]
[Login]
 - [View past transfers]
    - [get a list of completed transfers]
 - [View your pending requests]
    - [accept or reject requests]
 - [Send TE bucks]
    - [send money to other users]
 - [Request TE bucks]
    - [request money from other users]
 - [Exit]






## TEnmo Server
a RESTful api in which you can send web requests to fetch data as long as you are an authenticated user.

How to use:
- Make sure you have a database running and have configured your application.properties to point to that database or esle
this program will not work.

- When Inside Intelij or your IDE run the TenmoApplication.java and see the server spin up to life.
- With Postman/Insomia you can test the various endpoints .

- To get authenticated and use the server
please register and log-in to obtain bearer token.



Endpoints within TEnmo are:

## Register / login
    - POST http://<Ip of Application>/register - to register a new user
        example of a new User JSON Request
        {
        "username" : "user",
        "passowrd" : "password"
        }
    - POST http://<Ip of Application>/login - to login in as user
        example of a new User JSON Request
        {
        "username" : "user",
        "passowrd" : "password"
        }     


 ## Account:
    - GET /user/account/id/{userId} - gets a users account by User ID.
    - GET /user/account/username/{username} - gets a user account by Username.

 ## Transfer:
    - GET /transfer/{transactionId} - gets a transaction record by Transaction ID.
    - GET /transfer/list/id/{userId}/{filter} - gets a list of transfers belonging to UserID filters are (Approved, Rejected, Pending, All).
    - POST /transfer/send - introduce a transfer Object to the server, so it can process it.
    - PUT /transfer/update - reintroduce a transfer Object that has had its status from Pending to either Approved or Rejected.

 ## User: 
    -GET /user/id/{id} - gets a User by a UserId.
    -GET /user/username/{username} - gets a User by Username.