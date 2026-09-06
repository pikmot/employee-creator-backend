package nology.io.employee.employee;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import nology.io.employee.common.exceptions.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import nology.io.employee.employee.dtos.CreateEmployeeRequest;
import nology.io.employee.employee.dtos.UpdateEmployeeRequest;
import nology.io.employee.employee.entities.Employee;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository repo;

    @Mock
    private ModelMapper mapper;

    @Spy
    @InjectMocks
    private EmployeeService employeeService;

    @Test
    public void findAll_CallsFindAllWithEmployees(){

        this.employeeService.findAll();
        verify(this.repo).findAll();
    }

    @Test
    public void findById_CallFindByIdWithCorrectArg(){
        this.employeeService.findById(1L);
        verify(this.repo).findById(1L);
    }

    @Test
    public void create_WhenEmployeeList_SavesEmployeeInDB(){

        //arrange
        CreateEmployeeRequest dto = new CreateEmployeeRequest();
        dto.setFirstName("John");
        dto.setMiddleName("Michael");
        dto.setLastName("Smith");
        dto.setEmail("john.smith@example.com");
        dto.setMobileNumber("0412345678");
        dto.setAddress("123 Example St, Sydney");
        dto.setContractType(ContractType.PERMANENT);
        dto.setEmploymentStatus(EmploymentStatus.FULL_TIME);
        dto.setStartDate(LocalDate.of(2023, 1, 15));
        dto.setFinishDate(null);
        dto.setOnGoing(true);
        dto.setHoursPerWeek(38);

        Employee fakeEmployee = new Employee();
        fakeEmployee.setFirstName("John");
        fakeEmployee.setMiddleName("Michael");
        fakeEmployee.setLastName("Smith");
        fakeEmployee.setEmail("john.smith@example.com");
        fakeEmployee.setMobileNumber("0412345678");
        fakeEmployee.setAddress("123 Example St, Sydney");
        fakeEmployee.setContractType(ContractType.PERMANENT);
        fakeEmployee.setEmploymentStatus(EmploymentStatus.FULL_TIME);
        fakeEmployee.setStartDate(LocalDate.of(2023, 1, 15));
        fakeEmployee.setFinishDate(null);
        fakeEmployee.setOnGoing(true);
        fakeEmployee.setHoursPerWeek(38);

        when(this.mapper.map(dto,Employee.class)).thenReturn(fakeEmployee);

        //act
        this.employeeService.create(dto);

        verify(this.repo).saveAndFlush(fakeEmployee);
    }

    //could break test into individual test as first badException negates the rest
    @Test
    public void create_WhenEmployeeBodyInvalid_ThrowsException(){

        CreateEmployeeRequest dto = new CreateEmployeeRequest();
        dto.setFirstName("");
        dto.setMiddleName("");
        dto.setLastName("");
        dto.setEmail("");
        dto.setMobileNumber("");
        dto.setAddress("");
        dto.setContractType(null);
        dto.setEmploymentStatus(null);
        dto.setStartDate(null);
        dto.setFinishDate(null);
        dto.setOnGoing(true);
        dto.setHoursPerWeek(0);

        Employee fakeEmployee = new Employee();
        fakeEmployee.setFirstName("");
        fakeEmployee.setMiddleName("Michael");
        fakeEmployee.setLastName("");
        fakeEmployee.setEmail("");
        fakeEmployee.setMobileNumber("");
        fakeEmployee.setAddress("");
        fakeEmployee.setContractType(null);
        fakeEmployee.setEmploymentStatus(null);
        fakeEmployee.setStartDate(null);
        fakeEmployee.setFinishDate(null);
        fakeEmployee.setOnGoing(true);
        fakeEmployee.setHoursPerWeek(0);

        //assert
        assertThrows(BadRequestException.class, () -> this.employeeService.create(dto));
        verify(this.repo, never()).saveAndFlush(fakeEmployee);

    }

    @Test
    public void delete_WhenEmployeeExists_DeletesFromDBReturnsTrue(){

        Employee fakeEmployee = new Employee();
        fakeEmployee.setFirstName("John");
        fakeEmployee.setMiddleName("Michael");
        fakeEmployee.setLastName("Smith");
        fakeEmployee.setEmail("john.smith@example.com");
        fakeEmployee.setMobileNumber("0412345678");
        fakeEmployee.setAddress("123 Example St, Sydney");
        fakeEmployee.setContractType(ContractType.PERMANENT);
        fakeEmployee.setEmploymentStatus(EmploymentStatus.FULL_TIME);
        fakeEmployee.setStartDate(LocalDate.of(2023, 1, 15));
        fakeEmployee.setFinishDate(null);
        fakeEmployee.setOnGoing(true);
        fakeEmployee.setHoursPerWeek(38);

        when(this.repo.findById(anyLong())).thenReturn(Optional.of(fakeEmployee));

        //act
        boolean result = this.employeeService.deleteById(1L);

        //assert
        verify(this.employeeService).findById(1L);
        verify(this.repo).delete(fakeEmployee);
        assertTrue(result);

    }

    @Test
    public void delete_WhenEmployeeDoesNotExist_DoesNotCallDeleteReturnsFalse(){

        //act
        boolean result = this.employeeService.deleteById(1L);

        //assert
        verify(this.employeeService).findById(1L);
        verify(this.repo, never()).delete(any(Employee.class));
        assertFalse(result);
    }

    @Test
    public void updateEmployeeById_whenEmployeeDoesNotExist_DoesNotSaveBook(){

        when(this.repo.findById(anyLong())).thenReturn(Optional.empty());
        UpdateEmployeeRequest dto = new UpdateEmployeeRequest();

        //act
        this.employeeService.updateById(1L,dto);

        verify(this.mapper, never()).map(dto,new Employee());
        verify(this.repo, never()).saveAndFlush(any(Employee.class));
    }

    @Test
    public void updateEmployeeById_whenEmployeeDoesExist_SavesBook(){

        //arrange
        UpdateEmployeeRequest dto = new UpdateEmployeeRequest();
        dto.setFirstName("John");


        Employee fakEmployee = new Employee();
        fakEmployee.setFirstName("John");

        when(this.repo.findById(1L)).thenReturn(Optional.of(fakEmployee));

        //act
        Optional<Employee> result = this.employeeService.updateById(1L, dto);

        assertTrue(result.isPresent());
        verify(this.mapper).map(dto,fakEmployee);
        verify(this.repo).saveAndFlush(fakEmployee);

    }
    
}
