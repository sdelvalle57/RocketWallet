package com.digitalcurrencyexperts.rocketwallet.models.BitcoinModelBlockChainApi;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by santiagp on 10/07/2017.
 */

public class BitcoinTxInput {
    @SerializedName("index") private int index;
    @SerializedName("output_hash") private String outputHash;
    @SerializedName("output_index") private int outputIndex;
    @SerializedName("output_confirmed") private boolean outputConfirmed;
    @SerializedName("value") private long value;
    @SerializedName("sequence") private long sequence;
    @SerializedName("address") private String address;
    @SerializedName("type") private String type;
    @SerializedName("multisig") private String multisig;
    @SerializedName("multisig_addresses") private ArrayList<String> multisigAddresses;
    @SerializedName("script_signature") private String scriptSignature;

    public int getIndex() {
        return index;
    }
    public String getOutputHash() {
        return outputHash;
    }
    public int getOutputIndex() {
        return outputIndex;
    }
    public boolean isOutputConfirmed() {
        return outputConfirmed;
    }
    public long getValue() {
        return value;
    }
    public long getSequence() {
        return sequence;
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
    public ArrayList<String> getMultisigAddresses() {
        return multisigAddresses;
    }
    public String getScriptSignature() {
        return scriptSignature;
    }
}
