package com.nttdata.creditproducts.service.service.impl;

import com.nttdata.creditproducts.service.exception.BusinessException;
import com.nttdata.creditproducts.service.model.TransactionDTO;
import com.nttdata.creditproducts.service.repository.TransactionRepository;
import com.nttdata.creditproducts.service.service.ReportCreditCardService;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.ReportOperationsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.YearMonth;


@Service
@RequiredArgsConstructor
public class ReportCreditCardServiceImpl  implements ReportCreditCardService {
    private final TransactionRepository transactionRepository;
    private static final Logger logger = LoggerFactory.getLogger(ReportCreditCardServiceImpl.class);

    @Override
    public Mono<ResponseEntity<ReportOperationsResponse>> findallOperation(String dni) {
        YearMonth currentMonth = YearMonth.now();
        LocalDate startOfMonth = currentMonth.atDay(1);
        LocalDate endOfMonth = currentMonth.atEndOfMonth();
        return transactionRepository.findByDni(dni)
                .filter(transaction -> {
                    LocalDate txDate = transaction.getTransactionDate();
                    return !txDate.isBefore(startOfMonth) && !txDate.isAfter(endOfMonth);
                })
                .map(TransactionDTO::getAmount)
                .switchIfEmpty(Mono.error(new BusinessException("No transactions found for current month")))
                .reduce(0.0, Double::sum)
                .map(totalAmount -> {
                    double averageDailyBalance = totalAmount / currentMonth.lengthOfMonth();
                    ReportOperationsResponse response = new ReportOperationsResponse();
                    response.setDni(dni);
                    response.setAmount(averageDailyBalance);
                    response.setReportDate(LocalDate.now());
                    return ResponseEntity.ok(response);
                })
                .onErrorResume(ex -> {
                    if (ex instanceof BusinessException) {
                        logger.warn("Business error: {}", ex.getMessage());
                        return Mono.just(ResponseEntity.badRequest().build());
                    }
                    logger.error("Internal error generating the report: {}", ex.getMessage(), ex);
                    return Mono.just(ResponseEntity.internalServerError().build());
                });
    }
}
