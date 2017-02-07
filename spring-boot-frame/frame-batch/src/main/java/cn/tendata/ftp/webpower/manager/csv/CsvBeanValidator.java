package cn.tendata.ftp.webpower.manager.csv;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;
import org.springframework.beans.factory.InitializingBean;

/**
 * @param <T>
 */
public class CsvBeanValidator<T> implements Validator<T>, InitializingBean {
    private javax.validation.Validator validator;

    @Override
    public void afterPropertiesSet() throws Exception {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    @Override
    public void validate(T t) throws ValidationException {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(t);
        if (constraintViolations.size() > 0) {
            StringBuilder messge = new StringBuilder();
            int index = 0;
            for (ConstraintViolation<T> constraintViolation : constraintViolations) {
                messge.append("<"+index+">:Field:{"
                        + constraintViolation.getPropertyPath()
                        + "}"
                        + constraintViolation.getMessage()
                        + ";\n");
                index++;
            }
            throw new ValidationException(messge.toString());
        }
    }
}
