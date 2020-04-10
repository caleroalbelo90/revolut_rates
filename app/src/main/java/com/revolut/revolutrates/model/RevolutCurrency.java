package com.revolut.revolutrates.model;

import com.google.gson.annotations.SerializedName;

public class RevolutCurrency {

    @SerializedName("baseCurrency")
    private String baseCurrency;

    @SerializedName("rates")
    Rates rates;

    // Getter Methods
    public String getBaseCurrency() {
        return baseCurrency;
    }

    public Rates getRates() {
        return rates;
    }

    // Setter Methods
    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public void setRates(Rates ratesObject) {
        this.rates = ratesObject;
    }

}