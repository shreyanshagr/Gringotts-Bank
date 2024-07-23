package com.gringotts.accountservice.repository;

import com.gringotts.accountservice.model.AccountType;
import com.gringotts.accountservice.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findAccountByUserIdAndAccountType(Long userId, AccountType accountType);

    Optional<Account> findAccountByAccountNumber(String accountNumber);

    Optional<Account> findAccountByUserId(Long userId);
}
