package com.gringotts.transaction.service.external;

import com.gringotts.transaction.service.configuration.FeignClientConfiguration;
import com.gringotts.transaction.service.model.external.Account;
import com.gringotts.transaction.service.model.response.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "account-service", configuration = FeignClientConfiguration.class)
public interface AccountService {

    @GetMapping("/accounts")
    ResponseEntity<Account> readByAccountNumber(@RequestParam String accountNumber);

    @PutMapping("/accounts")
    ResponseEntity<Response> updateAccount(@RequestParam String accountNumber, @RequestBody Account account);
}
