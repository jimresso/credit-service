package com.nttdata.creditproducts.service.service;

import org.openapitools.model.ReportOperationsResponse;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface ReportCreditCardService {
    Mono<ResponseEntity<ReportOperationsResponse>> findallOperation(String dni);
}