package com.nttdata.creditproducts.service.exception;

public class DuplicateCreditCardException extends RuntimeException {
    public DuplicateCreditCardException(String message) {
        super(message);
    }
}