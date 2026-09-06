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
    
}
