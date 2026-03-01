package com.tribesystems.payment.transaction.model;

import com.tribesystems.payment.common.model.BaseEntity;
import com.tribesystems.payment.transaction.enums.TransactionStatus;
import com.tribesystems.payment.transaction.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "transaction")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Transaction extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long transactionId;

    @Column(nullable = false)
    private String MerchantRequestID;
    @Column(nullable = false)
    private String CheckoutRequestID;
    @Column(nullable = false)
    private double ResponseCode;

    @Column(nullable = false)
//    @Enumerated(EnumType.STRING)
//    private TransactionStatus transactionStatus;
    private String transactionStatus;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
}
