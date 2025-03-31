package com.nttdata.creditproducts.service.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Document(collection = "payments")
public class PaymentDTO {
    private String id;
    private String creditId;
    private BigDecimal amount;
    private String customerId;
    private LocalDateTime paymentDate;
}
