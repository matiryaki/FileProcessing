package com.etstur.FileProcessing.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.error.ErrorAttributeOptions.Include;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@RestController
@ControllerAdvice
public class ErrorHandler implements ErrorController {

    @Autowired
    private ErrorAttributes errorAttributes;


    @RequestMapping("/error")
    public ErrorMessage handleError(WebRequest webRequest) {

        Map<String, Object> attributes = this.errorAttributes.getErrorAttributes(webRequest
                ,ErrorAttributeOptions.of(Include.MESSAGE,Include.BINDING_ERRORS));
        String message =(String) attributes.get("message");
        String path =(String) attributes.get("path");
        int status =(int) attributes.get("status");
        ErrorMessage err=new ErrorMessage(status, message, path);

        Map<String,String> validationErrors=new HashMap<String, String>();

        if(attributes.get("errors")!=null) {
            @SuppressWarnings("unchecked")
            List<FieldError> errors=(List<FieldError>) attributes.get("errors");

            for(FieldError fieldError : errors) {
                validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            err.setValidationErrors(validationErrors);
        }
        return err;

    }



}