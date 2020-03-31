package com.market.products.exceptions.custom;

import com.market.products.exceptions.exceptionhandlers.ApiError;

public class BaseHttpException extends RuntimeException {
    private ApiError apiError;

    public BaseHttpException(ApiError apiError) {
        this.apiError = apiError;
    }

    public ApiError getApiError() {
        return this.apiError;
    }
}
