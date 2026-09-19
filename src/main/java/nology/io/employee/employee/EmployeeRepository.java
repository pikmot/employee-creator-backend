package nology.io.employee.employee;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import nology.io.employee.employee.entities.Employee;

public interface EmployeeRepository extends JpaRepository<Employee,Long>{

    Page<Employee> findAll(Pageable pageable);
    
}
