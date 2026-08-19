package nology.io.employee.employee;

import org.springframework.data.jpa.repository.JpaRepository;

import nology.io.employee.employee.entities.Employee;

public interface EmployeeRepository extends JpaRepository<Employee,Long>{
    
}
