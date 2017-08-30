package com.digitalcurrencyexperts.rocketwallet.models.BitcoinModelBlockChainApi;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by santiagp on 10/07/2017.
 */

public class BitcoinTxOutput {
    @SerializedName("index") private int index;
    @SerializedName("value") private long value;
    @SerializedName("address") private String address;
    @SerializedName("type") private String type;
    @SerializedName("multisig") private String multisig;
    @SerializedName("multisig_addresses") private ArrayList<String> multisigAddresses;
    @SerializedName("script") private String script;
    @SerializedName("script_hex") private String scriptHex;
    @SerializedName("spent_hash") private String spentHash; //If null is UTXO
    @SerializedName("spent_index") private int spent_index;
    private String fromAddress;

    public int getIndex() {
        return index;
    }
    public long getValue() {
        return value;
    }
    public String getAddress() {
        return address;
    }
    public String getType() {
        return type;
    }
    public String getMultisig() {
        return multisig;
    }
    public String getScript() {
        return script;
    }
    public String getScriptHex() {
        return scriptHex;
    }
    public String getSpentHash() {
        return spentHash;
    }
    public int getSpentIndex() {
        return spent_index;
    }
    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }
}
