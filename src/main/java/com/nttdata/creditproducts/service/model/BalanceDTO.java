package com.nttdata.creditproducts.service.model;

import lombok.Data;
import org.openapitools.model.Credit;
import org.openapitools.model.CreditCard;


import java.util.List;

@Data
public class BalanceDTO {

    private String customerId;
    private List<Account> bankAccounts;
    private List<Credit> credits;
    private List<CreditCard> creditCards;
}
