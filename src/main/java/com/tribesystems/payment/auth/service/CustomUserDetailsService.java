package com.tribesystems.payment.auth.service;

import com.tribesystems.payment.auth.dto.CustomUserDetails;
import com.tribesystems.payment.auth.model.User;
import com.tribesystems.payment.auth.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @NotNull
    @Override
    public CustomUserDetails loadUserByUsername(@NotNull String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByUsername(username);

        if(user.isEmpty())
        {
            throw new UsernameNotFoundException("User " + username + " does not exist");
        }

        return new CustomUserDetails(user.get());
    }
}
