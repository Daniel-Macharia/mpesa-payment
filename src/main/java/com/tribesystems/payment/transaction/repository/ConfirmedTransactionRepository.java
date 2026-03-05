package com.tribesystems.payment.transaction.repository;

import com.tribesystems.payment.transaction.model.ConfirmedTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmedTransactionRepository extends JpaRepository<ConfirmedTransaction, Long> {
    Optional<ConfirmedTransaction> findConfirmedTransactionByMpesaTransactionId(String mpesaTransactionId);
}
