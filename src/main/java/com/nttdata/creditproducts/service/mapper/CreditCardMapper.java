package com.nttdata.creditproducts.service.mapper;

import com.nttdata.creditproducts.service.model.CreditCardDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.openapitools.model.CreditCard;

@Mapper(componentModel = "spring")
public interface CreditCardMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "dni", source = "dni")
    @Mapping(target = "customerId", source = "customerId")
    @Mapping(target = "cardNumber", source = "cardNumber")
    @Mapping(target = "expirationDate", source = "expirationDate")
    @Mapping(target = "limit", source = "limit")  // Antes: creditLimit
    @Mapping(target = "balance", source = "balance")  // Antes: availableBalance
    @Mapping(target = "status", source = "status")
    @Mapping(target = "limitTransaction", source = "limitTransaction")
    CreditCardDTO toEntity(CreditCard dto);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "dni", source = "dni")
    @Mapping(target = "customerId", source = "customerId")
    @Mapping(target = "cardNumber", source = "cardNumber")
    @Mapping(target = "expirationDate", source = "expirationDate")
    @Mapping(target = "limit", source = "limit")
    @Mapping(target = "balance", source = "balance")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "limitTransaction", source = "limitTransaction")
    CreditCard toDto(CreditCardDTO entity);
}