package com.gringotts.transaction.service.service.implementation;

import com.gringotts.transaction.service.exception.AccountStatusException;
import com.gringotts.transaction.service.exception.GlobalErrorCode;
import com.gringotts.transaction.service.exception.InsufficientBalance;
import com.gringotts.transaction.service.exception.ResourceNotFound;
import com.gringotts.transaction.service.external.AccountService;
import com.gringotts.transaction.service.model.TransactionStatus;
import com.gringotts.transaction.service.model.TransactionType;
import com.gringotts.transaction.service.model.dto.TransactionDto;
import com.gringotts.transaction.service.model.entity.Transaction;
import com.gringotts.transaction.service.model.external.Account;
import com.gringotts.transaction.service.model.mapper.TransactionMapper;
import com.gringotts.transaction.service.model.response.Response;
import com.gringotts.transaction.service.model.response.TransactionRequest;
import com.gringotts.transaction.service.repository.TransactionRepository;
import com.gringotts.transaction.service.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;

    private final TransactionMapper transactionMapper = new TransactionMapper();

    @Value("${spring.application.ok}")
    private String ok;

    @Override
    public Response addTransaction(TransactionDto transactionDto) {

        ResponseEntity<Account> response = accountService.readByAccountNumber(transactionDto.getAccountId());
        if (Objects.isNull(response.getBody())){
            throw new ResourceNotFound("Requested account not found on the server", GlobalErrorCode.NOT_FOUND);
        }
        Account account = response.getBody();
        Transaction transaction = transactionMapper.convertToEntity(transactionDto);
        if(transactionDto.getTransactionType().equals(TransactionType.DEPOSIT.toString())) {
            account.setAvailableBalance(account.getAvailableBalance().add(transactionDto.getAmount()));
        } else if (transactionDto.getTransactionType().equals(TransactionType.WITHDRAWAL.toString())) {
            if(!account.getAccountStatus().equals("ACTIVE")){
                log.error("account is either inactive/closed, cannot process the transaction");
                throw new AccountStatusException("account is inactive or closed");
            }
            if(account.getAvailableBalance().compareTo(transactionDto.getAmount()) < 0){
                log.error("insufficient balance in the account");
                throw new InsufficientBalance("Insufficient balance in the account");
            }
            transaction.setAmount(transactionDto.getAmount().negate());
            account.setAvailableBalance(account.getAvailableBalance().subtract(transactionDto.getAmount()));
        }

        transaction.setTransactionType(TransactionType.valueOf(transactionDto.getTransactionType()));
        transaction.setComments(transactionDto.getDescription());
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setReferenceId(UUID.randomUUID().toString());

        accountService.updateAccount(transactionDto.getAccountId(), account);
        transactionRepository.save(transaction);

        return Response.builder()
                .message("Transaction completed successfully")
                .responseCode(ok).build();
    }

    @Override
    public Response internalTransaction(List<TransactionDto> transactionDtos, String transactionReference) {

        // Convert the list of transaction DTOs to entities
        List<Transaction> transactions = transactionMapper.convertToEntityList(transactionDtos);

        // Update the status of each transaction to 'COMPLETED'
        transactions.forEach(transaction -> {
            transaction.setTransactionType(TransactionType.INTERNAL_TRANSFER);
            transaction.setStatus(TransactionStatus.COMPLETED);
            transaction.setReferenceId(transactionReference);
        });

        // Save all the completed transactions to the transaction repository
        transactionRepository.saveAll(transactions);

        // Return the response indicating the completion of the transaction
        return Response.builder()
                .responseCode(ok)
                .message("Transaction completed successfully").build();
    }

    @Override
    public List<TransactionRequest> getTransaction(String accountId) {

        return transactionRepository.findTransactionByAccountId(accountId)
                .stream().map(transaction -> {
                    TransactionRequest transactionRequest = new TransactionRequest();
                    BeanUtils.copyProperties(transaction, transactionRequest);
                    transactionRequest.setTransactionStatus(transaction.getStatus().toString());
                    transactionRequest.setLocalDateTime(transaction.getTransactionDate());
                    transactionRequest.setTransactionType(transaction.getTransactionType().toString());
                    return transactionRequest;
                }).collect(Collectors.toList());
    }

    @Override
    public List<TransactionRequest> getTransactionByTransactionReference(String transactionReference) {

        return transactionRepository.findTransactionByReferenceId(transactionReference)
                .stream().map(transaction -> {
                    TransactionRequest transactionRequest = new TransactionRequest();
                    BeanUtils.copyProperties(transaction, transactionRequest);
                    transactionRequest.setTransactionStatus(transaction.getStatus().toString());
                    transactionRequest.setLocalDateTime(transaction.getTransactionDate());
                    transactionRequest.setTransactionType(transaction.getTransactionType().toString());
                    return transactionRequest;
                }).collect(Collectors.toList());
    }
}
