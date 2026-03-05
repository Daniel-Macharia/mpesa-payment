package com.tribesystems.payment.mpesa.dto;

import java.math.BigInteger;

public record SimulatePaymentForC2BRequest(
        long ShortCode,
        String CommandID,
        long Amount,
        BigInteger Msisdn,
        String BillRefNumber

) {
}
