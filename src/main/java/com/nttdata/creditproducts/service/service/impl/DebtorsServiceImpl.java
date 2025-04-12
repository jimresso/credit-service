package com.nttdata.creditproducts.service.service.impl;

import com.nttdata.creditproducts.service.exception.CreditCardNotFoundException;
import com.nttdata.creditproducts.service.mapper.DebtorsMapper;
import com.nttdata.creditproducts.service.repository.CreditCardRepository;
import com.nttdata.creditproducts.service.repository.DebtorsRepository;
import com.nttdata.creditproducts.service.service.DebtorsService;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.DebtorsRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DebtorsServiceImpl implements DebtorsService {
    private final DebtorsRepository debtorsRepository;
    private final CreditCardRepository creditCardRepository;
    private final DebtorsMapper debtorsMapper;
    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);
    @Override
    public Mono<ResponseEntity<Void>> saveDebt(DebtorsRequest c) {
        String customerId = c.getCustomerId();
        String cardNumber = c.getCardNumber();
        logger.info("Starting process to save debt for customerId: {}", customerId);
        return creditCardRepository.findByCardNumber(cardNumber)
                .switchIfEmpty(Mono.error(
                        new CreditCardNotFoundException(
                                "No card found for customerId: " + customerId)))
                .flatMap(card -> {
                    logger.info("Credit card found for customerId: {}", customerId);
                    return debtorsRepository.save(debtorsMapper.toEntity(c))
                            .doOnSuccess(debtor -> logger.info("Debt saved for customerId: {}", customerId))
                            .then(Mono.just(ResponseEntity.ok().<Void>build()))
                            .onErrorResume(e -> {
                                logger.error("Error occurred while saving debt: {}", e.getMessage());
                                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                            });
                })
                .onErrorResume(e -> {
                    logger.error("Error occurred while finding credit " +
                            "card for customerId: {}. Error: {}", customerId, e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }
    }

