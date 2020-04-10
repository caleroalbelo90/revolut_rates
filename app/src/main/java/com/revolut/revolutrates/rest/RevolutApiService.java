package com.revolut.revolutrates.rest;

import com.revolut.revolutrates.model.RevolutCurrency;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RevolutApiService {

    @GET("android/latest")
    Call<RevolutCurrency> getAllRates(@Query("base") String base);

}