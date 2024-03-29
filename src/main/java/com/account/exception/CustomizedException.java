package com.account.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/*@ControllerAdvice :to share this customised exception controller accross multiple classes
@RestController: this is a controller that will return a response
*/

@ControllerAdvice
@RestController
public class CustomizedException extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleExceptions(Exception ex, WebRequest request) {
	ExceptionResponse response=new ExceptionResponse(new Date(),ex.getMessage(),request.getDescription(false));
	
	return new ResponseEntity(response,HttpStatus.INTERNAL_SERVER_ERROR);
	}

	
	@ExceptionHandler(AccountNotFoundException.class)
	public final ResponseEntity<Object> handleExceptions(AccountNotFoundException ex, WebRequest request) {
	ExceptionResponse response=new ExceptionResponse(new Date(),ex.getMessage(),request.getDescription(false));
	
	return new ResponseEntity(response,HttpStatus.NOT_FOUND);
	}
}
