package cn.tendata.mdcs.admin.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.tendata.mdcs.admin.web.model.ValidationErrorDto;

@ControllerAdvice(basePackageClasses = IndexController.class)
public class RestExceptionController {

	@Autowired
	MessageSource message;
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
	public ResponseEntity<ValidationErrorDto> handleValidationError(MethodArgumentNotValidException ex){
		BindingResult result = ex.getBindingResult();
		
		ValidationErrorDto fieldErrors = new ValidationErrorDto();
        List<FieldError> fieldErrorsList = result.getFieldErrors();
        for (FieldError error: fieldErrorsList) {
        	String messageKey = error.getObjectName() + '.' + error.getField();
        	fieldErrors.addFieldError(messageKey, message.getMessage(messageKey, null, null)); 
        }
        
        List<ObjectError> globalErrorsList = result.getGlobalErrors();
        for (ObjectError error: globalErrorsList) {
        	String messageKey = error.getCode();
        	fieldErrors.addFieldError(messageKey, error.getDefaultMessage()); 
        }
        
        return new ResponseEntity<ValidationErrorDto>(fieldErrors, HttpStatus.BAD_REQUEST);
	}
}
