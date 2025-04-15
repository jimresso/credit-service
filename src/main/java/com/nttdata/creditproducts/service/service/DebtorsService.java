package com.nttdata.creditproducts.service.service;

import org.openapitools.model.CheckDebtorsRequest;
import org.openapitools.model.DebtorsRequest;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface DebtorsService {
    Mono<ResponseEntity<Void>> saveDebt(DebtorsRequest c);
    Mono< ResponseEntity<Boolean>> checkDebts(CheckDebtorsRequest checkDebtorsRequest);
}
