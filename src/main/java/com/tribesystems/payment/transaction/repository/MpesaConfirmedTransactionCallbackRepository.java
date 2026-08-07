package com.tribesystems.payment.transaction.repository;

import com.tribesystems.payment.transaction.model.MpesaConfirmedTransactionCallback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MpesaConfirmedTransactionCallbackRepository extends JpaRepository<MpesaConfirmedTransactionCallback, Long> {
}
