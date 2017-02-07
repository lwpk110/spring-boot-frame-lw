package cn.tendata.mdcs.web.controller;

import cn.tendata.mdcs.core.BasicErrorCodeException;
import cn.tendata.mdcs.core.DefaultMessageSource;
import cn.tendata.mdcs.web.mail.parse.EmptyUploadRecordSizeException;
import cn.tendata.mdcs.web.mail.parse.InvalidUploadExtensionException;
import cn.tendata.mdcs.web.mail.parse.MaxUploadRecordSizeExceededException;
import cn.tendata.mdcs.web.mail.parse.MaxUploadSizeExceededException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice(basePackageClasses = HomeController.class)
public class GlobalControllerExceptionHandler implements MessageSourceAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    private MessageSourceAccessor messages = DefaultMessageSource.getAccessor();

    @ExceptionHandler({
            MaxUploadRecordSizeExceededException.class,
            EmptyUploadRecordSizeException.class,
            InvalidUploadExtensionException.class,
            MaxUploadSizeExceededException.class})
    @ResponseBody
    public String handleUploadException(BasicErrorCodeException ex) throws IOException {
        Map<String, Object> errorAttributes = new LinkedHashMap<>();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        errorAttributes.put("status", status.value());
        errorAttributes.put("error", status.getReasonPhrase());
        errorAttributes.put("message", messages.getMessage("error." + ex.getErrorCode(), ex.getMessage()));
        return new ObjectMapper().writeValueAsString(errorAttributes);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ResponseEntity<?> handleValidationException(Exception ex) {
        BindingResult result = null;
        if (ex instanceof MethodArgumentNotValidException) {
            result = ((MethodArgumentNotValidException) ex).getBindingResult();
        }
        if (ex instanceof BindException) {
            result = ((BindException) ex).getBindingResult();
        }
        Map<String, Object> errorAttributes = new LinkedHashMap<String, Object>();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        errorAttributes.put("status", status.value());
        errorAttributes.put("error", status.getReasonPhrase());
        List<Error> errors = new ArrayList<>(6);
        for (ObjectError err : result.getGlobalErrors()) {
            Error error = new Error();
            error.message = messages.getMessage(err.getCode(), err.getDefaultMessage());
            errors.add(error);
        }
        for (FieldError err : result.getFieldErrors()) {
            Error error = new Error();
            error.field = err.getField();
            error.rejected = err.getRejectedValue();
            error.message = messages.getMessage(err.getCode(), err.getDefaultMessage());
            errors.add(error);
        }
        errorAttributes.put("errors", errors);
        errorAttributes.put("message", messages.getMessage("error.VALIDATION_ERROR", "Validation failed for object='"
                + result.getObjectName() + "'. Error count: " + result.getErrorCount()));
        return new ResponseEntity<>(errorAttributes, status);
    }

    @JsonInclude(Include.NON_EMPTY)
    static class Error {
        public String field;
        public Object rejected;
        public String message;
    }

    @ExceptionHandler(BasicErrorCodeException.class)
    public void handleErrorCodeException(BasicErrorCodeException ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        logException(ex, request, status);
        String message = messages.getMessage("error." + ex.getErrorCode(), ex.getMessage());
        response.sendError(status.value(), message);
    }

    @ExceptionHandler(Exception.class)
    public void handleUncaughtException(Exception ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        logException(ex, request, status);
        String message = messages.getMessage("error.INTERNAL_SERVER_ERROR", "Server error");
        response.sendError(status.value(), message);
    }

    private void logException(Exception ex, HttpServletRequest request, HttpStatus status) {
        if (LOGGER.isErrorEnabled() && status.value() >= 500 || LOGGER.isInfoEnabled()) {
            Marker marker = MarkerFactory.getMarker(ex.getClass().getName());
            String uri = request.getRequestURI();
            if (request.getQueryString() != null) {
                uri += '?' + request.getQueryString();
            }
            String msg = String.format("%s %s ~> %s", request.getMethod(), uri, status);
            if (status.value() >= 500) {
                LOGGER.error(marker, msg, ex);
            } else if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(marker, msg, ex);
            } else {
                LOGGER.info(marker, msg);
            }
        }
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }
}
