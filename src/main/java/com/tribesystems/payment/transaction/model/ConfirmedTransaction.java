package com.tribesystems.payment.transaction.model;

import com.tribesystems.payment.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name ="confirmed_transaction")
@Builder
@Data
public class ConfirmedTransaction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long confirmedTransactionId;

    @Column(nullable = false)
    private String mpesaTransactionStatus;

    @Column(nullable = false)
    private String mpesaTransactionType;
    @Column(nullable = false)
    private String mpesaTransactionId;
    @Column(nullable = false)
    private String mpesaTransactionTime;
    @Column(nullable = false)
    private String mpesaTransactionAmount;
    @Column(nullable = false)
    private String mpesaTransactionBusinessShortCode;
    @Column
    private String mpesaTransactionBillRefNumber;
    @Column
    private String mpesaTransactionInvoiceNumber;
    @Column
    private String mpesaTransactionOrgAccountBalance;
    @Column
    private String mpesaTransactionThirdPartyTransId;
    @Column
    private String mpesaTransactionMSISDN;
    @Column
    private String mpesaTransactionFirstName;
    @Column
    private String mpesaTransactionMiddleName;
    @Column
    private String mpesaTransactionLastName;
}
