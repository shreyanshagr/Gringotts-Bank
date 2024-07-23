package com.gringotts.accountservice.external;

import com.gringotts.accountservice.model.dto.external.SequenceDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;


@FeignClient(name = "SEQUENCE-GENERATOR")
public interface SequenceService {

    @PostMapping("/sequence")
    SequenceDto generateAccountNumber();
}
