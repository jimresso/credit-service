package com.nttdata.creditproducts.service.mapper;

import com.nttdata.creditproducts.service.model.TransactionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.openapitools.model.Transaction;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(target = "customerId", source = "customerId")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "transactionDate", source = "transactionDate")
    @Mapping(target = "transactionType", source = "transactionType")
    Transaction toEntity(TransactionDTO transactionDTO);

    @Mapping(target = "customerId", source = "customerId")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "transactionDate", source = "transactionDate")
    @Mapping(target = "transactionType", source = "transactionType")
    TransactionDTO toDto(Transaction transaction);
}