package com.nttdata.creditproducts.service.mapper;

import com.nttdata.creditproducts.service.model.Account;
import com.nttdata.creditproducts.service.model.AccountResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "customerType", source = "customerType")
    @Mapping(target = "accountType", source = "accountType")
    @Mapping(target = "balance", source = "balance")
    @Mapping(target = "monthlyLimit", source = "monthlyLimit")
    @Mapping(target = "lastDepositDate", source = "lastDepositDate")
    @Mapping(target = "holders", source = "holders")
    Account toEntity(AccountResponse accountResponse);

    @Mapping(target = "customerType", source = "customerType")
    @Mapping(target = "accountType", source = "accountType")
    @Mapping(target = "balance", source = "balance")
    @Mapping(target = "monthlyLimit", source = "monthlyLimit")
    @Mapping(target = "lastDepositDate", source = "lastDepositDate")
    @Mapping(target = "holders", source = "holders")
    AccountResponse toDto(Account account);
}
