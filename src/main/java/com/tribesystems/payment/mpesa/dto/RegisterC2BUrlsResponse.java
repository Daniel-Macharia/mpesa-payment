package com.tribesystems.payment.mpesa.dto;

public record RegisterC2BUrlsResponse(
        String OriginatorCoversationID,
        String ResponseCode,
        String ResponseDescription
) {
}
