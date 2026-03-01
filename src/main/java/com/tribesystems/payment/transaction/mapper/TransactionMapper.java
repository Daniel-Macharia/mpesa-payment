package com.tribesystems.payment.transaction.mapper;

import com.tribesystems.payment.mpesa.dto.InitiatePaymentResponse;
import com.tribesystems.payment.transaction.enums.TransactionType;
import com.tribesystems.payment.transaction.model.Transaction;

public class TransactionMapper {

    public static Transaction initiatePaymentResponseToTransactionMapper(InitiatePaymentResponse resp, String status)
    {
        return Transaction.builder()
                .MerchantRequestID(resp.MerchantRequestID())
                .CheckoutRequestID(resp.CheckoutRequestID())
                .transactionStatus(status)
                .transactionType(TransactionType.MPESA)
                .ResponseCode(resp.ResponseCode())
                .build();
    }
}
