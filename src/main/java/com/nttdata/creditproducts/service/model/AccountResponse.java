package com.nttdata.creditproducts.service.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.List;

@Data
public class AccountResponse {
    @Id
    private String id;
    private String customerId;
    private CustomerType customerType;
    private AccountType accountType;
    private Double balance;
    private Integer monthlyLimit;
    private LocalDate lastDepositDate;
    private List<String> holders;
    private Double limitTransaction;
    private ClientType clientType;

    public enum AccountType {
        AHORRO,
        CORRIENTE,
        PLAZO_FIJO
    }
    public enum CustomerType {
        PERSONAL,
        EMPRESARIAL
    }
    public enum ClientType {
        VIP,
        PYME,
    }
}