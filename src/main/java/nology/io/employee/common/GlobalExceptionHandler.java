package nology.io.employee.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException.UnprocessableContent;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.servlet.http.HttpServletRequest;
import nology.io.employee.common.dtos.ApiErrorResponse;
import nology.io.employee.common.exceptions.BadRequestException;
import nology.io.employee.common.exceptions.NotFoundException;
import nology.io.employee.common.exceptions.UnprocessableContentException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleMissingBodyException (HttpMessageNotReadableException ex, HttpServletRequest req){
        ApiErrorResponse response = ApiErrorResponse.of(HttpStatus.BAD_REQUEST, ex.getMessage(), req.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFoundException (NotFoundException ex, HttpServletRequest req){
        ApiErrorResponse response = ApiErrorResponse.of(HttpStatus.NOT_FOUND, ex.getMessage(), req.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentTypeMismatchException (MethodArgumentTypeMismatchException ex, HttpServletRequest req){
        ApiErrorResponse response = ApiErrorResponse.of(HttpStatus.BAD_REQUEST, ex.getMessage(), req.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNoResourceFoundException (NoResourceFoundException ex, HttpServletRequest req){
        ApiErrorResponse response = ApiErrorResponse.of(HttpStatus.NOT_FOUND, ex.getMessage(), req.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException (MethodArgumentNotValidException ex, HttpServletRequest req){
        ApiErrorResponse response = ApiErrorResponse.of(HttpStatus.BAD_REQUEST, ex.getMessage(), req.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiErrorResponse> handleBadRequestException(BadRequestException ex, HttpServletRequest req){
        ApiErrorResponse response = ApiErrorResponse.of(HttpStatus.BAD_REQUEST, ex.getMessage(), req.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnprocessableContent.class)
    public ResponseEntity<ApiErrorResponse> handleUnprocessableContentException(UnprocessableContentException ex, HttpServletRequest req){
        ApiErrorResponse response = ApiErrorResponse.of(HttpStatus.UNPROCESSABLE_CONTENT, ex.getMessage(), req.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_CONTENT);
    }


    
}
