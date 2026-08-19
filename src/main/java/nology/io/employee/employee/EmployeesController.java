package nology.io.employee.employee;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import nology.io.employee.employee.dtos.CreateEmployeeRequest;
import nology.io.employee.employee.entities.Employee;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/employees")
@Tag(name = "Employees Controller")
public class EmployeesController {

    private final EmployeeService employeeService;

    public EmployeesController (EmployeeService employeeService){

        this.employeeService = employeeService;

    }

    @GetMapping()
    public ResponseEntity<List<Employee>> findAllEmployees() {
        List<Employee> allEmployees = this.employeeService.findAll();

        return ResponseEntity.ok(allEmployees);
    }

    @PostMapping()
    public ResponseEntity<Employee> createEmployee(@RequestBody @Valid CreateEmployeeRequest data) {

        Employee createdEmployee = this.employeeService.create(data);

        return new ResponseEntity<Employee>(createdEmployee, HttpStatus.CREATED);
    }
    
    
    
    
}
