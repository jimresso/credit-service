package com.nttdata.creditproducts.service.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document(collection = "debtors")
public class DebtorsDTO {
    @Id
    private String id;
    private String customerId;
    private String cardNumber;
    private Double amount;
    private LocalDate expirationDate;
    private StatusEnum status;
    public enum StatusEnum {
        PENDING, CANCELED
    }



}
