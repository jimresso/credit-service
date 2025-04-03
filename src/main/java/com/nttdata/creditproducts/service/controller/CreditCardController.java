package com.nttdata.creditproducts.service.controller;

import com.nttdata.creditproducts.service.service.CreditCardService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.openapitools.api.CreditcardsApi;
import org.openapitools.model.CardRequest;
import org.openapitools.model.CardResponse;
import org.openapitools.model.CreditCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@RestController
@RequiredArgsConstructor
public class CreditCardController implements CreditcardsApi {
    private final CreditCardService creditCardService;
    private static final Logger logger = LoggerFactory.getLogger(CreditCardController.class);

    @Override
    public Mono<ResponseEntity<CreditCard>> createCreditCard(Mono<CreditCard> creditCard, ServerWebExchange exchange) {
        logger.info("Starting createCreditCard");
        return creditCard.flatMap(creditCardService::createCreditCard);
    }

    @Override
    public Mono<ResponseEntity<CardResponse>> hascreeditcard(Mono<CardRequest> cardRequest, ServerWebExchange exchange) {
        logger.info("Starting hascreeditcard");
        return cardRequest.flatMap(creditCardService::validCreditCard);
    }

}
