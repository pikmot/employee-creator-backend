package nology.io.employee.config.seeders;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import nology.io.employee.employee.EmployeeRepository;

@Component
@Profile({"dev"})
public class EmployeeDataSeeder implements CommandLineRunner {

    private final EmployeeRepository repo;


    public EmployeeDataSeeder(EmployeeRepository repo){
        this.repo = repo;
    }

    @Override
    public void run (String... args) throws Exception{

        

    }
    
}
