package com.nttdata.creditproducts.service.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import com.nttdata.creditproducts.service.model.TransactionDTO;
import reactor.core.publisher.Flux;

import java.util.Locale;

public interface TransactionRepository extends ReactiveMongoRepository<TransactionDTO, String> {
    Flux<TransactionDTO> findByDni(String dni);
}
