package com.ing.imc.services;

import lombok.Getter;

@Getter
public class ErrorOrResult<R> {

    private final R result;
    private final String errorMessage;
    private final Integer errorCode;

    ErrorOrResult(R result) {
        this.result = result;
        errorMessage = null;
        errorCode = null;
    }
    ErrorOrResult(String errorMessage, Integer errorCode) {
        this.result = null;
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public boolean isSuccessful() {
        return result != null;
    }
}
