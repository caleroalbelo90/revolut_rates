package com.revolut.revolutrates.ui.main;

import android.util.Pair;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.blongho.country_data.Country;
import com.blongho.country_data.World;
import com.revolut.revolutrates.controller.Controller;
import com.revolut.revolutrates.model.RevolutCurrency;
import com.revolut.revolutrates.model.RevolutCurrencyItem;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class PageViewModel extends ViewModel implements Controller.LoadRatesCallback {

    private Timer timer = new Timer();

    private Controller controller = new Controller(this);

    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();

    private MutableLiveData<Pair<RevolutCurrencyItem, List<RevolutCurrencyItem>>> currencyMutableLiveData = new MutableLiveData<>();

    private Map<String, Float> ratesByCurrency = new HashMap<>();

    private String baseCurrency = "EUR";

    public void start() {

        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                controller.start(baseCurrency);
            }
        }, 0, 1500);

    }

    public void setIndex(int index) {
        mIndex.setValue(index);
    }

    public MutableLiveData<Pair<RevolutCurrencyItem, List<RevolutCurrencyItem>>> getCurrencyMutableLiveData() {
        return currencyMutableLiveData;
    }

    @Override
    public void onRatesLoaded(RevolutCurrency revolutCurrency) {

        for (int i = 0; i < 31; i++) {

            switch (i) {
                case 0:
                    ratesByCurrency.put("AUD", revolutCurrency.getRevolutRates().getAUD());
                    break;

                case 1:
                    ratesByCurrency.put("BGN", revolutCurrency.getRevolutRates().getBGN());
                    break;

                case 2:
                    ratesByCurrency.put("BRL", revolutCurrency.getRevolutRates().getBRL());
                    break;

                case 3:
                    ratesByCurrency.put("CAD", revolutCurrency.getRevolutRates().getCAD());
                    break;

                case 4:
                    ratesByCurrency.put("CHF", revolutCurrency.getRevolutRates().getCHF());
                    break;

                case 5:
                    ratesByCurrency.put("CNY", revolutCurrency.getRevolutRates().getCNY());
                    break;

                case 6:
                    ratesByCurrency.put("CZK", revolutCurrency.getRevolutRates().getCZK());
                    break;

                case 7:
                    ratesByCurrency.put("DKK", revolutCurrency.getRevolutRates().getDKK());
                    break;

                case 8:
                    ratesByCurrency.put("GBP", revolutCurrency.getRevolutRates().getGBP());
                    break;

                case 9:
                    ratesByCurrency.put("HKD", revolutCurrency.getRevolutRates().getHKD());
                    break;

                case 10:
                    ratesByCurrency.put("HRK", revolutCurrency.getRevolutRates().getHRK());
                    break;

                case 11:
                    ratesByCurrency.put("HUF", revolutCurrency.getRevolutRates().getHUF());
                    break;

                case 12:
                    ratesByCurrency.put("IDR", revolutCurrency.getRevolutRates().getIDR());
                    break;

                case 13:
                    ratesByCurrency.put("ILS", revolutCurrency.getRevolutRates().getILS());
                    break;

                case 14:
                    ratesByCurrency.put("INR", revolutCurrency.getRevolutRates().getINR());
                    break;

                case 15:
                    ratesByCurrency.put("ISK", revolutCurrency.getRevolutRates().getISK());
                    break;

                case 16:
                    ratesByCurrency.put("JPY", revolutCurrency.getRevolutRates().getJPY());
                    break;

                case 17:
                    ratesByCurrency.put("KRW", revolutCurrency.getRevolutRates().getKRW());
                    break;

                case 18:
                    ratesByCurrency.put("MXN", revolutCurrency.getRevolutRates().getMXN());
                    break;

                case 19:
                    ratesByCurrency.put("MYR", revolutCurrency.getRevolutRates().getMYR());
                    break;

                case 20:
                    ratesByCurrency.put("NOK", revolutCurrency.getRevolutRates().getNOK());
                    break;

                case 21:
                    ratesByCurrency.put("NZD", revolutCurrency.getRevolutRates().getNZD());
                    break;

                case 22:
                    ratesByCurrency.put("PHP", revolutCurrency.getRevolutRates().getPHP());
                    break;

                case 23:
                    ratesByCurrency.put("AUD", revolutCurrency.getRevolutRates().getPLN());
                    break;

                case 24:
                    ratesByCurrency.put("RON", revolutCurrency.getRevolutRates().getRON());
                    break;

                case 25:
                    ratesByCurrency.put("RUB", revolutCurrency.getRevolutRates().getRUB());
                    break;

                case 26:
                    ratesByCurrency.put("SEK", revolutCurrency.getRevolutRates().getSEK());
                    break;

                case 27:
                    ratesByCurrency.put("SGD", revolutCurrency.getRevolutRates().getSGD());
                    break;

                case 28:
                    ratesByCurrency.put("THB", revolutCurrency.getRevolutRates().getTHB());
                    break;

                case 29:
                    ratesByCurrency.put("USD", revolutCurrency.getRevolutRates().getUSD());
                    break;

                case 30:
                    ratesByCurrency.put("ZAR", revolutCurrency.getRevolutRates().getZAR());
                    break;

            }

        }

        List<RevolutCurrencyItem> currencyItemList = new LinkedList<>();
        RevolutCurrencyItem baseCurrencyCopy = null;

        final List<Country> countries = World.getAllCountries();

        for (String currency : ratesByCurrency.keySet()) {

            RevolutCurrencyItem currencyItem = new RevolutCurrencyItem();
            currencyItem.setCurrency(currency);
            currencyItem.setRate(ratesByCurrency.get(currency));

            for (Country country : countries) {
                if (country.getCurrency() != null && country.getCurrency().getCode().equals(currency)) {
                    currencyItem.setCountryCode(country.getAlpha2());
                    currencyItem.setCurrencyCompleteName(country.getCurrency().getName());
                    break;
                }
            }

            if (currency.toLowerCase().equals(baseCurrency.toLowerCase())) {
                baseCurrencyCopy = currencyItem;
                currencyItemList.add(0, currencyItem);
            } else {
                currencyItemList.add(currencyItem);
            }
        }

        Pair<RevolutCurrencyItem, List<RevolutCurrencyItem>> revolutCurrencyItemListPair = new Pair<>(baseCurrencyCopy ,currencyItemList);

        currencyMutableLiveData.setValue(revolutCurrencyItemListPair);
    }

    @Override
    public void onRatesFailToLoad() {

    }

    public void setCurrencyAtTop(String currency) {
        baseCurrency = currency;

        //Cancel all apiCalls
        controller.cancelCalls();

        //Stop the timer until the animation end
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}