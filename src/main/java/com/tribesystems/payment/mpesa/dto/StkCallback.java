package com.tribesystems.payment.mpesa.dto;

public record StkCallback(
        String MerchantRequestID,
        String CheckoutRequestID,
        int ResultCode,
        String ResultDesc,
        CallbackMetadata CallbackMetadata
) {
}
