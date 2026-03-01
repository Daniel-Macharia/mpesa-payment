package com.tribesystems.payment.mpesa.service;

import com.google.gson.Gson;
import com.tribesystems.payment.common.dto.ApiResponse;
import com.tribesystems.payment.mpesa.dto.*;
import com.tribesystems.payment.common.utils.DateTimeUtil;
import com.tribesystems.payment.transaction.mapper.TransactionMapper;
import com.tribesystems.payment.transaction.model.Transaction;
import com.tribesystems.payment.transaction.repository.TransactionRepository;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class MpesaService {

    @Value("${mpesa.auth-url}")
    private String mpesaAuthUrl;
    @Value("${mpesa.consumer-secret}")
    private String consumerSecret;
    @Value("${mpesa.consumer-key}")
    private String consumerKey;
    @Value("${mpesa.security-credential}")
    private String securityCredential;
    @Value("${mpesa.account-ref}")
    private String accountRef;

    @Value("${mpesa.initiate-stk-push-url}")
    private String initiateStkPushUrl;
    @Value("${mpesa.query-txn-status}")
    private String queryTxnStatusUrl;

    @Value("${mpesa.passkey}")
    private String passkey;
    @Value("${mpesa.shortcode}")
    private String shortCode;
    @Value("${mpesa.till-number}")
    private String tillNumber;
    @Value("${mpesa.callback-url}")
    private String callbackUrl;

    @Value("${mpesa.confirm-result-url}")
    private String confirmPaymentResultUrl;
    @Value("${mpesa.confirm-timeout-url}")
    private String confirmPaymentQueueTimeoutUrl;

    @Autowired
    private OkHttpClient client;

    @Autowired
    private Gson gson;

    @Autowired
    private TransactionRepository transactionRepository;

    private final Logger logger = LoggerFactory.getLogger(MpesaService.class);

    public ApiResponse<AuthenticateAppResponse> authenticateWithMpesa()
    {
        try{
            logger.info("========================================Authenticating client========================================");
            String url = mpesaAuthUrl;

            String authString = consumerKey + ":" + consumerSecret;

            String encodedAuthString = Base64.getEncoder().encodeToString(authString.getBytes());

            logger.info("=======================================Encoded Url=======================================");
            logger.info(url);
            logger.info("=======================================Auth String=======================================");
            logger.info(encodedAuthString);

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("Authorization" ,"Basic " + encodedAuthString)
                    .build();

            Response resp = client.newCall(request).execute();
            logger.info("{}", resp);

            if(resp.isSuccessful() && resp.body() != null)
            {
                AuthenticateAppResponse authResp = gson.fromJson(resp.body().string(), AuthenticateAppResponse.class);
                logger.info("{}", authResp);
                return new ApiResponse<>(
                        200,
                        "success",
                        authResp
                );
            }
            else{
                return new ApiResponse<>(
                        500,
                        "failed",
                        new AuthenticateAppResponse(null, null)
                );
            }

        }catch(Exception e)
        {
            logger.error("Error: {}", e.getMessage());
            return new ApiResponse<>(
                    500,
                    "failed",
                    new AuthenticateAppResponse(null, null)
            );
        }
    }

    private String getMpesaPassword(){return Base64.getEncoder().encodeToString(new String(shortCode + passkey + DateTimeUtil.formatedDateTime()).getBytes());}

    public ApiResponse<InitiatePaymentResponse> initiatePayment(InitiatePaymentDto initiatePaymentDto)
    {
        try{
            String timestamp = DateTimeUtil.formatedDateTime();
            String password = getMpesaPassword();
            String testDesc = "Payment";
            String testAccRef = accountRef;
            String tillTransactionType = "CustomerPayBillOnline";

            InitiatePaymentRequest initReq = new InitiatePaymentRequest(
                    password,
                    shortCode,
                    timestamp,
                    "" + initiatePaymentDto.amount(),
                    initiatePaymentDto.phoneNumber(),
                    tillNumber,
                    tillTransactionType,
                    initiatePaymentDto.phoneNumber(),
                    testDesc,
                    testAccRef,
                    callbackUrl
            );
            logger.info("==================================================== Request Body ====================================================");
            logger.info("{}", initReq);

            RequestBody body = RequestBody.create(gson.toJson(initReq), MediaType.get("application/json"));
            logger.info("==================================================== Encoded Request Body ====================================================");
            logger.info("{}", body);

            String token = authenticateWithMpesa().data().access_token();

            if(token == null)
            {
                logger.info("Failed to authenticate with mpesa");
                logger.info("Token: {}", token);
                return new ApiResponse<>(
                        500,
                        "failed",
                        new InitiatePaymentResponse(null, null, -1, null, null)
                );
            }

            Request req = new Request.Builder()
                    .url(initiateStkPushUrl)
                    .addHeader("Authorization", "Bearer " + token)
                    .addHeader("Content-Type", "application/json")
                    .post(body)
                    .build();

            logger.info("===================================initiating payment===================================");
            logger.info("{}", req);

            Response resp = client.newCall(req).execute();

            if(resp.isSuccessful() && resp.body() != null)
            {
                logger.info("=================================== Status code ===================================");
                logger.info("{}", resp.code());
                String bodyStr = resp.body().string();
                logger.info("{}", bodyStr);
                InitiatePaymentResponse initPmtResp = gson.fromJson(bodyStr, InitiatePaymentResponse.class);

                logger.info("=========================================converting response to transaction=========================================");
                Transaction transaction = TransactionMapper.initiatePaymentResponseToTransactionMapper(initPmtResp, "PENDING");
                logger.info("=========================================after converting response to transaction=========================================");
                logger.info("=========================================saving transaction=========================================");
                transactionRepository.save(transaction);
                logger.info("=========================================successfully saved transaction=========================================");
                return new ApiResponse<>(
                        200,
                        "success",
                        initPmtResp
                );
            }
            else{
                logger.info("=================================== Status code ===================================");
                logger.info("{}", resp.code());
                logger.info("Failed to initiate payment");
                logger.info("{}", resp);
                return new ApiResponse<>(
                        500,
                        "failed",
                        new InitiatePaymentResponse(null, null, -1, null, null)
                );
            }
        }catch(Exception e)
        {
            logger.error("Failed to initiate stk push: {}", e.getMessage());
            return new ApiResponse<>(
                    500,
                    "failed",
                    new InitiatePaymentResponse(null, null, -1, null, null)
            );
        }
    }

    public ApiResponse<ConfirmPaymentStatusResponse> checkTransactionStatus(CheckTxnStatusDto checkTxnStatusDto)
    {
        try{
            logger.info("initiating payment");
            String token = authenticateWithMpesa().data().access_token();

            String initiator = "TRIBE_SYSTEMS";
            String commandId = "TransactionStatusQuery";
            String txnId = "";
            String idType = "4";
            String remarks = "OK";
            String occasion = "OK";

            CheckTxnStatusRequest reqData = new CheckTxnStatusRequest(
                    initiator,
                    securityCredential,
                    commandId,
                    txnId,
                    checkTxnStatusDto.originatorConversationId(),
                    shortCode,
                    idType,
                    confirmPaymentResultUrl,
                    confirmPaymentQueueTimeoutUrl,
                    remarks,
                    occasion
            );
            logger.info("=======================================Request Body=======================================");
            logger.info("{}", reqData);

            RequestBody body = RequestBody.create(
                    gson.toJson(reqData), MediaType.parse("application/json")
            );

            logger.info("=======================================Request Body=======================================");
            logger.info("{}", body.toString());
            Request request = new Request.Builder()
                    .url(queryTxnStatusUrl)
                    .addHeader("Authorization", "Bearer " + token)
                    .addHeader("Content-Type", "application/json")
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();
            logger.info("Payment checked successfully");

            if(response.isSuccessful() && response.body() != null)
            {
                logger.info("{}", response.code());
                logger.info("{}", response.message());
                String bodystr = response.body().string();
                logger.info("{}", bodystr);

                ConfirmPaymentStatusResponse confResp = gson.fromJson(bodystr, ConfirmPaymentStatusResponse.class);
                return new ApiResponse<>(
                        200,
                        "success",
                        confResp
                );
            }
            else{
                logger.info("{}", response.code());
                logger.info(response.message());
                return new ApiResponse<>(
                        500,
                        "failed",
                        new ConfirmPaymentStatusResponse(null, null, -1, null)
                );
            }
        } catch (Exception e) {
            logger.error("Error: {}", e.getMessage());
            return new ApiResponse<>(
                    500,
                    "failed",
                    new ConfirmPaymentStatusResponse(null, null, -1, null)
            );
        }
    }
}
