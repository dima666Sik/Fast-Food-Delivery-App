# Food Ordering App

![Project Video](github-files/final_app_v_1.gif)

## Description

The application provides users with a convenient option to place orders online. Taking into account the needs of different users, the functionality of the application includes registration and authorization, sorting products by categories, viewing hot offers, as well as the ability to add products to the cart and choose a delivery method. In addition, authenticated users can leave reviews and likes for selected products, as well as view their orders and personal information. The application also allows administrators to create and manage other administrators.

Even if you are a guest, you can still order food, although you do not have access to all the features of the app.

In general, this application offers a convenient and fairly complete set of functions for convenient and efficient ordering of food online.

## Parts site

- To check the client part of the app, you can refer to my [client-readme.md](client/README.md).

- To check the server part of the app, you can refer to my [server-readme.md](server/README.md).

## Meets for requirements

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
  - Products in recommendations.

- The back-end of the application meets the following requirements:
  - Using MySQL as a database to store information about users, products and other relevant data.
  - API to provide interaction between client and server
    in parts;
  - Using of Spring Security to ensure user authentication and authorization. Use JSON Web Token to transmit and verify identity information.
  - To ensure processing of requests, validation of input data and relevant checks. Process requests to create, update, and retrieve information from the database.
  - Supporting for various types of requests to the database;
  - To work with an additional service - e-mail;
