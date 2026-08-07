package com.tribesystems.payment.transaction.mapper;

import com.tribesystems.payment.mpesa.dto.StkCallback;
import com.tribesystems.payment.transaction.model.MpesaConfirmedTransactionCallback;
import jakarta.persistence.Column;

public class MpesaTransactionMapper {

    public static MpesaConfirmedTransactionCallback stkCallbackToMpesaConfirmedTransactionCallback(
            StkCallback stkCallback,
            String amount,
            String receipt,
            String txnDate,
            String phone
    ) {
        return MpesaConfirmedTransactionCallback.builder()
                .merchantRequestID(stkCallback.MerchantRequestID())
                .checkoutRequestID(stkCallback.CheckoutRequestID())
                .resultCode("" + stkCallback.ResultCode())
                .resultDesc(stkCallback.ResultDesc())
                .amount(amount)
                .mpesaReceiptNumber(receipt)
                .transactionDate(txnDate)
                .phoneNumber(phone)
                .build();
    }
}
