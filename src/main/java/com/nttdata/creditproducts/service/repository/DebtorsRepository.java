package com.nttdata.creditproducts.service.repository;


import com.nttdata.creditproducts.service.model.DebtorsDTO;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface DebtorsRepository extends ReactiveMongoRepository<DebtorsDTO, String> {
    Flux<DebtorsDTO> findByCardNumber(String cardNumber);
    Flux<DebtorsDTO> findByCustomerId(String customerId);
}
