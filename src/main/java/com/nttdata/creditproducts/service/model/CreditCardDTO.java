package com.nttdata.creditproducts.service.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;



@Data
@Document(collection = "creditcards")
public class CreditCardDTO {
    @Id
    private String id;
    private String customerId;
    private String cardNumber;
    private LocalDate expirationDate;
    private Double limit;
    private Double balance;
    private StatusEnum status;
    public enum StatusEnum {
        ACTIVE, BLOCKED, CLOSED
    }


}