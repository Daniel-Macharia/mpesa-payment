package com.tribesystems.payment.mpesa.dto;

public record ConfirmPaymentStatusResponse(
        String OriginatorConversationID,
        String ConversationID,
        int ResponseCode,
        String ResponseDescription
) {
}
