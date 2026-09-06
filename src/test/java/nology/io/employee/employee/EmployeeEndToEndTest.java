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

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


import java.util.HashMap;

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
    
}
