package nology.io.employee.employee;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;
import nology.io.employee.employee.dtos.CreateEmployeeRequest;
import nology.io.employee.employee.entities.Employee;

@Service
public class EmployeeService {
    
    private final EmployeeRepository repo;

    private final ModelMapper mapper;
    
    public EmployeeService(EmployeeRepository repo, ModelMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public List<Employee> findAll() {
        return this.repo.findAll();
    }

    public Employee create(CreateEmployeeRequest data) {

        Employee createdEmployee = this.mapper.map(data, Employee.class);
        this.repo.saveAndFlush(createdEmployee);

        return createdEmployee;
    }
    


}
