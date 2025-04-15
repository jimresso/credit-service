package com.nttdata.creditproducts.service.repository;

import org.openapitools.model.CreditCard;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import com.nttdata.creditproducts.service.model.CreditCardDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface CreditCardRepository extends ReactiveMongoRepository<CreditCardDTO, String> {
    Mono<CreditCard> findByCustomerId(String customerId);
    Flux<CreditCard> findByDni(String dni);
    Mono<CreditCard> findByCardNumber(String cardNumber);

}