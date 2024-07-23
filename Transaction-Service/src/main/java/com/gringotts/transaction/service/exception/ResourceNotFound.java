package com.gringotts.transaction.service.exception;

public class ResourceNotFound extends GlobalException {

    public ResourceNotFound(String errorCode, String message) {
        super(errorCode, message);
    }
}
