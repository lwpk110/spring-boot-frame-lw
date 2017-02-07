package cn.tendata.mdcs.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.tendata.mdcs.data.domain.User;
import cn.tendata.mdcs.data.domain.UserMailRecipientGroup;
import cn.tendata.mdcs.service.UserMailRecipientGroupService;
import cn.tendata.mdcs.service.UserService;
import cn.tendata.mdcs.web.model.MailSouRecipientsDto;
import cn.tendata.mdcs.web.validation.MailSouRequestTokenValidator;

@Controller
@RequestMapping("/api")
public class ApiController {

    private final UserService userService;
    private final UserMailRecipientGroupService userMailRecipientGroupService;

    @Value("${mailsou.token.secret:123456}")
    private String tokenSecret;
    
    @Autowired
    public ApiController(UserService userService, UserMailRecipientGroupService userMailRecipientGroupService) {
        this.userService = userService;
        this.userMailRecipientGroupService = userMailRecipientGroupService;
    }
    
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(new MailSouRequestTokenValidator(tokenSecret));
    }
    
    @RequestMapping(value = "/mailsou/recipients/import", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> importMailSouRecipients(@Validated @RequestBody MailSouRecipientsDto recipientsDto, 
            final Errors errors){
        if(errors.hasErrors()){
            return new ResponseEntity<>(new SimpleResult("F", createErrorList(errors)), HttpStatus.BAD_REQUEST);
        }
        User user = userService.findById(recipientsDto.getUserId());
        if(user == null){
            user = new User(recipientsDto.getUserId(), recipientsDto.getUsername());
            userService.save(user);
        }
        UserMailRecipientGroup recipientGroup = new UserMailRecipientGroup(recipientsDto.getName(), 
                UserMailRecipientGroup.Sources.MAIL_SOU, recipientsDto.getRecipients(), user);
        userMailRecipientGroupService.save(recipientGroup);
        return new ResponseEntity<>(new SimpleResult("T"), HttpStatus.OK);
    }
    
    private List<String> createErrorList(Errors errors){
        List<String> errorList = new ArrayList<>();
        for (FieldError error: errors.getFieldErrors()) {
            errorList.add(error.getField() + ", " + error.getCode());
        }
        for (ObjectError error: errors.getGlobalErrors()) {
            errorList.add(error.getCode());
        }
        return errorList;
    }
    
    public static class SimpleResult {
        
        private final String success;
        private final Object result;
        private final List<String> errors;
        
        private SimpleResult(String success){
            this(success, null);
        }
        
        private SimpleResult(String success, List<String> errors){
            this(success, null, errors);
        }
        
        private SimpleResult(String success, Object result, List<String> errors) {
            this.success = success;
            this.result = result;
            this.errors = errors;
        }

        public String getSuccess() {
            return success;
        }
        
        public Object getResult() {
            return result;
        }

        public List<String> getErrors() {
            return errors;
        }
    }
}
