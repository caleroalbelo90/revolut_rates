package com.revolut.revolutrates.controller;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.revolut.revolutrates.model.RevolutCurrency;
import com.revolut.revolutrates.rest.RevolutApiService;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Controller implements Callback<RevolutCurrency> {

    private LoadRatesCallback callback;
    private static final String BASE_URL = "https://hiring.revolut.codes/api/";

    private final List<Call> apiCalls = Collections.synchronizedList(new LinkedList<Call>());

    public Controller(LoadRatesCallback callback) {
        this.callback = callback;
    }

    public void start(String baseCurrency) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RevolutApiService gerritAPI = retrofit.create(RevolutApiService.class);

        Call<RevolutCurrency> call = gerritAPI.getAllRates(baseCurrency);
        apiCalls.add(call);
        call.enqueue(this);

    }

    @Override
    public void onResponse(@NonNull Call<RevolutCurrency> call, Response<RevolutCurrency> response) {
        if (response.isSuccessful()) {

            RevolutCurrency revolutCurrency = response.body();
            callback.onRatesLoaded(revolutCurrency);

        } else {
            callback.onRatesFailToLoad();
            System.out.println(response.errorBody());
        }
    }

    @Override
    public void onFailure(@NonNull Call<RevolutCurrency> call, Throwable t) {
        t.printStackTrace();
    }

    public void cancelCalls() {
        synchronized (apiCalls) {
            for (Call call : apiCalls) {
                call.cancel();
            }

            apiCalls.clear();
        }
    }

    public interface LoadRatesCallback {
        void onRatesLoaded(RevolutCurrency revolutCurrency);

        void onRatesFailToLoad();
    }

}