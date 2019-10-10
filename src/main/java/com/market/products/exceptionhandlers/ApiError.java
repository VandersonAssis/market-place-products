package com.market.products.exceptionhandlers;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Data
class ApiError {
    private HttpStatus status;
    private String message;
    private List<String> errors;

    ApiError(HttpStatus status, String message, String error) {
        super();
        this.status = status;
        this.message = message;
        errors = Collections.singletonList(error);
    }

    ApiError(HttpStatus status, String error) {
        super();
        this.status = status;
        errors = Collections.singletonList(error);
    }
}
