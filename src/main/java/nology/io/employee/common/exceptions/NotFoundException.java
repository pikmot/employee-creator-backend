package nology.io.employee.common.exceptions;

public class NotFoundException extends RuntimeException{

    public NotFoundException(String message){
        super(message);
    }
    
}
