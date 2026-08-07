package com.tribesystems.payment.mpesa.controller;

import com.google.gson.Gson;
import com.tribesystems.payment.common.dto.ApiResponse;
import com.tribesystems.payment.mpesa.dto.*;
import com.tribesystems.payment.mpesa.service.MpesaService;
import com.tribesystems.payment.transaction.model.B2BTransaction;
import com.tribesystems.payment.transaction.model.ConfirmedTransaction;
import com.tribesystems.payment.transaction.model.Payment;
import com.tribesystems.payment.transaction.model.Transaction;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.tribesystems.payment.common.constants.Constants.API;
import static com.tribesystems.payment.common.constants.Constants.PAYMENTS;
import static com.tribesystems.payment.mpesa.constants.MpesaConstants.*;

@RestController
@RequestMapping(API + PAYMENTS)
@Tag(name = "mpesa payment controller", description = "provides endpoint to facilitate payments via mpesa")
public class PaymentController {

    private final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    private final MpesaService mpesaService;
    private final Gson gson;


    @Autowired
    public PaymentController(MpesaService mpesaService, Gson gson)
    {
        this.mpesaService = mpesaService;
        this.gson = gson;
    }

    @PostMapping(INITIATE_STK_PUSH)
    @Operation(summary = "initiate C2B payment", description = "make stk push to specified phone number with specified amount")
    public ApiResponse<InitiatePaymentResponse> initiatePayment(
            @RequestBody InitiatePaymentDto initiatePayment
            )
    {
        return mpesaService.initiatePayment(initiatePayment);
    }

    @PostMapping(CHECK_STK_PUSH_STATUS)
    @Operation(summary = "check stk push payment status", description = "check stk push transaction status")
    public ApiResponse<CheckSTKPushStatusResponse> checkStkPushTransactionStatus(
            @RequestBody CheckSTKPushStatusRequest request
            )
    {
        return mpesaService.checkStkPushStatus(request);
    }

    @PostMapping(CONFIRM_PAYMENT)
    @Operation(summary = "confirm status of transaction", description = "check the status of the transaction specified by the originator conversation ID")
    public ApiResponse<ConfirmPaymentStatusResponse> confirmPaymentStatus(
            @RequestBody CheckTxnStatusDto checkTxnStatusDto
    )
    {
        return mpesaService.checkTransactionStatus(checkTxnStatusDto);
    }

    @GetMapping(GET_ALL_TRANSACTIONS)
    @Operation(summary = "get all transactions", description = "retrieve all STK push transaction records")
    public ApiResponse<List<Transaction>> getAllTransactions()
    {
        logger.info("========================== Fetching all STK push transactions ======================");
        return mpesaService.getAllTransactions();
    }

    @PostMapping(CONFIRM_PAYMENT_RESULT)
    @Operation(summary = "confirm payment result callback", description = "invoked by daraja api with the result of the payment confirmation")
    public ApiResponse<String> confirmPaymentStatusResultCallback(
            @RequestBody String rawBody
    )
    {
        logger.info("=========================== received result in confirm payment timeout callback ===========================");
        logger.error("{}", rawBody);
        return new ApiResponse<>(
                200,
                "success",
                "confirm payment status result callback received payload"
        );
    }

    @PostMapping(CONFIRM_PAYMENT_TIMEOUT)
    @Operation(summary = "confirm payment timeout callback", description = "invoked by daraja api with the result of the payment confirmation that timed out")
    public ApiResponse<String> confirmPaymentStatusTimeoutCallback(
            @RequestBody String rawBody
    )
    {
        logger.info("=========================== received result in confirm payment timeout callback ===========================");
        logger.error("{}", rawBody);
        return new ApiResponse<>(
                200,
                "success",
                "confirm payment status timeout callback received payload"
        );
    }
//
//    @PostMapping(INITIATE_STK_PUSH_CALLBACK)
//    @Operation(summary = "initiate stk push callback", description = "invoked by daraja api with the result of an stk push")
//    public ApiResponse<String> mpesaPaymentCallback(
//            @RequestBody StkCallback stkCallback
//    )
//    {
//        logger.info("========================== Received stk callback ======================");
//        logger.error("{}", stkCallback);
//        return new ApiResponse<>(
//                200,
//                "success",
//                "mpesa payment callback received payload"
//        );
//    }
@PostMapping(INITIATE_STK_PUSH_CALLBACK)
@Operation(summary = "initiate stk push callback", description = "invoked by daraja api with the result of an stk push")
public ApiResponse<String> mpesaPaymentCallback(@RequestBody String rawBody)
{
    logger.info("========================== Received stk callback (raw) ======================");
    logger.info("Raw body: {}", rawBody);

    try {
        StkCallbackEnvelope envelope = gson.fromJson(rawBody, StkCallbackEnvelope.class);
        StkCallback stkCallback = envelope.Body().stkCallback();
        logger.info("Parsed successfully: {}", stkCallback);

        if(stkCallback.CallbackMetadata() != null && stkCallback.CallbackMetadata().Item() != null)
        {
            logger.info("================================= callback metadata items ===================================");
            for(CallbackMetadataItem item : stkCallback.CallbackMetadata().Item())
            {
                logger.info("{}", item);
            }
            logger.info("================================= end of callback metadata items ===================================");
        }
        else{
            logger.info("================================= CallbackMetadata Items is null ===================================");
        }
         mpesaService.processStkPushCallback(stkCallback); // wire this in once parsing is confirmed
    } catch (Exception e) {
        logger.error("Failed to parse STK callback body. Raw payload was: {}", rawBody, e);
    }

    return new ApiResponse<>(200, "success", "mpesa payment callback received payload");
}

    @PostMapping(REGISTER_C_2_B_URL_CALLBACKS)
    @Operation(summary = "trigger register c2b callbacks", description = "register callbacks for payment validation and confirmation notifications")
    public ApiResponse<RegisterC2BUrlsResponse> registerC2BCallbacks()
    {
        logger.info("========================== Registering C2B payment notification callbacks ======================");
        return mpesaService.registerC2BValidationAndConfirmationCallbacks();
    }

    @PostMapping(SIMULATE_C2B_TRANSACTION)
    @Operation(summary = "simulate c2b transaction to test callbacks", description = "simulate transaction to test validation and confirmation callbacks")
    public ApiResponse<SimulatePaymentForC2BResponse> simulateC2BTransaction(@RequestParam("amount") int amount)
    {
        logger.info("========================== simulating a C2B transaction ======================");
        return mpesaService.simulateTransaction(amount);
    }

    @PostMapping(C2B_VALIDATION)
    @Operation(summary = "validate c2b transactions", description = "validate c2b transactions")
    public ValidateC2BPaymentResponse c2bTransactionValidation(@RequestBody String rawBody)
    {
        logger.info("========================== Received a C2B transaction validation request ======================");
        logger.info("Validating transaction: {}", rawBody);
//        return mpesaService.transactionValidationCallback(req);
        return new ValidateC2BPaymentResponse(
                "200",
                "Success"
        );
    }

    @PostMapping(C2B_CONFIRMATION)
    @Operation(summary = "confirm c2b transactions", description = "receive confirmation notifications for c2b transactions")
    public void c2bTransactionConfirmation(@RequestBody String rawBody)
    {
        logger.info("========================== Received a C2B transaction confirmation notification ======================");
        logger.info("Confirmed transaction: {}", rawBody);
//        mpesaService.transactionConfirmationCallback(req);
    }

    @GetMapping(C2B_GET_ALL_TRANSACTIONS)
    @Operation(summary = "get all transactions", description = "retrieve all C2B transaction records")
    public ApiResponse<List<ConfirmedTransaction>> c2bGetAllTransactions()
    {
        logger.info("========================== Fetching all C2B transactions ======================");
        return mpesaService.getAllC2BTransactions();
    }


    @PostMapping(B2C_INITIATE_PAYMENT)
    @Operation(summary = "initiate b2c payment", description = "initiate b2c payment for merchant")
    public ApiResponse<B2CResponse> b2cInitiatePayment(@RequestBody InitiatePaymentDto dto)
    {
        logger.info("========================== Received a B2C payment initiation request ======================");
        logger.info("Payment initiation request: {}", dto);
        return mpesaService.initiateB2Cpayment(dto);
    }

    @GetMapping(B2C_GET_ALL_PAYMENTS)
    @Operation(summary = "get all payments", description = "retrieve all B2C payments")
    public ApiResponse<List<Payment>> b2cGetAllPayments()
    {
        logger.info("========================== Fetching all B2C payments ======================");
        return mpesaService.getAllB2CPayments();
    }

    @PostMapping(B2C_CALLBACK_RESULT)
    @Operation(summary = "Receive B2C callback result", description = "Receive and process B2C Callback result")
    public void b2cCallbackResult(@RequestBody String rawBody)
    {
        logger.info("========================== Received B2C Callback Result ======================");
        logger.info("B2C Callback: {}", rawBody);
//        mpesaService.processB2CCallbackResult(result);
    }

    @PostMapping(B2C_CALLBACK_TIMEOUT)
    @Operation(summary = "Receive B2C callback timeout", description = "Receive and process B2C Callback result")
    public void b2cCallbackTimeout(@RequestBody String rawBody)
    {
        logger.info("========================== Received B2C Callback Timeout ======================");
        logger.info("B2C Callback: {}", rawBody);
//        mpesaService.processB2CCallbackTimeout(result);
    }

    @PostMapping(B2B_INITIATE_PAYMENT)
    @Operation(summary = "Initiate B2B payment request", description = "Initiate B2B payment request")
    public ApiResponse<B2BTransactionResponse> initiateB2BTransaction(@RequestBody InitiateB2BTransactionDto dto)
    {
        logger.info("========================== Initiating B2B Payment ======================");
        return mpesaService.initiateB2Bpayment(dto);
    }

    @PostMapping(B2B_PAYMENT_REQUEST_CALLBACK)
    @Operation(summary = "Process B2B transaction responses from daraja", description = "Process B2B transaction responses from daraja")
    public void b2bCallback(@RequestBody String rawBody)
    {
        logger.info("========================== Processing B2B Transaction Response ======================");
        logger.info("B2B Callback: {}", rawBody);
//        mpesaService.processB2BTransactionCallbackRequest(response);
    }

    @GetMapping(B2B_GET_ALL_TRANSACTIONS)
    @Operation(summary = "Fetch All B2B transactions", description = "Fetch all B2B transactions")
    public ApiResponse<List<B2BTransaction>> getAllB2BTransactions()
    {
        logger.info("========================== Processing B2B Transaction Response ======================");
        return mpesaService.getAllB2BTransactions();
    }
}
