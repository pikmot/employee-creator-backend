package nology.io.employee.employee;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import nology.io.employee.employee.dtos.CreateEmployeeRequest;
import nology.io.employee.employee.dtos.UpdateEmployeeRequest;
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

    public Optional<Employee> findById(Long id) {
        return this.repo.findById(id);
       
    }

    public Optional<Employee> updateById(Long id, UpdateEmployeeRequest data){

        Optional<Employee> result = this.findById(id);
        if (result.isEmpty()){
            return result;
        }

        Employee foundEmployee = result.get();

        this.mapper.map(data,foundEmployee);

        this.repo.saveAndFlush(foundEmployee);

        return Optional.of(foundEmployee);

    }

    public boolean deleteById(Long id) {

        Optional<Employee> result = this.findById(id);

        if (result.isEmpty()){
            return false;
        }

        this.repo.delete(result.get());
        return true;
    }
    


}
