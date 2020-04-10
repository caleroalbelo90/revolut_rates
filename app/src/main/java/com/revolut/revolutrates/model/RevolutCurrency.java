package com.revolut.revolutrates.model;

import com.google.gson.annotations.SerializedName;

public class RevolutCurrency {

    @SerializedName("baseCurrency")
    private String baseCurrency;

    @SerializedName("rates")
    RevolutRates revolutRates;

    // Getter Methods
    public String getBaseCurrency() {
        return baseCurrency;
    }

    public RevolutRates getRevolutRates() {
        return revolutRates;
    }

    // Setter Methods
    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public void setRevolutRates(RevolutRates revolutRatesObject) {
        this.revolutRates = revolutRatesObject;
    }

}