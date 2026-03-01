package com.tribesystems.payment.transaction.repository;

import com.tribesystems.payment.transaction.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @NativeQuery(
            value = " SELECT * " +
                    " FROM transaction " +
                    " WHERE checkout_requestid = :checkoutRequestId"
    )
    Optional<Transaction> findByCheckoutRequestID(@Param("checkoutRequestId") String checkoutRequestId);

    List<Transaction> findByTransactionStatus(String transactionStatus);
}
