package cn.tendata.mdcs.admin.web.model;

import java.util.ArrayList;
import java.util.List;

public class ValidationErrorDto {

	private List<FieldErrorDto> fieldErrors = new ArrayList<FieldErrorDto>();

    public ValidationErrorDto() { }

    public void addFieldError(String field, String message) {
    	FieldErrorDto error = new FieldErrorDto(field, message);
        fieldErrors.add(error);
    }
    
    public void addFieldError(FieldErrorDto error) {
        fieldErrors.add(error);
    }

    public List<FieldErrorDto> getFieldErrors() {
        return fieldErrors;
    }
}
