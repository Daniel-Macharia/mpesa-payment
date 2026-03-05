package com.tribesystems.payment.transaction.mapper;

import com.tribesystems.payment.mpesa.dto.InitiatePaymentDto;
import com.tribesystems.payment.mpesa.dto.InitiatePaymentResponse;
import com.tribesystems.payment.mpesa.dto.TransactionCallbackRequest;
import com.tribesystems.payment.transaction.enums.TransactionType;
import com.tribesystems.payment.transaction.model.ConfirmedTransaction;
import com.tribesystems.payment.transaction.model.Transaction;
import jakarta.persistence.Column;

public class TransactionMapper {

    public static Transaction initiatePaymentResponseToTransactionMapper(InitiatePaymentResponse resp, InitiatePaymentDto dto, String status)
    {
        return Transaction.builder()
                .MerchantRequestID(resp.MerchantRequestID())
                .CheckoutRequestID(resp.CheckoutRequestID())
                .transactionStatus(status)
                .transactionType(TransactionType.MPESA)
                .ResponseCode(resp.ResponseCode())
                .customerPhoneNumber(dto.phoneNumber())
                .amount(dto.amount())
                .build();
    }

    public static ConfirmedTransaction transactionCallbackRequestToConfirmedTransactionMapper(TransactionCallbackRequest request, String transactionStatus)
    {

        return ConfirmedTransaction.builder()
                .mpesaTransactionStatus(transactionStatus)
                .mpesaTransactionType(request.TransactionType())
                .mpesaTransactionId(request.TransID())
                .mpesaTransactionTime(request.TransTime())
                .mpesaTransactionAmount(request.TransAmount())
                .mpesaTransactionBusinessShortCode(request.BusinessShortCode())
                .mpesaTransactionBillRefNumber(request.BillRefNumber())
                .mpesaTransactionInvoiceNumber(request.InvoiceNumber())
                .mpesaTransactionOrgAccountBalance(request.OrgAccountBalance())
                .mpesaTransactionThirdPartyTransId(request.ThirdPartyTransID())
                .mpesaTransactionMSISDN(request.MSISDN())
                .mpesaTransactionFirstName(request.FirstName())
                .mpesaTransactionMiddleName(request.MiddleName())
                .mpesaTransactionLastName(request.LastName())
                .build();
    }
}
