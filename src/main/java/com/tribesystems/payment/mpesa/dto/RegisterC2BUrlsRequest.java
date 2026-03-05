package com.tribesystems.payment.mpesa.dto;

public record RegisterC2BUrlsRequest(
        String ShortCode,
        String ResponseType,
        String ConfirmationURL,
        String ValidationURL
) {
}
