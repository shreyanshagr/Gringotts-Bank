package com.gringotts.accountservice.model.dto;

import com.gringotts.accountservice.model.AccountStatus;
import lombok.Data;

@Data
public class AccountStatusUpdate {
    AccountStatus accountStatus;
}
