# Employee Creator Frontend

A React frontend for managing employees with full CRUD functionality.

```
employee-creator-frontend/
├── src/
│ ├── components/ # UI components (EmployeeCard, EmployeeForm)
│ ├── pages/ # Home and Form pages
│ ├── services/ # API calls (LoadData)
│ └── scss/ # Shared styles, variables, mixins
│
├── .env # Backend URL config
├── index.html
├── package.json
└── tsconfig.json
```

## Demo & Snippets

```
http://localhost:8080/swagger-ui/index.html#/
```

## Requirements / Purpose

### MVP

- Display a list of employees on the home page
  -Create new employees through a form
- Edit existing employee details
- Delete employees from the list
- Form fields include personal info, contact details, contract type, employment status, dates and hours

### Purpose of project

Second Full Stack project using React and Spring Boot. Focused on building a form heavy CRUD application with controlled inputs, radio buttons, checkboxes and connecting to a REST API with a relational database.

### Tech Stack

- **Backend:** Spring Boot, Java 17, Spring Data JPA, MySQL
- **Validation:** Jakarta Validation
- **Docs:** Springdoc OpenAPI
- **Mapping:** ModelMapper

## Build Steps

```bash
./mvnw spring-boot:run
```

Requires a MySQL database and a .env file

```bash
DB_HOST=localhost
DB_PORT=3306
DB_USER=root
DB_NAME=employee_creator_database
DB_PASSWORD=your_password
SPRING_PROFILE=dev
```

## Design Goals / Approach

- **Separate Create and Update DTOs** - CreateEmployeeRequest has strict validation with NotBlank and NotNull on required fields. UpdateEmployeeRequest uses Pattern validation so fields are only checked if they are provided, allowing partial updates.
- **Global Exception Handler** - All exceptions are caught in one place and return consistent error responses with timestamps, status codes and messages.
- **ModelMapper with Strict Matching** - Configured to skip null values so PATCH requests only update the fields that are sent, leaving everything else unchanged.

## Features

- GET all employees
- GET employee by ID
- POST create employee with validation
- PATCH update employee with partial data
- DELETE employee by ID
- Enum types for contract type and employment status
- Global exception handling for not found, validation errors, bad requests and missing body
- CORS configured for frontend on localhost 5173

## Known issues

- Buggy null values/undefined
- Missing Fields

## Future Goals

- Add finishDate and ongoing fields to the Employee entity
- Add integration and unit tests
- Add search and filtering endpoints

## Change logs

### 19/08/2026 - Initial Setup and CRUD

- Created initial Spring Boot project template
- Added controller, service, repository and employee entity
- Set up ModelMapper and basic global exception handler
- Added full CRUD operations with error handling

### 22/08/2026 - CORS Configuration

- Added WebConfig to allow requests from the frontend on localhost 5173

## What did you struggle with?

-
