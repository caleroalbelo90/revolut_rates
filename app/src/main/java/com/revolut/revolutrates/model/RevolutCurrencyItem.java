package com.revolut.revolutrates.model;

public class RevolutCurrencyItem {

    String currency;

    String countryCode;

    String currencyCompleteName;

    float rate;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCurrencyCompleteName() {
        return currencyCompleteName;
    }

    public void setCurrencyCompleteName(String currencyCompleteName) {
        this.currencyCompleteName = currencyCompleteName;
    }

}