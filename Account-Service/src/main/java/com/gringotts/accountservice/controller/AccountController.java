package com.gringotts.accountservice.controller;

import com.gringotts.accountservice.model.dto.AccountDto;
import com.gringotts.accountservice.model.dto.AccountStatusUpdate;
import com.gringotts.accountservice.model.dto.response.Response;
import com.gringotts.accountservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private  AccountService accountService;

    @PostMapping
    public ResponseEntity<Response> createAccount(@RequestBody AccountDto accountDto) {
        return new ResponseEntity<>(accountService.createAccount(accountDto), HttpStatus.CREATED);
    }

    @PatchMapping
    public ResponseEntity<Response> updateAccountStatus(@RequestParam String accountNumber,@RequestBody AccountStatusUpdate accountStatusUpdate) {
        return ResponseEntity.ok(accountService.updateStatus(accountNumber, accountStatusUpdate));
    }


    @GetMapping
    public ResponseEntity<AccountDto> readByAccountNumber(@RequestParam String accountNumber) {
        return ResponseEntity.ok(accountService.readAccountByAccountNumber(accountNumber));
    }


    @PutMapping
    public ResponseEntity<Response> updateAccount(@RequestParam String accountNumber, @RequestBody AccountDto accountDto) {
        return ResponseEntity.ok(accountService.updateAccount(accountNumber, accountDto));
    }

    @GetMapping("/balance")
    public ResponseEntity<String> accountBalance(@RequestParam String accountNumber) {
        return ResponseEntity.ok(accountService.getBalance(accountNumber));
    }

    @PutMapping("/closure")
    public ResponseEntity<Response> closeAccount(@RequestParam String accountNumber) {
        return ResponseEntity.ok(accountService.closeAccount(accountNumber));
    }

//    @GetMapping("/{userId}")
//    public ResponseEntity<AccountDto> readAccountByUserId(@PathVariable Long userId){
//        return ResponseEntity.ok(accountService.readAccountByUserId(userId));
//    }
}
