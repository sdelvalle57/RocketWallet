package com.digitalcurrencyexperts.rocketwallet.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by santiagp on 19/07/2017.
 */

public class ExchangeValues {
    @SerializedName("15m") float m15;
    @SerializedName("last") float last;
    @SerializedName("buy") float buy;
    @SerializedName("sell") float sell;
    @SerializedName("symbol")
    String symbol;

    public float getM15() {
        return m15;
    }
    public float getLast() {
        return last;
    }
    public float getBuy() {
        return buy;
    }
    public float getSell() {
        return sell;
    }
    public String getSymbol() {
        return symbol;
    }
}
