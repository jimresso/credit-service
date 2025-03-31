package com.nttdata.creditproducts.service.controller;

import com.nttdata.creditproducts.service.service.CreditCardService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.CreditCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class CreditCardControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CreditCardService creditCardService;

    private static final String BASE_URL = "/credit-cards";

    @Test
    void createCreditCard_Success() {
        CreditCard creditCard = new CreditCard();
        creditCard.setId("123");
        creditCard.setCustomerId("456");
        creditCard.setBalance(BigDecimal.valueOf(5000));
        when(creditCardService.createCreditCard(any()))
                .thenReturn(Mono.just(ResponseEntity.status(HttpStatus.CREATED).body(creditCard))); // Sin defer()
        webTestClient.post().uri(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(creditCard) // Usa `bodyValue()` en lugar de `body(Mono.just(...))`
                .exchange()
                .expectStatus().isCreated() // 201 CREATED
                .expectBody(CreditCard.class)
                .value(response -> Assertions.assertEquals("123", response.getId()));
    }
}
