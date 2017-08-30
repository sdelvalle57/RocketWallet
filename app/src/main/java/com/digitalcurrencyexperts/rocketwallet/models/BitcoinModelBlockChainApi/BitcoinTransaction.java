package com.digitalcurrencyexperts.rocketwallet.models.BitcoinModelBlockChainApi;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by santiagp on 10/07/2017.
 */

public class BitcoinTransaction {
    @SerializedName("raw") private String raw;
    @SerializedName("hash") private String hash;
    @SerializedName("first_seen_at") private String firstSeenAt;
    @SerializedName("last_seen_at") private String lastSeenAt;
    @SerializedName("block_height") private int blockHeight;
    @SerializedName("block_time") private String blockTime;
    @SerializedName("confirmations") private int confirmations;
    @SerializedName("is_coinbase") private boolean isCoinbase;
    @SerializedName("estimated_value") private long estimatedValue;
    @SerializedName("total_input_value") private long totalInputValue;
    @SerializedName("total_output_value") private long totalOutputValue;
    @SerializedName("total_fee") private long totalFee;
    @SerializedName("estimated_change") private long estimatedChange;
    @SerializedName("estimated_change_address") private String estimatedChangeAddress;
    @SerializedName("high_priority") private boolean highPriority;
    @SerializedName("enough_fee") private boolean enoughFee;
    @SerializedName("contains_dust") private boolean containsDust;
    @SerializedName("inputs") private ArrayList<BitcoinTxInput> bitcoinTxInputs;
    @SerializedName("outputs") private ArrayList<BitcoinTxOutput> bitcoinTxOutputs;
    private int blockDepth =0;
    private boolean isIncome;
    private String myAddress;
    private float estimatedValueFiat;

    public String getRaw() {
        return raw;
    }
    public String getHash() {
        return hash;
    }
    public String getFirstSeenAt() {
        return firstSeenAt;
    }
    public String getLastSeenAt() {
        return lastSeenAt;
    }
    public int getBlockHeight() {
        return blockHeight;
    }
    public String getBlockTime() {
        return blockTime;
    }
    public int getConfirmations() {
        return confirmations;
    }
    public boolean isCoinbase() {
        return isCoinbase;
    }
    public long getEstimatedValue() {
        return estimatedValue;
    }
    public long getTotalInputValue() {
        return totalInputValue;
    }
    public long getTotalOutputValue() {
        return totalOutputValue;
    }
    public long getTotalFee() {
        return totalFee;
    }
    public long getEstimatedChange() {
        return estimatedChange;
    }
    public String getEstimatedChangeAddress() {
        return estimatedChangeAddress;
    }
    public boolean isHighPriority() {
        return highPriority;
    }
    public boolean isEnoughFee() {
        return enoughFee;
    }
    public boolean isContainsDust() {
        return containsDust;
    }
    public ArrayList<BitcoinTxInput> getBitcoinTxInputs() {
        return bitcoinTxInputs;
    }
    public ArrayList<BitcoinTxOutput> getBitcoinTxOutputs() {
        return bitcoinTxOutputs;
    }
    public int getBlockDepth() {
        return blockDepth;
    }
    public boolean isIncome() {
        return isIncome;
    }
    public String getMyAddress() {
        return myAddress;
    }
    public float getEstimatedValueFiat() {
        return estimatedValueFiat;
    }

    public void setBlockDepth(int blockDepth) {
        this.blockDepth = blockDepth;
    }
    public void setIncome(boolean income) {
        isIncome = income;
    }
    public void setMyAddress(String myAddress) {
        this.myAddress = myAddress;
    }
    public void setEstimatedValueFiat(float estimatedValueFiat) {
        this.estimatedValueFiat = estimatedValueFiat;
    }
    public void setEstimatedValue(long estimatedValue) {
        this.estimatedValue = estimatedValue;
    }
}

