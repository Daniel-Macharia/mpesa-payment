package com.tribesystems.payment.mpesa.dto;

public record TransactionCallbackRequest(
        String TransactionType,
        String TransID,
        String TransTime,
        String TransAmount,
        String BusinessShortCode,
        String BillRefNumber,
        String InvoiceNumber,
        String OrgAccountBalance,
        String ThirdPartyTransID,
        String MSISDN,
        String FirstName,
        String MiddleName,
        String LastName
) {
}
