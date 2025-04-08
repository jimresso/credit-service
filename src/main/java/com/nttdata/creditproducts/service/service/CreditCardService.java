package com.nttdata.creditproducts.service.service;

import org.openapitools.model.CardRequest;
import org.openapitools.model.CardResponse;
import org.openapitools.model.CreditCard;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;


public interface CreditCardService {
    Mono<ResponseEntity<CreditCard>> createCreditCard(CreditCard credit);
    Mono<ResponseEntity<CardResponse>>validCreditCard(CardRequest cardRequest);
}