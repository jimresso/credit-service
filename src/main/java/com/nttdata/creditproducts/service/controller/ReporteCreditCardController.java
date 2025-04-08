package com.nttdata.creditproducts.service.controller;

import com.nttdata.creditproducts.service.service.ReportCreditCardService;
import lombok.RequiredArgsConstructor;
import org.openapitools.api.ReportApi;
import org.openapitools.model.ReportOperationsRequest;
import org.openapitools.model.ReportOperationsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class ReporteCreditCardController implements ReportApi {
    private static final Logger logger = LoggerFactory.getLogger(ReporteCreditCardController.class);
    private final ReportCreditCardService reportCreditCardService;

    @Override
    public Mono<ResponseEntity<ReportOperationsResponse>> reportOperations(
            Mono<ReportOperationsRequest> reportOperationsRequest, ServerWebExchange exchange) {
        return reportOperationsRequest.flatMap(
                c -> reportCreditCardService.findallOperation(c.getDni()));
    }

}
