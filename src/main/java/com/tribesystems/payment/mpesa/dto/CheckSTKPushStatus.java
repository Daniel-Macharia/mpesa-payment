package com.tribesystems.payment.mpesa.dto;

public record CheckSTKPushStatus(
        String BusinessShortCode,
            String Password,
            String Timestamp,
            String CheckoutRequestID
) {
}
