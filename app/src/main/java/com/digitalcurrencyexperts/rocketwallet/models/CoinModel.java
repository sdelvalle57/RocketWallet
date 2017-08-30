package com.digitalcurrencyexperts.rocketwallet.models;

import android.content.Context;

import com.digitalcurrencyexperts.rocketwallet.common.Constants;
import com.digitalcurrencyexperts.rocketwallet.common.UserLocalSettings;

/**
 * Created by santiagp on 27/07/2017.
 */

public class CoinModel {

    private String coinAbb;
    private String coinName;
    private String fiat;
    private ExchangeValues fiatValues;
    private int addressLenght;
    public CoinModel(String coinAbb, String coinName, int addressLenght) {
        this.coinAbb = coinAbb;
        this.coinName = coinName;
        this.addressLenght = addressLenght;
    }

    public String getCoinAbb() {
        return coinAbb;
    }
    public String getCoinName() {
        return coinName;
    }
    public String getFiat() {
        return fiat;
    }
    public ExchangeValues getFiatValues() {
        return fiatValues;
    }
    public int getAddressLenght() {
        return addressLenght;
    }

    public void setBitcoinExchane(BitcoinExchange bitcoinExchange, Context context) {
        UserLocalSettings userLocalSettings = new UserLocalSettings(context);
        this.fiat = userLocalSettings.getFiatName();
        if (fiat.equals(Constants.AUD)){
            this.fiatValues = bitcoinExchange.getAud();
        }else if (fiat.equals(Constants.USD)){
            this.fiatValues = bitcoinExchange.getUsd();
        }
    }
}
