package com.etstur.FileProcessing.exception;

import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorMessage {
    private int status;
    private String message;
    private String path;
    private long timestamp=new Date().getTime();


    private Map<String,String> validationErrors;

    public ErrorMessage(int status, String message, String path) {
        this.status = status;
        this.message = message;
        this.path = path;
    }
}
