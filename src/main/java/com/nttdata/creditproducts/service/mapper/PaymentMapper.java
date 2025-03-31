package com.nttdata.creditproducts.service.mapper;

import com.nttdata.creditproducts.service.model.PaymentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.openapitools.model.Payment;


@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "creditId", source = "creditId")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "customerId", source = "customerId")
    @Mapping(target = "paymentDate", source = "paymentDate")
    PaymentDTO toEntity(Payment dto);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "creditId", source = "creditId")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "customerId", source = "customerId")
    @Mapping(target = "paymentDate", source = "paymentDate")
    Payment toDto(PaymentDTO entity);
}