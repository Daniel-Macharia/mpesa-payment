package com.tribesystems.payment.mpesa.dto;

public record SimulatePaymentForC2BResponse(
        String OriginatorCoversationID,
        String ResponseCode,
        String ResponseDescription
) {
}
