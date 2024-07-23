package com.gringotts.accountservice.service;


import com.gringotts.accountservice.model.dto.AccountDto;
import com.gringotts.accountservice.model.dto.AccountStatusUpdate;
import com.gringotts.accountservice.model.dto.external.TransactionResponse;
import com.gringotts.accountservice.model.dto.response.Response;

import java.util.List;

public interface AccountService {

    Response createAccount(AccountDto accountDto);

    Response updateStatus(String accountNumber, AccountStatusUpdate accountUpdate);

    AccountDto readAccountByAccountNumber(String accountNumber);

    Response updateAccount(String accountNumber, AccountDto accountDto);

    String getBalance(String accountNumber);

    Response closeAccount(String accountNumber);

    AccountDto readAccountByUserId(Long userId);

    public List<TransactionResponse> getTransactionsFromAccountId(String accountId);
}
