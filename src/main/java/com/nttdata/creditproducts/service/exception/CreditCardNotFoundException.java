package com.nttdata.creditproducts.service.exception;

public class CreditCardNotFoundException extends RuntimeException {
    public CreditCardNotFoundException(String message) {
        super(message);
    }
}