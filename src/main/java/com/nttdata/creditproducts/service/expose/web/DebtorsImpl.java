package com.nttdata.creditproducts.service.expose.web;

import com.nttdata.creditproducts.service.service.DebtorsService;
import lombok.RequiredArgsConstructor;
import org.openapitools.api.DebtorsApi;
import org.openapitools.model.CheckDebtorsRequest;
import org.openapitools.model.DebtorsRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class DebtorsImpl implements DebtorsApi {
    private final DebtorsService debtorsService;
    private static final Logger logger = LoggerFactory.getLogger(DebtorsImpl.class);

    @Override
    public Mono<ResponseEntity<Boolean>> checkDebtors(
            Mono<CheckDebtorsRequest> checkDebtorsRequest, ServerWebExchange exchange) {
        logger.info("Starting checkDebtors");
        return checkDebtorsRequest.flatMap(debtorsService::checkDebts);
    }

    @Override
    public Mono<ResponseEntity<Void>> debtors(Mono<DebtorsRequest> debtorsRequest, ServerWebExchange exchange) {
        logger.info("Starting debtors");
        return debtorsRequest.flatMap(debtorsService::saveDebt);
    }
}
