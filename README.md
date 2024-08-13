# Social network backend
>This project is built using a microservices architecture with the Spring framework, which allows independent development of different components. This also provides scalability and easy maintenance of the application.

## Technology stack
- Java 17
- Spring(Boot, Cache, Cloud, Data JPA, Mail Sender, Security, Web)
- RabbitMQ
- Prometheus
- OpenFeign
- Docker
- PostgreSQL
- MongoDB
- Redis

## Services

### API Gateway:
Allows for seamless data transfer between endpoints and ensuring that User using JWT token.

### Discovery server:
Detects the other services.

### Notification service:
Has the responsibility to send emails using RabbitMQ to receive information from Publication and Security services.

### Publication service:
Allows you to Create/Read/Update/Delete publications and interract with them(press like and write comments). When new publications are created, a notification is sent to all subscribers' emails. This servise does not contain any information about User and take it from JWT and Feign Client. The service is also capable of caching popular posts using Redis, which reduces the load on the database.

PublicationController
-  `GET` **`/publication`** Returns all publications paginated
-  `GET` **`/publication/{id}`** Returns publication by id
-  `PATCH` **`/publication/{id}/like`** Сhanges the like status
-  `PATCH` **`/publication/{id}/comment`** Creates a comment to a publication
-  `GET` **`/publication/user/{userId}`** Returns all publications by user id
-  `POST` **`/publication/new`** Creates new publication
-  `PATCH` **`/publication/update/{id}`** Updates publication by id
-  `DELETE` **`/publication/delete/{id}`** Deletes publication by id

### Security service:
Responsible for registration, JWTs and subscriptions. To confirm your account you are sent an activation code to your email. After confirming your email you are able to generate JWT token, which will expire in 2 hours

AuthController
- `POST` **`auth/register`** Registers a new account
- `GET` **`auth/activate`** Activates account with given code
- `PATCH` **`auth/update`** Updates account
- `DELETE` **`auth/delete`** Deletes account
- `POST` **`auth/token`** Generates JWT token for account
- `GET` **`auth/validate`** Validates given JWT

SubscribeController
- `GET` **`/users`** Returns all users
- `GET` **`/users/subscibers`** Returns all users subscribed to you
- `GET` **`/users/subsciptions`** Returns all users you are subscribed to
- `PATCH` **`/users/subscribe/{id}`** Allows you to subscribe to a user with given id
- `PATCH` **`/users/unsubscribe/{id}`** Allows you to unsubscribe from a user with given id
