package com.nttdata.creditproducts.service.repository;


import com.nttdata.creditproducts.service.model.debtorsDTO;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface DebtorsRepository extends ReactiveMongoRepository<debtorsDTO, String> {
    Flux<debtorsDTO> findByCardNumber(String cardNumber);
    Flux<debtorsDTO> findByCustomerId(String customerId);
}
