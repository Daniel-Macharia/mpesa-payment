package com.tribesystems.payment.transaction.schedule;

import com.tribesystems.payment.mpesa.dto.CheckTxnStatusDto;
import com.tribesystems.payment.mpesa.dto.ConfirmPaymentStatusResponse;
import com.tribesystems.payment.mpesa.service.MpesaService;
import com.tribesystems.payment.transaction.enums.TransactionStatus;
import com.tribesystems.payment.transaction.model.Transaction;
import com.tribesystems.payment.transaction.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UpdateTransactionStatusSchedule {

    private final Logger logger = LoggerFactory.getLogger(UpdateTransactionStatusSchedule.class);
    private final TransactionRepository transactionRepository;
    private final MpesaService mpesaService;

    public UpdateTransactionStatusSchedule(MpesaService mpesaService, TransactionRepository transactionRepository)
    {
        this.mpesaService = mpesaService;
        this.transactionRepository = transactionRepository;
    }


    @Scheduled(cron = "0 * * * * *")//check and update status every minute
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
