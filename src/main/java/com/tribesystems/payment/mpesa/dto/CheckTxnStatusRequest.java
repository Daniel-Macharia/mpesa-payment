package com.tribesystems.payment.mpesa.dto;

public record CheckTxnStatusRequest(
        String Initiator,
        String SecurityCredential,
        String CommandID,
        String TransactionID,
        String OriginalConversationID,
        String PartyA,
        String IdentifierType,
        String ResultURL,
        String QueueTimeOutURL,
        String Remarks,
        String Occasion
) {
}
