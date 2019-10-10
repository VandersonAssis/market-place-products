package com.market.products.exceptionhandlers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class ExceptionHandlers extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new ApiError(BAD_REQUEST, error.toString())).collect(Collectors.toSet()), BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(new ApiError(BAD_REQUEST, "Malformed request", ex.getMessage()), BAD_REQUEST);
    }

    @ExceptionHandler(value = { RuntimeException.class } )
    protected  ResponseEntity<Object> handleInternalServerErrorException(RuntimeException ex, WebRequest request) {
        String message = "Internal server error. A more meaningful log has been forwarded to our team.";
        return handleExceptionInternal(ex, message, new HttpHeaders(), INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class })
    protected ResponseEntity<Object> handleConflictException(RuntimeException ex, WebRequest request) {
        String message = "Conflict exception happened. This register is already in our database.";
        return handleExceptionInternal(ex, message, new HttpHeaders(), CONFLICT, request);
    }
}
