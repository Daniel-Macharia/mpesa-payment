package com.tribesystems.payment.auth.dto;

public record UserLoginRequest(
        String username,
        String password
) {
}
