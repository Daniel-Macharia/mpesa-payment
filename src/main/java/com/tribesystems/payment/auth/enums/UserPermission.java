package com.tribesystems.payment.auth.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserPermission {
    INITIATE_MPESA_STK_PUSH("initiate stk push"),
    CONFIRM_MPESA_PAYMENT_STATUS("confirm mpesa payment status");

    @Getter
    private final String userPermission;
}
