package com.tribesystems.payment.auth.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;

import static com.tribesystems.payment.auth.enums.UserPermission.CONFIRM_MPESA_PAYMENT_STATUS;
import static com.tribesystems.payment.auth.enums.UserPermission.INITIATE_MPESA_STK_PUSH;

@RequiredArgsConstructor
public enum UserRole {
    ADMIN(
            Set.of(
                    INITIATE_MPESA_STK_PUSH,
                    CONFIRM_MPESA_PAYMENT_STATUS
            )
    ),
    APPLICATION(
            Set.of(
                    INITIATE_MPESA_STK_PUSH,
                    CONFIRM_MPESA_PAYMENT_STATUS
            )
    );

    @Getter
    private final Set<UserPermission> userPermissions;

    public List<SimpleGrantedAuthority> getAuthorities()
    {
        var auth = getUserPermissions()
                .stream()
                .map(userPermission -> new SimpleGrantedAuthority(userPermission.getUserPermission()))
                .toList();

        auth.addFirst(new SimpleGrantedAuthority("ROLE_" + this.name()));

        return auth;
    }
}
