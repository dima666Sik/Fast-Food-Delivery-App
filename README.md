# Food Ordering App
## List of Contents

- [Description](#description)
- [Usage](#usage)
- [Parts site](#parts-site)
- [Requirements](#requirements)

---

## Description

This project represents a modern web application designed to streamline the food ordering process while offering an intuitive and user-friendly experience. The application provides the following key features:

- A convenient user interface ensuring navigation and accessibility;
- User registration and authorization for secure access to personalized features;
- Food ordering functionality, enabling users to browse, select, and purchase items easily;
- Integration of an AI-powered chat assistant to enhance user interaction and support;
- Menu management tools for adding new dishes;
- Online payment processing for secure transactions;
- A comprehensive menu display with dish details and recommendations;
- Features for adding items to a shopping cart and managing order quantities;
- Sorting and filtering options for dishes based on various criteria;
- The ability to write reviews and rate dishes with likes;
- Ability to select orders for delivery by couriers;
- Search functionality to quickly find specific dishes by name;
- Tools for administrators to view analytics on popular dishes and order trends;
- Email integration for technical support communication;
- Enhanced customer engagement through personalized product recommendations.

## Usage

### Prerequisites

Before starting, make sure that you have the following programs installed:

Docker (version `27.2.0` or higher)

To check versions, run:

```yaml
docker --version
```

### Running containers

#### Starting the client separately:

Go to the folder with the client part of project:

```txt
cd client
```

Run Docker Compose:

```yaml
docker-compose up --build
```

#### Starting the server separately:

Go to the folder with the server part of project:

```txt
cd server
```

Run Docker Compose:

```yaml
docker-compose up --build
```

The `docker-compose up --build` command is used for the following purposes:

Building Docker images before running,
running containers if changes have been made to the project and images need to be rebuilt,
`--build` ensures this.

### Requests
If you want to add some sliders and default products in the menu of products 
please execute the following command:
1. To add a slider images use the following command:
```http request
POST http://localhost:8080/api/v2/product/slider/add-slider-images
```
2. To add a product info use the following command:
```http request
POST http://localhost:8080/api/v2/product/foods/add-all-default-products
```
---

## Parts site

- To check the client part of the app, you can refer to my [client-readme.md](client/README.md).

- To check the server part of the app, you can refer to my [server-readme.md](server/README.md).

## Requirements

- The front-end of the application meets the following requirements:

  - Convenient and easy-to-understand interface for the user;
  - The ability to filter by type of food (pizza, hamburger, sushi, or all together);
  - The ability to add the desired product to the cart, change the quantity
    of the product or remove the product from the cart;
  - The ability to see and add popular food to the cart;
  - The possibility of sorting food according to various criteria (by name, price, likes);
  - The possibility of searching for food by name;
  - A convenient side, pop-up panel for the basket must be created;
  - Modal authorization/registration forms;
  - A form for convenient contact with technical staff. support (sending a request to the site's mail);
  - The user can write reviews for dishes and delete them;
  - The possibility to view the product by clicking on the picture;
  - Possibility of authorization/registration/exit;
  - User authentication during each request to the server;
  - Products in recommendations;
  - AI Chat Assistant for Customer:
  - Online payment option;
  - The possibility of viewing certain analytics;
  - The possibility of selecting orders for delivery by couriers;
  - The possibility of viewing completed orders.

- The backend of the application is designed with a microservice architecture, ensuring scalability and modularity. Each microservice communicates through APIs exposed via a Gateway, enabling seamless interaction with the client. The backend incorporates the following features:
  - API to provide interaction between client and Server in parts;
  - Using of Spring Security to ensure user authentication and authorization. Use JSON Web Token to transmit and verify identity information.
  - To ensure processing of requests, validation of input data and relevant checks. Process requests to create, update, and retrieve information from the database.
  - Supporting for various types of requests to the databases;
  - OpenAI API integration, providing intelligent conversational capabilities to enhance user interaction.
  - RabbitMQ integration for message queuing, facilitating efficient communication between Producer and Consumer components.
  - Stripe API integration for secure and seamless payment processing.
  - Gmail SMTP Server integration for reliable email notifications and user communication.
  - Spring Security to safeguard access to application resources, coupled with JWT-based authentication using access and refresh tokens for secure and efficient user authorization.
  - Database integration for storing and managing user, product, and other relevant data, ensuring robust data persistence.
  - Comprehensive support for CRUD operations, including data validation and necessary business logic checks for input requests.
  - Implementation of modular and integration tests to ensure the reliability and functionality of microservices.

This architecture not only optimizes backend performance but also ensures data security, reliability, and a smooth user experience.
