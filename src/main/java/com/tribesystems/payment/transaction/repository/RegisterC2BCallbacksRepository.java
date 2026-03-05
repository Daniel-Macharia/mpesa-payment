package com.tribesystems.payment.transaction.repository;

import com.tribesystems.payment.transaction.model.RegisterC2BCallbacksModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegisterC2BCallbacksRepository extends JpaRepository<RegisterC2BCallbacksModel, Long> {
}
