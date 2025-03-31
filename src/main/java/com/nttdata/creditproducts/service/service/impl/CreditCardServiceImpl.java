package com.nttdata.creditproducts.service.service.impl;


import com.nttdata.creditproducts.service.mapper.CreditCardMapper;
import com.nttdata.creditproducts.service.model.Account;
import com.nttdata.creditproducts.service.repository.CreditCardRepository;
import com.nttdata.creditproducts.service.service.CreditCardService;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.CreditCard;
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
public class CreditCardServiceImpl implements CreditCardService {

    private final CreditCardRepository creditCardRepository;
    private final CreditCardMapper creditCardMapper;
    private static final Logger logger = LoggerFactory.getLogger(CreditCardServiceImpl.class);

    private WebClient.Builder webClientBuilder;
    @Value("${account.service.uri.put}")
    private String accountsUri;

    @Override
    public Mono<ResponseEntity<CreditCard>> createCreditCard(CreditCard credit) {
        WebClient webClient = webClientBuilder.build();
        return webClient.get()
                .uri(accountsUri)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Account>>() {})
                .flatMapMany(Flux::fromIterable)
                .filter(account -> account.getCustomerId().equals(credit.getCustomerId()))
                .next()
                .switchIfEmpty(Mono.defer(() -> {
                    logger.info("The client does not have an associated bank account. The loan will be created.");
                    return Mono.just(new Account());
                }))
                .flatMap(account -> {
                    logger.info("Building credit for the customer {}", credit.getCustomerId());
                    return creditCardRepository.save(creditCardMapper.toEntity(credit))
                            .map(creditCardMapper::toDto)
                            .map(savedCredit -> ResponseEntity
                                    .status(HttpStatus.CREATED)
                                    .body(savedCredit)
                            )
                            .doOnSuccess(savedCredit -> logger.info("Credit successfully created for the client: {}", savedCredit));
                });
    }
}