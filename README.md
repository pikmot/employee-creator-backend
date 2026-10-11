# Employee Creator Backend

A Spring Boot REST API for managing employees: full CRUD, validation, pagination and name search, with MySQL persistence and an automated test suite.

```
employee/
├── src/
│   ├── main/java/nology/io/employee/
│   │   ├── employee/          # Controller, service, repository, entity, DTOs, enums
│   │   ├── common/            # Global exception handler, page DTOs, custom exceptions
│   │   └── config/            # CORS, ModelMapper, dev data seeder
│   ├── main/resources/        # application.properties
│   └── test/
│       ├── java/              # End-to-end (REST Assured) and service (Mockito) tests
│       └── resources/         # H2 test config, JSON schemas, cleanup SQL
├── .env.example
├── pom.xml
└── README.md
```

## Demo & Snippets

Swagger UI (while running locally):

```
http://localhost:8080/swagger-ui/index.html
```

### Endpoints

| Method | Path                                    | Description                                 |
| ------ | --------------------------------------- | ------------------------------------------- |
| GET    | `/employees?page=1&size=10&searchTerm=` | Paginated list, optionally filtered by name |
| GET    | `/employees/{id}`                       | Single employee                             |
| POST   | `/employees`                            | Create (validated)                          |
| PATCH  | `/employees/{id}`                       | Partial update                              |
| DELETE | `/employees/{id}`                       | Delete                                      |

List response:

```json
{
  "currentPage": 1,
  "totalPages": 4,
  "totalResults": 20,
  "resultsPerPage": 5,
  "nextPage": 2,
  "previousPage": null,
  "data": [{ "id": 1, "firstName": "..." }]
}
```

## Requirements / Purpose

### MVP

- Full CRUD REST API for employees
- Server-side validation on required fields
- Consistent error responses

### Stretch goals completed

- Pagination (`page` min 1, `size` default 10, max 20)
- Case-insensitive search by first, middle or last name
- Seeded sample data for development
- Automated unit and end-to-end tests

### Purpose of project

Second Full Stack project using React and Spring Boot: a REST API with a relational database, DTO-based validation, centralised error handling and test coverage.

### Tech Stack

- **Backend:** Spring Boot 4.1, Java 17, Spring Data JPA, MySQL
- **Validation:** Jakarta Validation
- **Docs:** Springdoc OpenAPI (Swagger UI)
- **Mapping:** ModelMapper
- **Dev data:** JavaFaker (seeds 20 employees under the `dev` profile when the table is empty)
- **Testing:** JUnit 5, Mockito, REST Assured (+ JSON schema validation), H2 in-memory database

## Build Steps

```bash
cp .env.example .env     # then fill in your values
./mvnw spring-boot:run
```

`.env`:

```bash
DB_HOST=localhost
DB_PORT=3306
DB_USER=root
DB_NAME=employee_creator_database
DB_PASSWORD=your_password
SPRING_PROFILE=dev
```

Requires a running MySQL server (the database is created if missing). With `SPRING_PROFILE=dev` the seeder adds 20 fake employees on first run.

Run the tests (no MySQL needed, they use H2):

```bash
./mvnw test
```

## Design Goals / Approach

- **Separate Create and Update DTOs** - `CreateEmployeeRequest` uses `@NotBlank` / `@NotNull` / `@Email` / `@Min` / `@Max`; `UpdateEmployeeRequest` only validates fields that are provided, allowing partial updates.
- **Service-level validation** - `EmployeeService.create` also rejects invalid data with a custom `BadRequestException`, so the service is safe when called outside the controller.
- **Global Exception Handler** - one `@ControllerAdvice` maps not-found, bad request, validation, malformed body and unprocessable errors to a consistent `ApiErrorResponse`.
- **ModelMapper skipping nulls** - PATCH only updates the fields sent.
- **Generic page response** - `PageResponse<T>` with 1-based page numbers and next/previous page links.
- **Test pyramid** - Mockito service unit tests plus REST Assured end-to-end tests that assert status codes, field values and JSON schemas.

## Features

- GET all (paginated, searchable), GET by ID, POST, PATCH, DELETE
- `contractType` (PERMANENT / CONTRACT / CASUAL) and `employmentStatus` (FULL_TIME / PART_TIME) enums stored as strings
- `finishDate` and `middleName` optional; `onGoing` flag
- Validation errors, not-found, malformed JSON and invalid ID handled with clear responses
- CORS configured for the frontend on `localhost:5173`
- Faker-based dev seeder

## Testing

- **End-to-end (12 tests):** list (empty and populated), get by ID (found, not found, invalid), create (valid, invalid), patch (valid, invalid body, not found), delete (valid, not found)
- **Service (8 tests):** findAll, findById, create (valid, invalid), update (exists, missing), delete (exists, missing)
- Context-load test
- JSON Schema checks for employee, employee list and error responses

## Known issues

- The 422 handler is bound to the wrong class: `@ExceptionHandler(UnprocessableContent.class)` in `GlobalExceptionHandler` imports Spring's `HttpClientErrorException.UnprocessableContent` instead of the custom `UnprocessableContentException`, so "Page Too High!" is likely not returned as a 422
- Search matches the first, middle and last names separately - a two-word search like "john smith" returns nothing
- No tests yet for the pagination and search query parameters
- The search repository method name contains a stray `OrOr` (`...IgnoreCaseOrOrMiddleName...`); the app starts, but it's probably a typo, and the unused `findByFirstName` variants could be removed
- `spring.application.name` is still `library` (leftover from a template)

## Future Goals

- Fix the 422 exception handler binding and add a test for it
- Search across the combined full name
- Tests for pagination and search
- Filters (e.g. contract type, employment status)
- A `pinned` flag on employees for a favourites section
- Add `CASUAL` to the frontend form

## Change logs

### 19/08/2026 - Initial Setup and CRUD

- Project template, controller, service, repository, entity, ModelMapper, global exception handler
- Full CRUD with error handling

### 22/08/2026 - 25/08/2026 - CORS and fixes

- CORS config for the frontend; fixed missing `finishDate` / `onGoing` data (getter naming bug)

### 06/09/2026 - Testing

- JSON schemas, H2 test config and cleanup SQL
- End-to-end tests for list, get, create, patch, delete
- Service-layer unit tests

### 19/09/2026 - 20/09/2026 - Pagination and seeding

- Pagination with page response DTO and "no data" handling
- Faker seeder for 20 employees (including `hoursPerWeek`)

### 24/09/2026 - 25/09/2026 - Search

- Search by first / middle / last name via `searchTerm`

## What did you struggle with?

- **The `onGoing` bug.** `finishDate` and `onGoing` went missing from responses because of how the boolean was named.
- **My first tests.** Comparing enums, strings, booleans and numbers needed different matchers, and optional fields had to be nullable for tests to pass.
- **Where validation runs.** `@Valid` works at the controller, so the service needed its own checks to be testable.
- **Pagination edge cases.** Empty results and pages past the end needed their own handling.
- **Faker seed data.** Converting `Date` to `LocalDate` and keeping `finishDate` and `onGoing` consistent took a few tries.
- **Full-name search.** Searching first, middle and last name separately works, but a combined search is still unsolved.
