package com.nttdata.creditproducts.service.controller;

import com.nttdata.creditproducts.service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.openapitools.api.PaymentsApi;
import org.openapitools.model.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class PaymentController implements PaymentsApi {
    private final PaymentService paymentService;
    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Override
    public Mono<ResponseEntity<Payment>> registerPayment(Mono<Payment> payment, ServerWebExchange exchange) {
        logger.info("Starting registerPayment");
        return payment.flatMap(paymentService::create);
    }

}
