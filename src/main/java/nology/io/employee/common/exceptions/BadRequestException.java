package nology.io.employee.common.exceptions;

public class BadRequestException extends RuntimeException{

    //400 error needs to be handled
    public BadRequestException(String message) {
        super(message);
    }

    
    
}


