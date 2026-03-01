package com.tribesystems.payment.mpesa.dto;

//result returned by mpesa check transaction status
public record Result(
    String ConversationID,
    String OriginatorConversationID,
    ReferenceData ReferenceData,
    int ResultCode,
    String ResultDesc,
    String ResultType,
    String TransactionID,
    ResultParameters ResultParameters
) {
}
