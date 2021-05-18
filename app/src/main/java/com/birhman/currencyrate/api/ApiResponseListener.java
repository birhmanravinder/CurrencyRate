package com.birhman.currencyrate.api;

public interface ApiResponseListener<T> {
    void onApiSuccess(T response, String apiName);
    void onApiError(String responses, String apiName);
    void onApiFailure(String failureMessage, String apiName);
}