package com.tribesystems.payment.mpesa.dto;

public record AuthenticateAppResponse(
        String access_token,
        String expires_in
) {
}
