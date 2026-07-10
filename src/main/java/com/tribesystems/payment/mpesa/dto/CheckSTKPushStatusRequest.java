package com.tribesystems.payment.mpesa.dto;

public record CheckSTKPushStatusRequest(
//        String CheckoutRequestId
        String paymentReference
) {
}
