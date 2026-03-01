package com.tribesystems.payment.common.dto;

public record ApiResponse<T>(
        int status,
        String message,
        T data
) {
}
