package com.nttdata.creditproducts.service.service.impl;


import com.nttdata.creditproducts.service.exception.BusinessException;
import com.nttdata.creditproducts.service.mapper.AccountMapper;
import com.nttdata.creditproducts.service.mapper.PaymentMapper;
import com.nttdata.creditproducts.service.model.Account;
import com.nttdata.creditproducts.service.model.AccountResponse;
import com.nttdata.creditproducts.service.repository.PaymentRepository;
import com.nttdata.creditproducts.service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final AccountMapper accountMapper;

    @Autowired
    private WebClient.Builder webClientBuilder;
    @Value("${account.service.uri.put}")
    private String accountsUri;
    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Override
    public Mono<ResponseEntity<Payment>> create(Payment payment) {
        WebClient webClient = webClientBuilder.build();
        return webClient.get()
                .uri(accountsUri)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Account>>() {})
                .flatMapMany(Flux::fromIterable)
                .filter(account -> account.getCustomerId().equals(payment.getCustomerId()))
                .next()
                .switchIfEmpty(Mono.error(new BusinessException("No account found for the customer")))
                .flatMap(account -> {
                    if (account.getBalance() < payment.getAmount().doubleValue()) {
                        return Mono.error(new BusinessException("Insufficient balance for payment"));
                    }
                    return paymentRepository.save(paymentMapper.toEntity(payment))
                            .map(paymentMapper::toDto)
                            .doOnSuccess(savedPayment -> logger.info("Payment saved successfully: {}", savedPayment))
                            .flatMap(savedPayment -> {
                                account.setBalance(account.getBalance() - payment.getAmount().doubleValue());
                                return webClient.put()
                                        .uri(accountsUri+"/"+account.getId() )
                                        .bodyValue(account)
                                        .retrieve()
                                        .bodyToMono(AccountResponse.class)
                                        .onErrorResume(e -> Mono.error(new BusinessException("Error updating account: " + e.getMessage())))
                                        .thenReturn(savedPayment);
                            });
                })
                .map(savedPayment -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(savedPayment)
                );
    }
}