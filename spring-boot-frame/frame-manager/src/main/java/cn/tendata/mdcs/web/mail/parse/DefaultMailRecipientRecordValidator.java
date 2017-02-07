package cn.tendata.mdcs.web.mail.parse;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

public class DefaultMailRecipientRecordValidator implements MailRecipientRecordValidator {
    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Override
    public boolean validate(MailRecipientRecord record) {
        boolean valid = true;
        Set<ConstraintViolation<MailRecipientRecord>> constraintViolations = validator.validate(record);
        if (constraintViolations.size() > 0) {
            valid = false;
        }
        return valid;
    }
}
