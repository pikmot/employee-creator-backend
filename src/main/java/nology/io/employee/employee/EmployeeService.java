package nology.io.employee.employee;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import nology.io.employee.common.exceptions.BadRequestException;
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

        if (data.getFirstName() == null || data.getFirstName().isBlank()) {
        throw new BadRequestException("firstName cannot be blank");
        }
        if (data.getLastName() == null || data.getLastName().isBlank()) {
            throw new BadRequestException("lastName cannot be blank");
        }
        if (data.getEmail() == null || data.getEmail().isBlank()) {
            throw new BadRequestException("email cannot be blank");
        }
        if (data.getMobileNumber() == null || data.getMobileNumber().isBlank()) {
            throw new BadRequestException("mobileNumber cannot be blank");
        }
        if (data.getAddress() == null || data.getAddress().isBlank()) {
            throw new BadRequestException("address cannot be blank");
        }
        if (data.getContractType() == null) {
            throw new BadRequestException("contractType is required");
        }
        if (data.getEmploymentStatus() == null) {
            throw new BadRequestException("employmentStatus is required");
        }
        if (data.getStartDate() == null) {
            throw new BadRequestException("startDate is required");
        }
        if (data.getHoursPerWeek() == null || data.getHoursPerWeek() < 1 || data.getHoursPerWeek() > 168) {
            throw new BadRequestException("hoursPerWeek must be between 1 and 168");
        }


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
