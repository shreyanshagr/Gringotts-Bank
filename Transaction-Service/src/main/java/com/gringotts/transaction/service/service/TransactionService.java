package com.gringotts.transaction.service.service;

import com.gringotts.transaction.service.model.dto.TransactionDto;
import com.gringotts.transaction.service.model.response.Response;
import com.gringotts.transaction.service.model.response.TransactionRequest;


import java.util.List;

public interface TransactionService {

    Response addTransaction(TransactionDto transactionDto);

    Response internalTransaction(List<TransactionDto> transactionDtos, String transactionReference);

    List<TransactionRequest> getTransaction(String accountId);

    List<TransactionRequest> getTransactionByTransactionReference(String transactionReference);
}
