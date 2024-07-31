package com.gringotts.accountservice.external;


import com.gringotts.accountservice.model.dto.external.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


//@FeignClient(name = "USER-SERVICE")
@FeignClient(url = "http://localhost:8081", value = "USER-SERVICE")
public interface UserService {
    @GetMapping("/api/users/{userId}")
    ResponseEntity<UserDto> readUserById(@PathVariable Long userId);
}