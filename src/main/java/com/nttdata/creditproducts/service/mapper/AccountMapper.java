package com.nttdata.creditproducts.service.mapper;

import com.nttdata.creditproducts.service.model.Account;
import com.nttdata.creditproducts.service.model.AccountResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "customerType", source = "customerType")
    @Mapping(target = "accountType", source = "accountType")
    @Mapping(target = "balance", source = "balance")
    @Mapping(target = "monthlyLimit", source = "monthlyLimit")
    @Mapping(target = "lastDepositDate", source = "lastDepositDate")
    @Mapping(target = "holders", source = "holders")
    @Mapping(target = "limitTransaction", source = "limitTransaction")
    @Mapping(target = "clientType", source = "clientType")
    Account toEntity(AccountResponse accountResponse);

    @Mapping(target = "customerType", source = "customerType")
    @Mapping(target = "accountType", source = "accountType")
    @Mapping(target = "balance", source = "balance")
    @Mapping(target = "monthlyLimit", source = "monthlyLimit")
    @Mapping(target = "lastDepositDate", source = "lastDepositDate")
    @Mapping(target = "holders", source = "holders")
    @Mapping(target = "limitTransaction", source = "limitTransaction")
    @Mapping(target = "clientType", source = "clientType")
    AccountResponse toDto(Account account);
}
