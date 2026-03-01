package com.tribesystems.payment.mpesa.dto;

public record InitiatePaymentDto(
        String phoneNumber,
        double amount
) {
}
