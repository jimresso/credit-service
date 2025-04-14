package com.nttdata.creditproducts.service.service.impl;


import com.nttdata.creditproducts.service.configure.CreditProperties;
import com.nttdata.creditproducts.service.exception.BusinessException;
import com.nttdata.creditproducts.service.exception.InternalServerErrorException;
import com.nttdata.creditproducts.service.exception.RemoteServiceUnavailableException;
import com.nttdata.creditproducts.service.mapper.CreditCardMapper;
import com.nttdata.creditproducts.service.model.Account;
import com.nttdata.creditproducts.service.notifications.FallbackNotifier;
import com.nttdata.creditproducts.service.repository.CreditCardRepository;
import com.nttdata.creditproducts.service.repository.DebtorsRepository;
import com.nttdata.creditproducts.service.service.CreditCardService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.CardRequest;
import org.openapitools.model.CardResponse;
import org.openapitools.model.CredicardProductRequest;
import org.openapitools.model.CreditCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CreditCardServiceImpl implements CreditCardService {

    private final CreditCardRepository creditCardRepository;
    private final CreditCardMapper creditCardMapper;
    private final DebtorsRepository debtorsRepository;
    private static final Logger logger = LoggerFactory.getLogger(CreditCardServiceImpl.class);
    private final WebClient.Builder webClientBuilder;
    private final FallbackNotifier fallbackNotifier;
    private final CreditProperties creditProperties;


    @Override
    @CircuitBreaker(name = "circuitBreakerCreditCard", fallbackMethod = "fallbackCreateCreditCard")
    public Mono<ResponseEntity<CreditCard>> createCreditCard(CreditCard credit) {
        try {
            validateCreditCardFields(credit);
        } catch (BusinessException e) {
            return Mono.error(e);
        }
        WebClient webClient = webClientBuilder.build();
        return webClient.get()
                .uri(creditProperties.getUri())
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, response -> {
                    logger.error("Account service responded 5xx");
                    return Mono.error(new RemoteServiceUnavailableException("Account service unavailable"));
                })
                .onStatus(HttpStatusCode::is4xxClientError, response -> {
                    logger.warn("Account service responded 4xx");
                    return Mono.error(new BusinessException("Error validating customer account"));
                })
                .bodyToMono(new ParameterizedTypeReference<List<Account>>() { })
                .onErrorMap(throwable -> {
                    Throwable root = throwable.getCause() != null ? throwable.getCause() : throwable;
                    if (root instanceof java.net.ConnectException || root instanceof java.net.UnknownHostException) {
                        return new RemoteServiceUnavailableException("Account service unreachable", root);
                    }
                    return throwable;
                })
                .flatMapMany(Flux::fromIterable)
                .filter(account -> account.getCustomerId().equals(credit.getCustomerId()))
                .next()
                .switchIfEmpty(Mono.defer(() -> {
                    logger.warn("Customer {} does not have an associated bank account", credit.getCustomerId());
                    return Mono.error(new BusinessException("Customer does not have a bank account"));
                }))
                .flatMap(account -> {
                    logger.info("Building credit for the customer {}", credit.getCustomerId());
                    if (credit.getTypeCard().equals(CreditCard.TypeCardEnum.DEBITO)) {
                        credit.setBalance(new BigDecimal(account.getBalance().toString()));
                        credit.setLimit(new BigDecimal("0"));
                    }
                    return creditCardRepository.save(creditCardMapper.toEntity(credit))
                            .map(creditCardMapper::toDto)
                            .map(savedCredit -> ResponseEntity
                                    .status(HttpStatus.CREATED)
                                    .body(savedCredit))
                            .doOnSuccess(savedCredit ->
                                    logger.info("Credit successfully created"))
                            .onErrorResume(e -> {
                                logger.error("Error saving credit card: {}", e.getMessage());
                                return Mono.error(new InternalServerErrorException("Could not save credit card", e));
                            });
                });
    }

    public Mono<ResponseEntity<Object>> fallbackCreateCreditCard(Throwable throwable) {
        logger.warn("Fallback activado en createCreditCard: {} - {}",
                throwable.getClass().getSimpleName(), throwable.getMessage());
        if (throwable instanceof BusinessException) {
            logger.info("Fallback invocado por BusinessException. No se envía correo.");
            return Mono.just(ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", throwable.getMessage())));
        }
        fallbackNotifier.sendFallbackEmail("CreditServiceImpl", throwable);
        return Mono.just(ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("error", "Service temporarily unavailable")));
    }

    @Override
    public Mono<ResponseEntity<CardResponse>> validCreditCard(CardRequest cardRequest) {
        return Flux.fromIterable(cardRequest.getCustomerId())
                .flatMap(creditCardRepository::findByCustomerId
                )
                .hasElements()
                .map(hasCreditCard -> {
                    CardResponse response = new CardResponse();
                    response.setCreditCard(hasCreditCard);
                        return ResponseEntity.ok(response);
                });
    }

    @Override
    public Mono<ResponseEntity<Flux<CreditCard>>> getAllProductUser(
            CredicardProductRequest credicardProductRequest) {
        return Mono.defer(() -> {
            Flux<CreditCard> flux = creditCardRepository
                    .findByDni(credicardProductRequest.getDni());

            return Mono.just(ResponseEntity.ok(flux));
        }).onErrorResume(e -> {
            logger.error("Error obtaining credit cards for the DNI: {}",
                    credicardProductRequest.getDni(), e);
            return Mono.just(ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Flux.empty()));
        });
    }
    private void validateCreditCardFields(CreditCard credit) {
        if (credit.getCustomerId() == null || credit.getCustomerId().isBlank()) {
            throw new BusinessException("Customer ID is required");
        }
        if (credit.getDni() == null || credit.getDni().isBlank()) {
            throw new BusinessException("DNI is required");
        }
        if (credit.getCardNumber() == null || credit.getCardNumber().isBlank()) {
            throw new BusinessException("Card number is required");
        }
        if (credit.getExpirationDate() == null) {
            throw new BusinessException("Expiration date is required");
        }
        if (credit.getTypeCard() == null) {
            throw new BusinessException("Card type is required");
        }

        if (credit.getStatus() == null) {
            throw new BusinessException("Card status is required");
        }

        if (credit.getBalance() == null) {
            throw new BusinessException("Card balance is required");
        }

        if (credit.getLimit() == null) {
            throw new BusinessException("Card limit is required");
        }
    }

}