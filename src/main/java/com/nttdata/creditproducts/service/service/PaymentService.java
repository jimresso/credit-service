package com.nttdata.creditproducts.service.service;

import org.openapitools.model.Payment;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface PaymentService {
    Mono<ResponseEntity<Payment>> create(Payment payment);
}