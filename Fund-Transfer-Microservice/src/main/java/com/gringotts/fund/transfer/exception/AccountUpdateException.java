package com.gringotts.fund.transfer.exception;

public class AccountUpdateException extends GlobalException{
    public AccountUpdateException(String errorCode, String message) {
        super(errorCode, message);
    }
}
