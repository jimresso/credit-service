package com.nttdata.creditproducts.service.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import com.nttdata.creditproducts.service.model.PaymentDTO;

public interface PaymentRepository extends ReactiveMongoRepository<PaymentDTO, String> {
}