package com.tribesystems.payment.transaction.model;

import com.tribesystems.payment.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Table(name = "register_c2b_callbacks")
@Builder
@Getter
public class RegisterC2BCallbacksModel extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long registerCallbacksId;

    @Column(nullable = false)
    private String registerCallbacksTransactionId;
}
