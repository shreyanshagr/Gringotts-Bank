package com.gringotts.transaction.service.repository;


import com.gringotts.transaction.service.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findTransactionByAccountId(String accountId);

    List<Transaction> findTransactionByReferenceId(String referenceId);
}
