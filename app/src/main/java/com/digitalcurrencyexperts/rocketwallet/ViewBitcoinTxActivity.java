package com.digitalcurrencyexperts.rocketwallet;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.digitalcurrencyexperts.rocketwallet.common.Constants;
import com.digitalcurrencyexperts.rocketwallet.common.DialogInputOutput;
import com.digitalcurrencyexperts.rocketwallet.common.PopUpTxWindow;
import com.digitalcurrencyexperts.rocketwallet.models.BitcoinModelBlockChainApi.BitcoinTransaction;
import com.digitalcurrencyexperts.rocketwallet.models.BitcoinModelBlockChainApi.BitcoinTxInput;
import com.digitalcurrencyexperts.rocketwallet.models.BitcoinModelBlockChainApi.BitcoinTxOutput;
import com.google.gson.Gson;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

/**
 * Created by santiagp on 28/07/2017.
 */

@EActivity(R.layout.activity_bitcoin_tx)
public class ViewBitcoinTxActivity extends BaseActivity {
    @ViewById protected LinearLayout llInputs;
    @ViewById protected LinearLayout llTx;
    @ViewById protected LinearLayout llOutputs;
    @ViewById protected TextView tvExplorer;
    @Extra(Constants.TX_STRING)
    String txString;
    @Extra(Constants.ADDRESSES)
    ArrayList<String> addresses;
    private BitcoinTransaction bitcoinTransaction;
    private DialogInputOutput dialogInputOutput;

    @AfterViews
    protected void initUI() {
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bitcoinTransaction = new Gson().fromJson(txString, BitcoinTransaction.class);

        addTxInfo();
        addInputs();
        addOutputs();
        setExplorer();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addTxInfo() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        View hashView = LayoutInflater.from(this).inflate(R.layout.view_tx, null, false);
        View blockHView = LayoutInflater.from(this).inflate(R.layout.view_tx, null, false);
        View blockDepthView = LayoutInflater.from(this).inflate(R.layout.view_tx, null, false);
        View totalInputValueView = LayoutInflater.from(this).inflate(R.layout.view_tx, null, false);
        View totalOutputValueView = LayoutInflater.from(this).inflate(R.layout.view_tx, null, false);
        View totalFeeView = LayoutInflater.from(this).inflate(R.layout.view_tx, null, false);
        View isCoinBaseView = LayoutInflater.from(this).inflate(R.layout.view_tx, null, false);
//        View estimatedChangeAddressView = LayoutInflater.from(this).inflate(R.layout.view_tx, null, false);

        TextView tvHash = (TextView) hashView.findViewById(R.id.tv_tx_name) ;
        TextView tvHashVal = (TextView) hashView.findViewById(R.id.tv_tx_value) ;
        TextView tvblockH = (TextView) blockHView.findViewById(R.id.tv_tx_name) ;
        TextView tvblockHVal = (TextView) blockHView.findViewById(R.id.tv_tx_value) ;
        TextView tvblockDepth = (TextView) blockDepthView.findViewById(R.id.tv_tx_name) ;
        TextView tvblockDepthVal = (TextView) blockDepthView.findViewById(R.id.tv_tx_value) ;
        TextView tvtotalInputValue = (TextView) totalInputValueView.findViewById(R.id.tv_tx_name) ;
        TextView totalInputValueVal = (TextView) totalInputValueView.findViewById(R.id.tv_tx_value) ;
        TextView tvtotalOutputValue = (TextView) totalOutputValueView.findViewById(R.id.tv_tx_name) ;
        TextView tvtotalOutputValueVal = (TextView) totalOutputValueView.findViewById(R.id.tv_tx_value) ;
        TextView tvtotalFee = (TextView) totalFeeView.findViewById(R.id.tv_tx_name) ;
        TextView tvtotalFeeVal = (TextView) totalFeeView.findViewById(R.id.tv_tx_value) ;
        TextView tvisCoinBase = (TextView) isCoinBaseView.findViewById(R.id.tv_tx_name) ;
        TextView tvisCoinBaseVal = (TextView) isCoinBaseView.findViewById(R.id.tv_tx_value) ;
////        TextView tvestimatedChangeAddress = (TextView) estimatedChangeAddressView.findViewById(R.id.tv_tx_name) ;
//        TextView tvestimatedChangeAddressVal = (TextView) estimatedChangeAddressView.findViewById(R.id.tv_tx_value) ;

        tvHash.setText(getString(R.string.hash).concat(" "));
        tvHashVal.setText(bitcoinTransaction.getHash());
        tvblockH.setText(getString(R.string.block_height).concat(" "));
        tvblockHVal.setText(String.valueOf(bitcoinTransaction.getBlockHeight()));
        tvblockDepth.setText(getString(R.string.block_depth).concat(" "));
        tvblockDepthVal.setText(String.valueOf(bitcoinTransaction.getBlockDepth()));
        tvtotalInputValue.setText(getString(R.string.total_input).concat(" "));
        totalInputValueVal.setText(String.valueOf(bitcoinTransaction.getTotalInputValue()/Constants.SATOSHI_TO_BTC).concat(" ").concat(Constants.BTC));
        tvtotalOutputValue.setText(getString(R.string.total_output).concat(" "));
        tvtotalOutputValueVal.setText(String.valueOf(bitcoinTransaction.getTotalOutputValue()/Constants.SATOSHI_TO_BTC).concat(" ").concat(Constants.BTC));
        tvtotalFee.setText(getString(R.string.total_fee).concat(" "));
        tvtotalFeeVal.setText(String.valueOf(bitcoinTransaction.getTotalFee()/Constants.SATOSHI_TO_BTC).concat(" ").concat(Constants.BTC));
        tvisCoinBase.setText(" ".concat(String.valueOf(bitcoinTransaction.isCoinbase())));
        tvisCoinBase.setText(getString(R.string.is_coinbase).concat(" "));
        tvisCoinBaseVal.setText(String.valueOf(bitcoinTransaction.isCoinbase()));
//        tvestimatedChangeAddress.setText(getString(R.string.estimated_change_address).concat(" "));
//        tvestimatedChangeAddressVal.setText(bitcoinTransaction.getEstimatedChangeAddress());

        llTx.addView(hashView, lp);
        llTx.addView(blockHView, lp);
        llTx.addView(blockDepthView, lp);
        llTx.addView(totalInputValueView, lp);
        llTx.addView(totalOutputValueView, lp);
        llTx.addView(totalFeeView, lp);
        llTx.addView(isCoinBaseView, lp);
        //llTx.addView(estimatedChangeAddressView, lp);

    }

    private void addInputs() {
        for (BitcoinTxInput bitcoinTxInput: bitcoinTransaction.getBitcoinTxInputs()){
            View view = LayoutInflater.from(this).inflate(R.layout.view_input, null, false);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
            lp.weight = 1;
            TextView tvInput = (TextView) view.findViewById(R.id.tv_input);
            TextView tvInputVal = (TextView) view.findViewById(R.id.tv_input_value);
            tvInput.setText(bitcoinTxInput.getAddress());
            tvInputVal.setText(String.valueOf(bitcoinTxInput.getValue()/Constants.SATOSHI_TO_BTC).concat(" ").concat(Constants.BTC));
            if (addresses.contains(bitcoinTxInput.getAddress())){
                tvInput.setTextColor(ContextCompat.getColor(this, R.color.color_category_0));
                tvInputVal.setTextColor(ContextCompat.getColor(this, R.color.color_category_0));
            }
            llInputs.addView(view, lp);
            setClickListener(view, bitcoinTransaction.getBitcoinTxInputs(), bitcoinTxInput);
        }
    }

    private void addOutputs(){
        for (BitcoinTxOutput bitcoinTxOutput: bitcoinTransaction.getBitcoinTxOutputs()){
            View view = LayoutInflater.from(this).inflate(R.layout.view_output, null, false);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
            lp.weight = 1;
            TextView tvOutput = (TextView) view.findViewById(R.id.tv_output);
            TextView tvOutputVal = (TextView) view.findViewById(R.id.tv_output_value);
            tvOutput.setText(bitcoinTxOutput.getAddress());
            tvOutputVal.setText(String.valueOf(bitcoinTxOutput.getValue()/Constants.SATOSHI_TO_BTC).concat(" ").concat(Constants.BTC));
            if (addresses.contains(bitcoinTxOutput.getAddress())){
                tvOutput.setTextColor(ContextCompat.getColor(this, R.color.color_category_0));
                tvOutputVal.setTextColor(ContextCompat.getColor(this, R.color.color_category_0));
            }
            llOutputs.addView(view, lp);
            setClickListener(view, bitcoinTransaction.getBitcoinTxOutputs(), bitcoinTxOutput);
        }
    }

    private void setClickListener(View view, ArrayList<BitcoinTxInput> bitcoinTxOutputs, BitcoinTxInput bitcoinTxOutput) {
        view.setOnClickListener(v -> {
            PopUpTxWindow popupTxWindow = new PopUpTxWindow(this, bitcoinTxOutputs, bitcoinTxOutput);
            popupTxWindow.showPopUp();
        });
    }

    private void setClickListener(View view, ArrayList<BitcoinTxOutput> bitcoinTxOutputs, BitcoinTxOutput bitcoinTxOutput) {
        view.setOnClickListener(v -> {
            PopUpTxWindow popupTxWindow = new PopUpTxWindow(this, bitcoinTxOutputs, bitcoinTxOutput);
            popupTxWindow.showPopUp();
            /*
            dialogInputOutput = new DialogInputOutput(this);
            dialogInputOutput.show();
            dialogInputOutput.setValues(bitcoinTxOutput);
            */

        });
    }



    private void setExplorer(){
        tvExplorer.setOnClickListener(v -> {
            String parameter = Constants.IS_PRODUCTION ? "BTC" : "tBTC";
            String url = "https://www.blocktrail.com/"+parameter+"/tx/"+bitcoinTransaction.getHash();
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        });
    }

}
