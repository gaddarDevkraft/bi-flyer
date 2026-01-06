package com.example.bi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse <T>{
    private T data;
    private Integer status;
    private String message;

    public BaseResponse() {
        this.data = null;
        this.status = HttpStatus.BAD_REQUEST.value();
        this.message = HttpStatus.BAD_REQUEST.getReasonPhrase();
    }

    public BaseResponse(T data) {
        this();
        this.data = data;
        this.status = HttpStatus.OK.value();
        this.message = "Success";
    }

    public BaseResponse(T data, Integer status, String message) {
        this(data);
        this.status = status;
        this.message = message;
    }
}
