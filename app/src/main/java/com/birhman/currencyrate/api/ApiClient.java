package com.birhman.currencyrate.api;

import android.content.Context;
import android.util.Log;

import com.birhman.currencyrate.local.AppConstant;
import com.birhman.currencyrate.local.SavedPrefManager;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.Util;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiClient {
    private static ApiClient apiClient = null;
    private ApiInterface apiInterface;
    private static boolean isTrue;
    private Context context;
    private Request.Builder requestBuilder;

    ApiClient(final Context context, Boolean urlChange) {
        this.context = context;
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(interceptor);
        httpClient.protocols(Util.immutableListOf(Protocol.HTTP_1_1));
        httpClient.connectTimeout(120, TimeUnit.SECONDS);
        httpClient.readTimeout(120, TimeUnit.SECONDS);
        httpClient.writeTimeout(120, TimeUnit.SECONDS);
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                String token = SavedPrefManager.getStringPreferences(context, AppConstant.ACCESS_TOKEN);
                Log.e("token", token);
                if (isTrue) {
                    requestBuilder = original.newBuilder()
                            //  .header("Authorization", "Bearer " + token)
                            .header("Authorization", token)
                            .header("Content-Type", "application/json")
                            .method(original.method(), original.body());
                } else {
                    requestBuilder = original.newBuilder()
                            .header("Content-Type", "application/json")
                            .method(original.method(), original.body());
                }
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        Log.e("Api manager url", AppConstant.BASE_URL);
        OkHttpClient client = httpClient.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstant.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        apiInterface = retrofit.create(ApiInterface.class);
    }

    public static ApiInterface current(Context context, boolean istrue) {
        isTrue = istrue;
        if (istrue) {
            return getInstance(context, true).getApiInterface();
        } else {
            return getInstance(context, false).getApiInterface();
        }
    }

    public static ApiClient getInstance(Context context, boolean istrue) {
        if (istrue) {
            if (apiClient == null) {
                apiClient = new ApiClient(context, istrue);
            }
        } else {
            if (apiClient == null) {
                apiClient = new ApiClient(context, false);
            }
        }
        return apiClient;
    }

    private ApiInterface getApiInterface() {
        return apiInterface;
    }
}
