package cn.tendata.mdcs.web.validation;

import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.DigestUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import cn.tendata.mdcs.web.model.MailSouRecipientsDto;

public class MailSouRequestTokenValidator implements Validator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailSouRequestTokenValidator.class);
    
    private final String tokenSecret;
    
    public MailSouRequestTokenValidator(String tokenSecret) {
        Assert.notNull(tokenSecret, "tokenSecret must not be null");
        
        this.tokenSecret = tokenSecret;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return MailSouRecipientsDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MailSouRecipientsDto recipientsDto = (MailSouRecipientsDto) target;
        LOGGER.debug("Validating mailsou request, user:{}[{}], token:{}, name:{}.", 
                recipientsDto.getUsername(), recipientsDto.getUserId(), recipientsDto.getToken(), recipientsDto.getName());
        String tempToken = DigestUtils.md5DigestAsHex(
                String.format("%s:%s@%s", recipientsDto.getUserId(), recipientsDto.getUsername(), tokenSecret).getBytes(StandardCharsets.UTF_8));
        if(!tempToken.equalsIgnoreCase(recipientsDto.getToken())){
            errors.rejectValue("token", "TokenNotValid");
        }
    }
}
