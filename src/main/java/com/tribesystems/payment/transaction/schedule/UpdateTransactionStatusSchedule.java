package com.tribesystems.payment.transaction.schedule;

import com.tribesystems.payment.mpesa.dto.CheckTxnStatusDto;
import com.tribesystems.payment.mpesa.dto.ConfirmPaymentStatusResponse;
import com.tribesystems.payment.mpesa.service.MpesaService;
import com.tribesystems.payment.mpesa.service.PaymentReferenceGenerator;
import com.tribesystems.payment.transaction.enums.TransactionStatus;
import com.tribesystems.payment.transaction.model.Transaction;
import com.tribesystems.payment.transaction.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
public class UpdateTransactionStatusSchedule {

    private final Logger logger = LoggerFactory.getLogger(UpdateTransactionStatusSchedule.class);
    private final TransactionRepository transactionRepository;
    private final MpesaService mpesaService;
    private final PaymentReferenceGenerator paymentReferenceGenerator;

    public UpdateTransactionStatusSchedule(PaymentReferenceGenerator paymentReferenceGenerator, MpesaService mpesaService, TransactionRepository transactionRepository)
    {
        this.paymentReferenceGenerator = paymentReferenceGenerator;
        this.mpesaService = mpesaService;
        this.transactionRepository = transactionRepository;
    }


    @Scheduled(cron = "0 * * * * *")//check and update status every minute
    public void updateNullPaymentReferencesOnTransactions()
    {
        try{
            List<Transaction> transactions = transactionRepository.findAll();

            for(Transaction transaction : transactions)
            {
                if(Objects.isNull(transaction.getPaymentReference()))
                {
                    //update all transactions with null payment references. Later enforce non null constraint on the column
                    transaction.setPaymentReference(transaction.getCreatedAt() + "_" + UUID.randomUUID());
                    transactionRepository.save(transaction);
                }
            }
        }catch(Exception e)
        {
            logger.error("Failed to fill the null payment trasactions");
            logger.error("{}", e.getMessage());
        }
    }

//    @Scheduled(cron = "0 * * * * *")//check and update status every minute
    public void updatePendingTransactionStatuses()
    {
        try{
            logger.info("Fetching all pending transactions");
            List<Transaction> pendingTransactions = transactionRepository.findByTransactionStatus("PENDING");
            ConfirmPaymentStatusResponse response;

            for( Transaction transaction : pendingTransactions )
            {
                response = mpesaService.checkTransactionStatus(new CheckTxnStatusDto(transaction.getCheckoutRequestID().trim())).data();
                Transaction trans = transactionRepository.findByCheckoutRequestID(transaction.getCheckoutRequestID().trim()).orElse(null);

                if(trans != null)
                {

                    if(response.ResponseCode() == 0)
                    {
                        trans.setTransactionStatus(TransactionStatus.COMPLETED.name());
                    }
                    else{
                        trans.setTransactionStatus(TransactionStatus.FAILED.name());
                    }

                    transactionRepository.save(trans);//update transaction status appropriately
                }
            }
        }catch(Exception e)
        {
            logger.error("============================================Failed to update transactions============================================");
            logger.error("{}", e.getMessage());
        }
    }
}
