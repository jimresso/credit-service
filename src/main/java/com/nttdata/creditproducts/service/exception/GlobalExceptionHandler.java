package com.nttdata.creditproducts.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebInputException;

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
        return Collections.singletonMap("error", "Unexpected error: " +
                ex.getClass().getName() + " - " + ex.getMessage());
    }

    @ExceptionHandler(ServerWebInputException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleWebInputError(org.springframework.web.server.ServerWebInputException ex) {
        Throwable cause = ex.getCause();
        if (cause instanceof IllegalArgumentException) {
            return Collections.singletonMap("error", "Invalid enum or argument: " + cause.getMessage());
        }
        if (cause != null) {
            return Collections.singletonMap("error", "Bad request: " + cause.getClass().getSimpleName() +
                    " - " + cause.getMessage());
        }
        return Collections.singletonMap("error", "Malformed request or bad input structure");
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<String> handleInsufficientFundsException(InsufficientFundsException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
    @ExceptionHandler(InternalServerErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleInternalServerError(InternalServerErrorException ex) {
        return Collections.singletonMap("error", ex.getMessage());
    }
    @ExceptionHandler(RemoteServiceUnavailableException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public Map<String, String> handleRemoteServiceDown(RemoteServiceUnavailableException ex) {
        return Collections.singletonMap("error", ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleDeserializationError(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();
        if (cause instanceof IllegalArgumentException) {
            return Collections.singletonMap("error", "Invalid value for enum field: " + cause.getMessage());
        }
        return Collections.singletonMap("error", "Malformed request body");
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleBusinessException(BusinessException ex) {
        return Collections.singletonMap("error", ex.getMessage());
    }
}