package com.birhman.currencyrate.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CurrencyResponse {
    @SerializedName("base")
    @Expose
    private String base;
    @SerializedName("rates")
    @Expose
    private RatesModel rates;
    @SerializedName("date")
    @Expose
    private String date;

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public RatesModel getRates() {
        return rates;
    }

    public void setRates(RatesModel rates) {
        this.rates = rates;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
