package com.tribesystems.payment.transaction.model;

import com.tribesystems.payment.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "mpesa_transaction")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class MpesaConfirmedTransactionCallback extends BaseEntity {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mpesaConfirmedTransactionCallbackId;

    @Column(nullable = false)
    private String merchantRequestID;
    @Column(nullable = false)
    private String checkoutRequestID;
    @Column(nullable = false)
    private String resultCode;
    @Column(nullable = false)
    private String resultDesc;
    @Column(nullable = false)
    private String amount;
    @Column(nullable = false)
    private String mpesaReceiptNumber;
    @Column(nullable = false)
    private String transactionDate;
    @Column(nullable = false)
    private String phoneNumber;
}
