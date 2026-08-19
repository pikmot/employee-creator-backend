package nology.io.employee.employee.dtos;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import nology.io.employee.employee.ContractType;
import nology.io.employee.employee.EmploymentStatus;

public class UpdateEmployeeRequest {

    @Pattern(regexp = ".*\\S.*", message = "firstName Cannot be Empty")
    private String firstName;

    private String middleName;

    @Pattern(regexp = ".*\\S.*", message = "lastName Cannot be Empty")
    private String lastName;

    @Email
    private String email;

    @Pattern(regexp = ".*\\S.*", message = "mobileNumber Cannot be Empty")
    private String mobileNumber;

    @Pattern(regexp = ".*\\S.*", message = "address Cannot be Empty")
    private String address;
    
    private ContractType contractType;


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ContractType getContractType() {
        return contractType;
    }

    public void setContractType(ContractType contractType) {
        this.contractType = contractType;
    }

    public EmploymentStatus getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(EmploymentStatus employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public Integer getHoursPerWeek() {
        return hoursPerWeek;
    }

    public void setHoursPerWeek(Integer hoursPerWeek) {
        this.hoursPerWeek = hoursPerWeek;
    }

    private EmploymentStatus employmentStatus;


    private LocalDate startDate;

    @Min(1)
    @Max(168)
    private Integer hoursPerWeek;
    
}
