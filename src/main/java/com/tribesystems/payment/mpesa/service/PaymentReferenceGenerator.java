package com.tribesystems.payment.mpesa.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentReferenceGenerator {

    public String generatePaymentReference()
    {
        return LocalDateTime.now() + "_" + UUID.randomUUID();
    }
}
