package com.nttdata.creditproducts.service.repository;

import org.openapitools.model.CreditCard;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import com.nttdata.creditproducts.service.model.CreditCardDTO;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CreditCardRepository extends ReactiveMongoRepository<CreditCardDTO, String> {
    Mono<CreditCardDTO> findByCardNumber(String cardNumber);


    Mono<CreditCard> findByCustomerId(String customerId);
}