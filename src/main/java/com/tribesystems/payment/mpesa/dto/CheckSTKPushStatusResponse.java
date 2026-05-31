package com.tribesystems.payment.mpesa.dto;

public record CheckSTKPushStatusResponse(
        double ResponseCode,
        String ResponseDescription,
        String MerchantRequestID,
        String CheckoutRequestID,
        String ResultCode,
        String ResultDesc
) {
}
