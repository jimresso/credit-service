package com.nttdata.creditproducts.service.mapper;

import com.nttdata.creditproducts.service.model.debtorsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.openapitools.model.DebtorsRequest;

@Mapper(componentModel = "spring")
public interface DebtorsMapper {
    @Mapping(target = "customerId", source = "customerId")
    @Mapping(target = "cardNumber", source = "cardNumber")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "expirationDate", source = "expirationDate")
    @Mapping(target = "status", source = "status")
    debtorsDTO toEntity(DebtorsRequest debtorsRequest);

}
