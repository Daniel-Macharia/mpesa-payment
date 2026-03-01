package com.tribesystems.payment.auth.controller;

import com.tribesystems.payment.auth.dto.UserDto;
import com.tribesystems.payment.auth.dto.UserLoginRequest;
import com.tribesystems.payment.auth.dto.UserLoginResponse;
import com.tribesystems.payment.auth.service.AuthService;
import com.tribesystems.payment.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.tribesystems.payment.auth.constants.AuthConstants.*;
import static com.tribesystems.payment.common.constants.Constants.API;

@RestController
@RequestMapping(API + AUTH)
@Tag(name = "authentication controller", description = "endpoint for user authentication")
public class AuthController {

    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService)
    {
        this.authService = authService;
    }

    @PostMapping(LOGIN)
    @Operation(summary = "User login", description = "Allow user login")
    public ApiResponse<UserLoginResponse> userLogin(@RequestBody UserLoginRequest userLoginRequest)
    {
        logger.info("loging in");
        logger.info("{}", userLoginRequest);
        return authService.login(userLoginRequest);
    }

    @PostMapping(UPDATE_PASSWORD)
    @Operation(summary = "Change user password", description = "allow changing of user password")
    public ApiResponse<UserDto> userPasswordChange(@RequestBody UserDto changePasswordRequest)
    {
        logger.info("Change password controller");
        return authService.updateUserPassword(changePasswordRequest);
    }

    @PostMapping(CREATE_USER)
    @Operation(summary = "Create user", description = "allow creation of users")
    public ApiResponse<UserDto> createUser(@RequestBody UserDto createUserRequest)
    {
        return authService.createNewUser(createUserRequest);
    }
}
