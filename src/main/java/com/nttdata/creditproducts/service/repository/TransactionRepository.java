package com.nttdata.creditproducts.service.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import com.nttdata.creditproducts.service.model.TransactionDTO;

public interface TransactionRepository extends ReactiveMongoRepository<TransactionDTO, String> {
}