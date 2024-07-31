package com.gringotts.fund.transfer.external;

import com.gringotts.fund.transfer.configuration.FeignClientConfiguration;
import com.gringotts.fund.transfer.model.dto.Account;
import com.gringotts.fund.transfer.model.dto.response.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


//@FeignClient(name = "account-service", configuration = FeignClientConfiguration.class)
@FeignClient(url = "http://localhost:8085", value = "ACCOUNT-SERVICE")
public interface AccountService {

    @GetMapping("/accounts")
    ResponseEntity<Account> readByAccountNumber(@RequestParam String accountNumber);

    @PutMapping("/accounts")
    ResponseEntity<Response> updateAccount(@RequestParam String accountNumber, @RequestBody Account account);
}
