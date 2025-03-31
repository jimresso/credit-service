package com.nttdata.creditproducts.service.model;


import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "transactions")
public class TransactionDTO {
    private String customerId;
    private Double amount;
    private String transactionDate;
    private TransactionType transactionType;

    public enum TransactionType {
        COMPRA, PAGO
    }
}
