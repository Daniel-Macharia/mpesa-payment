package com.tribesystems.payment.auth.dto;

public record UserDto(
        String username,
        String password,
        String role
) {
}
