package nology.io.employee.employee;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import nology.io.employee.employee.entities.Employee;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.time.LocalDate;
import java.util.HashMap;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class EmployeeEndToEndTest {

    @LocalServerPort
    private int port;

    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeEndToEndTest(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }

    @BeforeEach
    public void setup(){
        RestAssured.port = this.port;
    }
    
    @Test
    public void getAllEmployees_NoEmployeeInDB_ReturnOKAndEmptyArray(){
        //arrange
        //act
        given().when().get("/employees")
                .then().statusCode(HttpStatus.OK.value())
                .body("$",hasSize(0));
        //asert

    }

    @Test
    public void getAllEmployees_EmployeesInDB_ReturnsOKAndArrayOfEmployeesFields() {

        //arrange
        Employee employee1 = new Employee();
        employee1.setFirstName("John");
        employee1.setMiddleName("Michael");
        employee1.setLastName("Smith");
        employee1.setEmail("john.smith@example.com");
        employee1.setMobileNumber("0412345678");
        employee1.setAddress("123 Example St, Sydney");
        employee1.setContractType(ContractType.PERMANENT);
        employee1.setEmploymentStatus(EmploymentStatus.FULL_TIME);
        employee1.setStartDate(LocalDate.of(2023, 1, 15));
        employee1.setFinishDate(null);
        employee1.setOnGoing(true);
        employee1.setHoursPerWeek(38);
        employeeRepository.saveAndFlush(employee1);

        Employee employee2 = new Employee();
        employee2.setFirstName("Jane");
        employee2.setMiddleName(null);
        employee2.setLastName("Doe");
        employee2.setEmail("jane.doe@example.com");
        employee2.setMobileNumber("0498765432");
        employee2.setAddress("456 Sample Rd, Melbourne");
        employee2.setContractType(ContractType.CONTRACT);
        employee2.setEmploymentStatus(EmploymentStatus.PART_TIME);
        employee2.setStartDate(LocalDate.of(2024, 6, 1));
        employee2.setFinishDate(LocalDate.of(2025, 6, 1));
        employee2.setOnGoing(false);
        employee2.setHoursPerWeek(20);
        employeeRepository.saveAndFlush(employee2);

        //testing body type
        given().when().get("/employees")
                .then().statusCode(HttpStatus.OK.value())
                .body("$", hasSize(2))
                .body("firstName", hasItems("John", "Jane"))
                .body("lastName", hasItems("Smith", "Doe"))
                .body("contractType", hasItems("PERMANENT", "CONTRACT"))
                .body("employmentStatus", hasItems("FULL_TIME", "PART_TIME"))
                .body("onGoing", hasItems(true, false))
                .body("hoursPerWeek", hasItems(38, 20))
                .body(matchesJsonSchemaInClasspath("schemas/employee-list-schema.json"));
    }

    @Test
    public void getById_IdNotFond(){

        //arrange
        long id = 1L;

        //testing body type
        given().when().get("/employees/" + id)
                .then().statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", matchesPattern("Can't Find Employee with ID 1"))
                .body("error", matchesPattern("Not Found"))
                .body(matchesJsonSchemaInClasspath("schemas/api-error-schema.json"));

    }

    @Test
    public void getById_InvalidId_BadRequest(){
        given().when().get("employees/test")
                .then().log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("error", matchesPattern("Bad Request"))
                .body(matchesJsonSchemaInClasspath("schemas/api-error-schema.json"));

    }

    //suceed with id
    @Test
    public void getById_ValidForExistingEmployee_Success() {

        //arrange
        Employee employee1 = new Employee();
        employee1.setFirstName("John");
        employee1.setMiddleName("Michael");
        employee1.setLastName("Smith");
        employee1.setEmail("john.smith@example.com");
        employee1.setMobileNumber("0412345678");
        employee1.setAddress("123 Example St, Sydney");
        employee1.setContractType(ContractType.PERMANENT);
        employee1.setEmploymentStatus(EmploymentStatus.FULL_TIME);
        employee1.setStartDate(LocalDate.of(2023, 1, 15));
        employee1.setFinishDate(null);
        employee1.setOnGoing(true);
        employee1.setHoursPerWeek(38);
        employeeRepository.saveAndFlush(employee1);

        employeeRepository.saveAndFlush(employee1);

        //act
        given().when().get("employees/" + employee1.getId())
                .then()
                // .log().body()
                .statusCode(HttpStatus.OK.value())
                .body("firstName", matchesPattern(employee1.getFirstName()))
                .body("middleName", matchesPattern(employee1.getMiddleName()))
                .body("lastName", matchesPattern(employee1.getLastName()))
                .body("email", matchesPattern(employee1.getEmail()))
                .body("mobileNumber", matchesPattern(employee1.getMobileNumber()))
                .body("address", matchesPattern(employee1.getAddress()))
                .body("contractType", equalTo(employee1.getContractType().name()))
                .body("employmentStatus", equalTo(employee1.getEmploymentStatus().name()))
                .body("startDate", matchesPattern(employee1.getStartDate().toString()))
                .body("finishDate", equalTo(null))
                .body("onGoing", equalTo(employee1.isOnGoing()))
                .body("hoursPerWeek", equalTo(employee1.getHoursPerWeek()))
                .body(matchesJsonSchemaInClasspath("schemas/employee-schema.json"));

    }

    // @Test
    // public void createTask_InvalidDto_BadRequest(){

    //     HashMap<String, String> data = new HashMap<>();
    //     data.put("title", "");


    //     given().contentType(ContentType.JSON).body(data)
    //             .when().post("/tasks")
    //             .then().log().body()
    //             .statusCode(HttpStatus.BAD_REQUEST.value()); //caught springs error

    // }

    // //creating task via DTO/ hash map
    // @Test
    // public void createTask_ValidDto_Created(){

    //      HashMap<String, String> data = new HashMap<>();
    //      data.put("title", "Title 1");
    //      data.put("description", "Description 1");
    //      data.put("status", "START");

    //      given().contentType(ContentType.JSON).body(data)
    //         .when().post("/tasks")
    //         .then().log().body()
    //         .statusCode(HttpStatus.CREATED.value())
    //         .body("title", matchesPattern("Title 1"))
    //         .body("description", matchesPattern("Description 1"))
    //         .body("status", matchesPattern("START"))
    //         .body(matchesJsonSchemaInClasspath("schemas/task-schema.json"));


    // }

    // //patch with bad body

    // @Test
    // public void patchTask_InvalidBodyDto_BadRequest(){

    //     //arrange

    //     //need to have existint task with id
    //     Task task1 = new Task();
    //     task1.setTitle("Title 1");
    //     task1.setDescription("Description 1");
    //     task1.setStatus("START");

    //     taskRepository.saveAndFlush(task1);

    //     HashMap<String, String> data = new HashMap<>();
    //     data.put("title", "");

    //     given().contentType(ContentType.JSON).body(data)
    //             .when().patch("/tasks/" + task1.getId())
    //             .then().log().body()
    //             .statusCode(HttpStatus.BAD_REQUEST.value()); //caught springs error

    // }

    // //patch with invalid id

    // @Test
    // public void patchTask_InvalidId_NotFound(){

    //     //has task but can't find ID
    //     HashMap<String, String> data = new HashMap<>();
    //     data.put("title", "Title 1");
    //     data.put("description", "Description 1");
    //     data.put("status", "START");

    //     given().contentType(ContentType.JSON).body(data)
    //             .when().patch("tasks/1")
    //             .then().log().body()
    //             .statusCode(HttpStatus.NOT_FOUND.value())
    //             .body("error", matchesPattern("Not Found"))
    //             .body(matchesJsonSchemaInClasspath("schemas/api-error-schema.json"));

    // }

    // //sucess patch

    // @Test
    // public void patchTask_ValidIdBody_Success(){

    //     HashMap<String, String> data = new HashMap<>();
    //     data.put("title", "Title NEW");
    //     data.put("description", "Description NEW");
    //     data.put("status", "FINISHED");

    //     Task task1 = new Task();
    //     task1.setTitle("Title 1");
    //     task1.setDescription("Description 1");
    //     task1.setStatus("START");

    //     taskRepository.saveAndFlush(task1);

    //     given().contentType(ContentType.JSON).body(data)
    //             .when().patch("tasks/" + task1.getId())
    //             .then().log().body()
    //             .statusCode(HttpStatus.OK.value())
    //             .body("title", matchesPattern("Title NEW"))
    //             .body("description", matchesPattern("Description NEW"))
    //             .body("status", matchesPattern("FINISHED"))
    //             .body(matchesJsonSchemaInClasspath("schemas/task-schema.json"));

    // }
    
    // //delete fail
    // @Test
    // public void deleteTask_invalidId_NotFound(){

    //     //arrange
    //     long id = 1L;

    //     //testing body type
    //     given().when().delete("/tasks/" + id)
    //             .then().statusCode(HttpStatus.NOT_FOUND.value())
    //             .body("message", matchesPattern("CAN'T FIND AND DELETE TASK THAT DOESN'T EXIST with ID " + id))
    //             .body("error", matchesPattern("Not Found"))
    //             .body(matchesJsonSchemaInClasspath("schemas/api-error-schema.json"));

    // }

    // //delete success
    // @Test
    // public void deleteTask_validId_Success(){

    //     //arrange
    //     Task task1 = new Task();
    //     task1.setTitle("Title 1");
    //     task1.setDescription("Description 1");
    //     task1.setStatus("START");

    //     taskRepository.saveAndFlush(task1);

    //     //need to grab current task ID instead of setting 1 after flush -> sets ID after flush
    //     long id = task1.getId();

    //     //testing body type
    //     given().when().delete("/tasks/" + id)
    //             .then().statusCode(HttpStatus.NO_CONTENT.value());//no body for our case can't validate against schema

    // }
    
}
