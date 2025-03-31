package com.nttdata.creditproducts.service.service;


import org.openapitools.model.Transaction;
import org.openapitools.model.TransactionRequest;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface TransactionService {
    Mono<ResponseEntity<Transaction>> consume(TransactionRequest transactionRequest);
}
