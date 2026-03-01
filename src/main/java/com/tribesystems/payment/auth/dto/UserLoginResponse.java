package com.tribesystems.payment.auth.dto;

public record UserLoginResponse(
        String status,
        String message,
        String token,
        UserDto details
) {
}
