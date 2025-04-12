package com.nttdata.creditproducts.service.service.impl;

import com.nttdata.creditproducts.service.controller.PaymentController;
import com.nttdata.creditproducts.service.exception.BusinessException;
import com.nttdata.creditproducts.service.exception.CreditCardNotFoundException;
import com.nttdata.creditproducts.service.exception.InsufficientFundsException;
import com.nttdata.creditproducts.service.mapper.CreditCardMapper;
import com.nttdata.creditproducts.service.mapper.TransactionMapper;
import com.nttdata.creditproducts.service.model.Account;
import com.nttdata.creditproducts.service.model.CreditCardDTO;
import com.nttdata.creditproducts.service.model.WithdrawRequest;
import com.nttdata.creditproducts.service.repository.CreditCardRepository;
import com.nttdata.creditproducts.service.repository.TransactionRepository;
import com.nttdata.creditproducts.service.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.Transaction;
import org.openapitools.model.TransactionRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
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
    private final WebClient.Builder webClientBuilder;
    @Value("${limit.transaction}")
    private int limit;
    // Un cliente puede cargar consumos a sus tarjetas de crédito en base a su límite de crédito.
    @Override
    public Mono<ResponseEntity<Transaction>> consume(TransactionRequest transactionRequest) {
        WebClient webClient = webClientBuilder.build();
        String customerId = transactionRequest.getCustomerId();
        String cardNumber = transactionRequest.getCardNumber();
        Double amount = transactionRequest.getMonto().doubleValue();
        return creditCardRepository.findByCardNumber(cardNumber)
                .flatMap(creditCard -> {
                    if (creditCard.getTypeCard().name().equals("DEBITO")) {
                        WithdrawRequest withdrawRequest = new WithdrawRequest();
                        withdrawRequest.monto(transactionRequest.getMonto().doubleValue());
                        return webClientBuilder.baseUrl("http://localhost:8086")
                                .build()
                                .put()
                                .uri("/api/accounts/{customerId}/withdraw", customerId)
                                .bodyValue(withdrawRequest)
                                .retrieve()
                                .bodyToMono(Account.class)
                                .onErrorResume(throwable -> {
                                    if (throwable instanceof BusinessException &&
                                            throwable.getMessage().contains("insufficient balance")) {
                                        return Mono.error(
                                                new InsufficientFundsException("Insufficient balance in the account"));
                                    }
                                    return Mono.error(throwable);
                                })
                                .flatMap(response -> {
                                    double updatedBalance = creditCard.getBalance().doubleValue() - amount;
                                    creditCard.setBalance(BigDecimal.valueOf(updatedBalance));

                                    return creditCardRepository.save(creditCardMapper.toEntity(creditCard))
                                            .flatMap(updatedCreditCard -> {
                                                Transaction transaction = new Transaction();
                                                transaction.setCustomerId(customerId);
                                                transaction.setAmount(amount);
                                                transaction.setTransactionDate(LocalDate.now());
                                                Transaction.TransactionTypeEnum type =
                                                        Transaction.TransactionTypeEnum.valueOf(
                                                        transactionRequest.getTransactionType().name());
                                                transaction.setTransactionType(type);
                                                transaction.setDni(updatedCreditCard.getDni());

                                                return transactionRepository.save(transactionMapper.toDto(transaction))
                                                        .map(savedTransactionDTO ->
                                                                ResponseEntity.ok(
                                                                        transactionMapper
                                                                                .toEntity(savedTransactionDTO)));
                                            });
                                })
                                .onErrorResume(e -> {
                                    logger.error("Error during withdrawal: {}", e.getMessage());
                                    return Mono.error(new Exception("Error processing withdrawal"));
                                });
                    } else {
                        double amountLimit = creditCard.getBalance().doubleValue() + amount;
                        if (amountLimit > creditCard.getLimit().doubleValue()) {
                            return Mono.error(new InsufficientFundsException("Limit exceeded"));
                        }
                        BigDecimal newBalance = BigDecimal.valueOf(amountLimit);
                        creditCard.setBalance(newBalance); }
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
