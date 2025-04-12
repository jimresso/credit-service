package com.nttdata.creditproducts.service.controller;

import com.nttdata.creditproducts.service.service.DebtorsService;
import lombok.RequiredArgsConstructor;
import org.openapitools.api.DebtorsApi;
import org.openapitools.model.DebtorsRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class DebtorsController implements DebtorsApi {
    private final DebtorsService debtorsService;
    @Override
    public Mono<ResponseEntity<Void>> debtors(Mono<DebtorsRequest> debtorsRequest, ServerWebExchange exchange) {
        return debtorsRequest.flatMap(debtorsService::saveDebt);
    }
}
