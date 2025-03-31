package com.nttdata.creditproducts.service.controller;


import com.nttdata.creditproducts.service.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.openapitools.api.ApiApi;
import org.openapitools.model.Transaction;
import org.openapitools.model.TransactionRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class TransactionController implements ApiApi {
private final TransactionService transactionService;
    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @Override
    public Mono<ResponseEntity<Transaction>> consumeCreditCard(Mono<TransactionRequest>transactionRequest, ServerWebExchange exchange) {
        logger.info("Starting createCreditCard");
        return transactionRequest.flatMap(transactionService::consume);
    }

}
