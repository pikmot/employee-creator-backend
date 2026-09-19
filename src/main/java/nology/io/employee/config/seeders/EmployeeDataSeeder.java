package nology.io.employee.config.seeders;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;

import io.micrometer.common.lang.internal.Contract;
import nology.io.employee.employee.ContractType;
import nology.io.employee.employee.EmployeeRepository;
import nology.io.employee.employee.EmploymentStatus;
import nology.io.employee.employee.entities.Employee;

@Component
@Profile({"dev"})
public class EmployeeDataSeeder implements CommandLineRunner {

    private final EmployeeRepository repo;

    private final Faker faker = new Faker();


    public EmployeeDataSeeder(EmployeeRepository repo){
        this.repo = repo;
    }

    @Override
    public void run (String... args) throws Exception{

        if (repo.count() == 0) {

            List<Employee> employeeList = new ArrayList<>();

            for (int i = 0; i < 20; i++){

                String randomFirstName = faker.name().firstName();
                String randomMiddleName = (Math.random() > 0.5) ? faker.name().firstName() : null;
                String randomLastName = faker.name().lastName();
                String randomEmail = faker.internet().emailAddress();
                String randomMobileNumber = faker.phoneNumber().cellPhone();
                String randomAddress = faker.address().toString();

                ContractType randomContractType = faker.options().option(ContractType.class);
                EmploymentStatus randomEmploymentStatus = faker.options().option(EmploymentStatus.class);

                LocalDate randomStartDate = faker.date()
                .past(26 * 365, TimeUnit.DAYS)
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

                LocalDate randomFinishDate = (Math.random() > 0.5) ?  faker.date()
                .between(Date.from(randomStartDate.atStartOfDay(ZoneId.systemDefault()).toInstant()),new Date())
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate() : null;

                boolean randomIsOnGoing = false;

                if (randomFinishDate == null){randomIsOnGoing = true;}

                Employee createdEmployee = new Employee();

                createdEmployee.setFirstName(randomFirstName);
                createdEmployee.setMiddleName(randomMiddleName);
                createdEmployee.setLastName(randomLastName);
                createdEmployee.setEmail(randomEmail);
                createdEmployee.setMobileNumber(randomMobileNumber);
                createdEmployee.setAddress(randomAddress);
                createdEmployee.setContractType(randomContractType);
                createdEmployee.setEmploymentStatus(randomEmploymentStatus);
                createdEmployee.setStartDate(randomStartDate);
                createdEmployee.setFinishDate(randomFinishDate);
                createdEmployee.setOnGoing(randomIsOnGoing);

                employeeList.add(createdEmployee);

            }

            repo.saveAllAndFlush(employeeList);

        }

    }
    
}
