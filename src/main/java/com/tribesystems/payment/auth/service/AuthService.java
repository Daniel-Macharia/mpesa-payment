package com.tribesystems.payment.auth.service;


import com.tribesystems.payment.auth.dto.CustomUserDetails;
import com.tribesystems.payment.auth.dto.UserDto;
import com.tribesystems.payment.auth.dto.UserLoginRequest;
import com.tribesystems.payment.auth.dto.UserLoginResponse;
import com.tribesystems.payment.auth.mapper.UserMapper;
import com.tribesystems.payment.auth.repository.UserRepository;
import com.tribesystems.payment.common.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserRepository userRepository,
                       JwtService jwtService,
                       CustomUserDetailsService customUserDetailsService,
                       BCryptPasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    public ApiResponse<UserLoginResponse> login(UserLoginRequest loginRequest)
    {
        try{
            CustomUserDetails userDetails = customUserDetailsService.loadUserByUsername(loginRequest.username());
            if( userDetails.getPassword() != null
                    && passwordEncoder.matches(loginRequest.password(), userDetails.getPassword()))
            {
                String jwt = jwtService.generateToken(userDetails);

                return new ApiResponse<>(
                        200,
                        "success",
                        new UserLoginResponse(
                                "200",
                                "success",
                                jwt,
                                UserMapper.userToUserDtoMapper(userDetails.getUser())
                        )
                );
            }
            else {
                return new ApiResponse<>(
                        200,
                        "success",
                        new UserLoginResponse(
                                "401",
                                "failed",
                                "Invalid password",
                                null
                        )
                );
            }
        }catch(UsernameNotFoundException e)
        {
            return new ApiResponse<>(
                    200,
                    "success",
                    new UserLoginResponse(
                            "401",
                            "user " + loginRequest.username() + " not found",
                            null,
                            null
                    )
            );
        }
    }

    public ApiResponse<UserDto> createNewUser(UserDto userDto)
    {
        try{
            return new ApiResponse<>(
                    200,
                    "success",
                    UserMapper.userToUserDtoMapper(userRepository.save(UserMapper.userDtoToUserMapper(userDto)))
            );
        } catch (Exception e) {
            logger.warn("Failed to create new user. Reason: {}", e.getMessage());
            return new ApiResponse<>(
                    500,
                    "failed",
                    null
            );
        }
    }

    public ApiResponse<UserDto> updateUserPassword(UserDto userDto)
    {
        try{
            return new ApiResponse<>(
                    200,
                    "success",
                    UserMapper.userToUserDtoMapper(userRepository.updateUserPassword(userDto.username(), userDto.password()))
            );
        } catch (Exception e) {
            logger.warn("Failed to create new user. Reason: {}", e.getMessage());
            return new ApiResponse<>(
                    500,
                    "failed",
                    null
            );
        }
    }
}
