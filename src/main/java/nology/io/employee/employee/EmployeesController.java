package nology.io.employee.employee;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import nology.io.employee.common.PageResponseAssembler;
import nology.io.employee.common.dtos.PageQueryParams;
import nology.io.employee.common.dtos.PageResponse;
import nology.io.employee.common.exceptions.NotFoundException;
import nology.io.employee.common.exceptions.UnprocessableContentException;
import nology.io.employee.employee.dtos.CreateEmployeeRequest;
import nology.io.employee.employee.dtos.UpdateEmployeeRequest;
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
    public ResponseEntity<PageResponse<Employee>> findAllEmployees( @ModelAttribute PageQueryParams params) {

        PageRequest pageRequest = PageRequest.of(params.getPage()-1,params.getSize());

        Page<Employee> employeePage = this.employeeService.findAll(pageRequest);

        PageResponse<Employee> response = new PageResponse<>(employeePage.getNumber() +1, employeePage.getTotalPages(),employeePage.getTotalElements(), employeePage.getSize(), employeePage.hasNext() ? employeePage.getNumber() + 2 : null, employeePage.hasPrevious() ? employeePage.getNumber(): null, employeePage.getContent());

        if (employeePage.getTotalPages() < params.getPage()){


            throw new UnprocessableContentException("Page Too High!");
        }

        // PageResponse<Employee> response = PageResponseAssembler.toPageResposne(employeePage, employeePage::getContent());

        // return ResponseEntity.ok(allEmployees);
        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public ResponseEntity<Employee> createEmployee(@RequestBody @Valid CreateEmployeeRequest data) {

        Employee createdEmployee = this.employeeService.create(data);

        return new ResponseEntity<Employee>(createdEmployee, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {

        Employee foundEmployee = this.employeeService.findById(id).orElseThrow(()-> new NotFoundException("Can't Find Employee with ID " + id));

        return ResponseEntity.ok(foundEmployee);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Employee> updateEmployeeById(@PathVariable  Long id, @Valid @RequestBody UpdateEmployeeRequest data){

        Employee foundEmployee = this.employeeService.updateById(id,data).orElseThrow(() -> new NotFoundException("Can't Find Employee with ID " + id));

        return ResponseEntity.ok(foundEmployee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployeeById(@PathVariable Long id){

        boolean isDeleted = this.employeeService.deleteById(id);

        if(isDeleted){
            return ResponseEntity.noContent().build();
        }

        throw new NotFoundException("Can't Find and Delete Employee with ID " + id);


    }
    
    
    
    
    
}
