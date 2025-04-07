package com.nttdata.creditproducts.service.service.impl;

import com.nttdata.creditproducts.service.controller.PaymentController;
import com.nttdata.creditproducts.service.exception.CreditCardNotFoundException;
import com.nttdata.creditproducts.service.exception.InsufficientFundsException;
import com.nttdata.creditproducts.service.mapper.CreditCardMapper;
import com.nttdata.creditproducts.service.mapper.TransactionMapper;
import com.nttdata.creditproducts.service.repository.CreditCardRepository;
import com.nttdata.creditproducts.service.repository.TransactionRepository;
import com.nttdata.creditproducts.service.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.Transaction;
import org.openapitools.model.TransactionRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final CreditCardRepository creditCardRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final CreditCardMapper creditCardMapper;
    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);
    // Un cliente puede cargar consumos a sus tarjetas de crédito en base a su límite de crédito.
    @Override
    public Mono<ResponseEntity<Transaction>> consume(TransactionRequest transactionRequest) {
        String customerId = transactionRequest.getCustomerId();
        Double amount = transactionRequest.getMonto().doubleValue();

        return creditCardRepository.findByCustomerId(customerId)
                .flatMap(creditCard -> {
                    double amountLimit = creditCard.getBalance().doubleValue() + amount;
                    if (amountLimit > creditCard.getLimit().doubleValue()) {
                        return Mono.error(new InsufficientFundsException("Limit exceeded"));
                    }
                    BigDecimal newBalance = BigDecimal.valueOf(amountLimit);
                    creditCard.setBalance(newBalance);
                    return creditCardRepository.save(creditCardMapper.toEntity(creditCard))
                            .flatMap(updatedCreditCard -> {
                                Transaction transaction = new Transaction();
                                transaction.setCustomerId(customerId);
                                transaction.setAmount(amount);
                                transaction.setTransactionDate(LocalDate.now());
                                Transaction.TransactionTypeEnum type = Transaction.TransactionTypeEnum.valueOf(
                                        transactionRequest.getTransactionType().name());
                                transaction.setTransactionType(type);
                                transaction.setDni(updatedCreditCard.getDni());
                                return transactionRepository.save(transactionMapper.toDto(transaction))
                                        .map(savedTransactionDTO ->
                                                ResponseEntity.ok(transactionMapper.toEntity(savedTransactionDTO)));
                            });
                })
                .switchIfEmpty(Mono.error(new CreditCardNotFoundException("Credit card not found")));
    }
}
