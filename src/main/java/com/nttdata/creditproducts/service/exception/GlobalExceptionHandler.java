package com.nttdata.creditproducts.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CreditCardNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleCreditCardNotFound(CreditCardNotFoundException ex) {
        return Collections.singletonMap("error", ex.getMessage());
    }

    @ExceptionHandler(DuplicateCreditCardException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleDuplicateCreditCard(DuplicateCreditCardException ex) {
        return Collections.singletonMap("error", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleGenericException(Exception ex) {
        return Collections.singletonMap("error", "An unexpected error occurred");
    }
    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<String> handleInsufficientFundsException(InsufficientFundsException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}