package com.nttdata.creditproducts.service.service.impl;

import com.nttdata.creditproducts.service.exception.BusinessException;
import com.nttdata.creditproducts.service.exception.CreditCardNotFoundException;
import com.nttdata.creditproducts.service.exception.InsufficientFundsException;
import com.nttdata.creditproducts.service.exception.InternalServerErrorException;
import com.nttdata.creditproducts.service.mapper.CreditCardMapper;
import com.nttdata.creditproducts.service.mapper.TransactionMapper;
import com.nttdata.creditproducts.service.model.Account;
import com.nttdata.creditproducts.service.model.WithdrawRequest;
import com.nttdata.creditproducts.service.notifications.FallbackNotifier;
import com.nttdata.creditproducts.service.repository.CreditCardRepository;
import com.nttdata.creditproducts.service.repository.TransactionRepository;
import com.nttdata.creditproducts.service.service.TransactionService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.Transaction;
import org.openapitools.model.TransactionRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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
    private final FallbackNotifier fallbackNotifier;
    @Value("${limit.transaction}")
    private int limit;
    // Un cliente puede cargar consumos a sus tarjetas de crédito en base a su límite de crédito.
    @Override
    @CircuitBreaker(name = "circuitBreakerTransaction", fallbackMethod = "fallbackConsume")
    public Mono<ResponseEntity<Transaction>> consume(TransactionRequest transactionRequest) {
        if (transactionRequest.getCardNumber() == null || transactionRequest.getCardNumber().isBlank()) {
            return Mono.error(new BusinessException("Card number is required"));
        }
        String customerId = transactionRequest.getCustomerId();
        String cardNumber = transactionRequest.getCardNumber();
        Double amount = transactionRequest.getMonto().doubleValue();
        return creditCardRepository.findByCardNumber(cardNumber)
                .flatMap(creditCard -> {
                    logger.info("Processing transaction for card {}", cardNumber);
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
                                    return Mono.error(new InternalServerErrorException("Error processing withdrawal"));
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

    public Mono<ResponseEntity<Transaction>> fallbackConsume(Throwable throwable) {
        logger.warn("Fallback activado para consume: {} - {}",
                throwable.getClass().getSimpleName(), throwable.getMessage());
        if (throwable instanceof BusinessException) {
            return Mono.error(new BusinessException(throwable.getMessage()));
        }
        fallbackNotifier.sendFallbackEmail("TransactionServiceImpl", throwable);
        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build());
    }
}
