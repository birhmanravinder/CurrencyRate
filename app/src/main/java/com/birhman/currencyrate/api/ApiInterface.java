package com.birhman.currencyrate.api;

import com.birhman.currencyrate.model.CurrencyResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET(AppConfig.URL_GET_CURRENCY_RATE)
    Call<CurrencyResponse> getCurrencyList();
}
