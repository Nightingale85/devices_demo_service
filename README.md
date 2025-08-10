# Devices Service

## Getting Started

### Local Development

1. Database Setup
    - Create a PostgreSQL database named `devices_db`

2. Application Startup
    - Start the application with the local profile using the following command:
      ```
      --spring.profiles.active=local
      ```

## API Documentation

Swagger UI can be accessed at the following URL when the application is running locally:

http://localhost:8080/swagger-ui/index.html


## Things to improve

1. This interface provides interactive documentation for all available API endpoints.

2. Add pagination of lots of data

3. Single query delete with check for in use in single query

4. Add validation for DTOs to reflect DB constraints

5. Add to exception handler bean validation exception, optimistic locking exception, 	also add UID to response for better troubleshooting

6. Add logging
