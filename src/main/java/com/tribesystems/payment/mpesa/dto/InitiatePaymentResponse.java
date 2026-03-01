package com.tribesystems.payment.mpesa.dto;

public record InitiatePaymentResponse(
    String MerchantRequestID,
    String CheckoutRequestID,
    double ResponseCode,
    String ResponseDescription,
    String CustomerMessage
) {
}
