package com.birhman.currencyrate.api;

import android.content.Context;

import com.birhman.currencyrate.model.CurrencyResponse;

public class ApiManager extends ApiClient {
    private static ApiManager apiManager;
    private Context mContext;

    public ApiManager(Context context, Boolean apiIS) {
        super(context, apiIS);
        mContext = context;
    }

    public static ApiManager getInstance(Context context, Boolean api) {
        if (apiManager == null) {
            apiManager = new ApiManager(context, api);
        }
        return apiManager;
    }

    public void getCurrencyRateApi(ApiCallBack<CurrencyResponse> callBack) {
        ApiClient.current(mContext, false).getCurrencyList().enqueue(callBack);
    }
}
