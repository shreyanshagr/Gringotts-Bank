package com.gringotts.accountservice.external;

import com.gringotts.accountservice.configuration.FeignConfiguration;
import com.gringotts.accountservice.model.dto.external.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "USER-SERVICE")
public interface UserService {
    ResponseEntity<UserDto> readUserById(@PathVariable Long userId);
}