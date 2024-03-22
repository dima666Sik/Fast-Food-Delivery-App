# Server part Food Ordering App

## List of Contents

- [Description](#description)
- [Technologies](#technologies)
- [Project Structure](#project-structure)
- [Usage](#usage)
- [Author Info](#author-info)

---

## Description

### Preview, I hope you enjoy it 😊

Welcome to server part of the Food Ordering App that consists of APIs for interacting with the client side application.
For the backend, we used the Spring Boot, Spring Security, Hibernate frameworks and part of Spring Data framework namely
Spring Data JPA.
The application uses the REST architectural approach, which involves using HTTP methods to interact with resources on
the server.
All technologies you can see in the part of this README.md under the heading [Technologies](#technologies).

---

## Technologies

- `Java Core`
- `Spring Core`
- `Spring Boot`
- `Spring Security`
- `Spring Data JPA`
- `JavaMail library`
- `lombok`
- `RDBMS MySQL`
- `Maven`

---

## Project Structure

- The application consists of three package, and each module contains of layers.
- An app modules:
    - `auth`: This package is responsible for authentication-related functionality, including user registration and
      login. To ensure security, we use JWT tokens to authenticate users during requests to the server;
    - `general`: The "general" package contains classes that are responsible for most of the functionality of the application,
      the controllers process HTTP requests after the user successfully passes the filter.
      Examples include: requests to process products, slides, product preferences, product reviews,
      product sorting by certain parameters, and more.
      It includes common business logic, and other essential components.
    - `mail`: This package contains classes that are responsible for interaction with mail.

### The architecture of the backend of the web application

- The architecture of the backend of the web application:<br>
  ![The architecture](src/main/resources/github-files/server-part-of-app-diagram.png)
  - The web layer is the top layer of the architecture and is responsible for interacting with the user and processing incoming requests. The web layer processes HTTP requests, validates and processes input data, executes business logic, and returns the correct response to the user. 
  - The service layer is responsible for implementing the business logic of the application. At this level, services can interact with the repository level to obtain the necessary data and ensure the execution of business rules. 
  - The repository level is the lowest in the architecture and is responsible for storing and accessing application data.

### Web application database diagram
The Hibernate used as part of Spring Data JPA, which provides an abstraction for working with databases through JPA (Java Persistence API).
JPA defines a standard for working with object-relational mapping (ORM) in Java applications. JpaRepository is part of Spring Data JPA that provides an abstraction for working with databases.
- Web application database:<br>
  ![Database](src/main/resources/github-files/database-diagram.png)

---

## Usage

1. I would you recommended to clone my project from the GitHub.
   <br> If you want to do this, please use this command:

```md  
git clone https://github.com/dima666Sik/Fast-Food-Delivery-App.git
```

2. To run this project, you will need to install:
    - JDK 17 or higher;
    - MySQL RDBMS.
3. If you want to start this project please change the `application.properties` on valid data.

---

## Author Info

- [Linkedin](https://www.linkedin.com/in/dmytro-kohol-333a7a2aa/)

- [GitHub](https://github.com/dima666Sik)

[Back To The Top](#description)