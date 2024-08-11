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
Allows you to CRUD publications and interract with them(press like and write comments). This servise does not contain any information about User and take it from JWT and Feign Client. The service is also capable of caching popular posts using Redis, which reduces the load on the database

documentation will be here tommorow(i hope so)

### Security service:
