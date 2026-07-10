package com.tribesystems.payment.mpesa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InitiatePaymentResponse{
    private String paymentReference;
    private String MerchantRequestID;
    private String CheckoutRequestID;
    private double ResponseCode;
    private String ResponseDescription;
    private String CustomerMessage;
}
