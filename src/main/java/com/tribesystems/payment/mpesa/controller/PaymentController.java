package com.tribesystems.payment.mpesa.controller;

import com.tribesystems.payment.common.dto.ApiResponse;
import com.tribesystems.payment.mpesa.dto.*;
import com.tribesystems.payment.mpesa.service.MpesaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.tribesystems.payment.common.constants.Constants.API;
import static com.tribesystems.payment.common.constants.Constants.PAYMENTS;
import static com.tribesystems.payment.mpesa.constants.MpesaConstants.*;

@RestController
@RequestMapping(API + PAYMENTS + MPESA)
@Tag(name = "mpesa payment controller", description = "provides endpoint to facilitate payments via mpesa")
public class PaymentController {

    private final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    private final MpesaService mpesaService;

    @Autowired
    public PaymentController(MpesaService mpesaService)
    {
        this.mpesaService = mpesaService;
    }

    @PostMapping(INITIATE_STK_PUSH)
    @Operation(summary = "initiate C2B payment", description = "make stk push to specified phone number with specified amount")
    public ApiResponse<InitiatePaymentResponse> initiatePayment(
            @RequestBody InitiatePaymentDto initiatePayment
            )
    {
        return mpesaService.initiatePayment(initiatePayment);
    }

    @PostMapping(CONFIRM_PAYMENT)
    @Operation(summary = "confirm status of transaction", description = "check the status of the transaction specified by the originator conversation ID")
    public ApiResponse<ConfirmPaymentStatusResponse> confirmPaymentStatus(
            @RequestBody CheckTxnStatusDto checkTxnStatusDto
    )
    {
        return mpesaService.checkTransactionStatus(checkTxnStatusDto);
    }

    @PostMapping(CONFIRM_PAYMENT_RESULT)
    @Operation(summary = "confirm payment result callback", description = "invoked by daraja api with the result of the payment confirmation")
    public ApiResponse<String> confirmPaymentStatusResultCallback(
            @RequestBody Result result
    )
    {
        logger.info("=========================== received result in confirm payment timeout callback ===========================");
        logger.error("{}", result);
        return new ApiResponse<>(
                200,
                "success",
                "confirm payment status result callback received payload"
        );
    }

    @PostMapping(CONFIRM_PAYMENT_TIMEOUT)
    @Operation(summary = "confirm payment timeout callback", description = "invoked by daraja api with the result of the payment confirmation that timed out")
    public ApiResponse<String> confirmPaymentStatusTimeoutCallback(
            @RequestBody Result result
    )
    {
        logger.info("=========================== received result in confirm payment timeout callback ===========================");
        logger.error("{}", result);
        return new ApiResponse<>(
                200,
                "success",
                "confirm payment status timeout callback received payload"
        );
    }

    @PostMapping(INITIATE_STK_PUSH_CALLBACK)
    @Operation(summary = "initiate stk push callback", description = "invoked by daraja api with the result of an stk push")
    public ApiResponse<String> mpesaPaymentCallback(
            @RequestBody StkCallback stkCallback
    )
    {
        logger.info("========================== Received stk callback ======================");
        logger.error("{}", stkCallback);
        return new ApiResponse<>(
                200,
                "success",
                "mpesa payment callback received payload"
        );
    }
}
