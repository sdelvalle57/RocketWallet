package com.digitalcurrencyexperts.rocketwallet.models.BitcoinModelBlockChainApi;

import com.google.gson.annotations.SerializedName;

/**
 * Created by santiagp on 20/07/2017.
 */

public class BitcoinFee {
    @SerializedName("optimal") private int optimal;
    @SerializedName("low_priority") private int lowPriority;

    public int getOptimal() {
        return optimal;
    }
    public int getLowPriority() {
        return lowPriority;
    }
}
