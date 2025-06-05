
# Voting Application Backend

This is the backend for the Voting Application, built with Spring Boot.

## Prerequisites

- Java JDK 17+
- Maven
- MySQL

## Configuration

The application is configured to use MySQL database. You can modify the database connection in `src/main/resources/application.yml`.

Default settings:
```
spring.datasource.url=jdbc:mysql://localhost:3306/voting_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root
```

## Project Structure

The project follows the standard Maven structure:

```
backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/voting/app/
│   │   │       ├── config/
│   │   │       ├── controller/
│   │   │       ├── dto/
│   │   │       │   ├── request/
│   │   │       │   └── response/
│   │   │       ├── mapper/
│   │   │       ├── model/
│   │   │       ├── repository/
│   │   │       ├── security/
│   │   │       │   ├── jwt/
│   │   │       │   └── services/
│   │   │       ├── service/
│   │   │       └── VotingApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
└── pom.xml
```

## Running the Application

1. Clone the repository
2. Navigate to the project directory
3. Run `mvn spring-boot:run`

The application will start on port 8080 with context path `/api`, so the base URL will be `http://localhost:8080/api`.

## API Documentation

The API documentation is available at `http://localhost:8080/api/swagger-ui.html` when the application is running.

## Default Users

An admin user is created by default with the following credentials:
- Email: admin@voting.com
- Password: admin123

## Available Endpoints

### Authentication
- POST `/api/auth/login` - Log in with email and password
- POST `/api/auth/register` - Register a new user

### Candidates
- GET `/api/candidates` - Get all candidates
- GET `/api/candidates/{id}` - Get a candidate by ID

### Votes
- POST `/api/votes` - Cast a vote
- GET `/api/votes/results` - Get vote results
- GET `/api/votes/voter/{voterId}` - Check if a voter has already voted

### Admin (requires ADMIN role)
- GET `/api/admin/candidates` - Get all candidates
- POST `/api/admin/candidates` - Create a new candidate
- DELETE `/api/admin/candidates/{id}` - Delete a candidate

## Security

The application uses JWT for authentication. All endpoints except `/auth/**` and Swagger documentation require authentication.

To authenticate, obtain a JWT token by calling the login endpoint, then include it in the Authorization header of subsequent requests:
```
Authorization: Bearer {your-token}
```
