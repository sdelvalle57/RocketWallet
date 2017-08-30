package com.digitalcurrencyexperts.rocketwallet.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by santiagp on 19/07/2017.
 */

public class BitcoinExchange {
    @SerializedName("USD") ExchangeValues usd;
    @SerializedName("JPY") ExchangeValues jpy;
    @SerializedName("CNY") ExchangeValues cny;
    @SerializedName("SGD") ExchangeValues sgd;
    @SerializedName("HKD") ExchangeValues hkd;
    @SerializedName("CAD") ExchangeValues cad;
    @SerializedName("NZD") ExchangeValues nzd;
    @SerializedName("AUD") ExchangeValues aud;
    @SerializedName("CLP") ExchangeValues clp;
    @SerializedName("GBP") ExchangeValues gbp;
    @SerializedName("DKK") ExchangeValues dkk;
    @SerializedName("SEK") ExchangeValues sek;
    @SerializedName("ISK") ExchangeValues isk;
    @SerializedName("CHF") ExchangeValues chf;
    @SerializedName("BRL") ExchangeValues brl;
    @SerializedName("EUR") ExchangeValues eur;
    @SerializedName("RUB") ExchangeValues rub;
    @SerializedName("PLN") ExchangeValues pln;
    @SerializedName("THB") ExchangeValues thb;
    @SerializedName("KRW") ExchangeValues krw;
    @SerializedName("TWD") ExchangeValues twd;

    public ExchangeValues getUsd() {
        return usd;
    }
    public ExchangeValues getJpy() {
        return jpy;
    }
    public ExchangeValues getCny() {
        return cny;
    }
    public ExchangeValues getSgd() {
        return sgd;
    }
    public ExchangeValues getHkd() {
        return hkd;
    }
    public ExchangeValues getCad() {
        return cad;
    }
    public ExchangeValues getNzd() {
        return nzd;
    }
    public ExchangeValues getAud() {
        return aud;
    }
    public ExchangeValues getClp() {
        return clp;
    }
    public ExchangeValues getGbp() {
        return gbp;
    }
    public ExchangeValues getDkk() {
        return dkk;
    }
    public ExchangeValues getSek() {
        return sek;
    }
    public ExchangeValues getIsk() {
        return isk;
    }
    public ExchangeValues getChf() {
        return chf;
    }
    public ExchangeValues getBrl() {
        return brl;
    }
    public ExchangeValues getEur() {
        return eur;
    }
    public ExchangeValues getRub() {
        return rub;
    }
    public ExchangeValues getPln() {
        return pln;
    }
    public ExchangeValues getThb() {
        return thb;
    }
    public ExchangeValues getKrw() {
        return krw;
    }
    public ExchangeValues getTwd() {
        return twd;
    }
}
