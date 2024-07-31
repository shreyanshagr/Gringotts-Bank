package com.gringotts.userservice.external;


import com.gringotts.userservice.model.external.Account;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

//@FeignClient(name = "ACCOUNT-SERVICE")
@FeignClient(url = "http://localhost:8085", value = "ACCOUNT-SERVICE")
public interface AccountService {

    @GetMapping("/accounts")
    ResponseEntity<Account> readByAccountNumber(@RequestParam String accountNumber);
}
