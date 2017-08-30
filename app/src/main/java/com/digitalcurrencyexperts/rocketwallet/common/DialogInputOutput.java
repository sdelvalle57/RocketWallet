package com.digitalcurrencyexperts.rocketwallet.common;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.digitalcurrencyexperts.rocketwallet.R;
import com.digitalcurrencyexperts.rocketwallet.models.BitcoinModelBlockChainApi.BitcoinTxInput;
import com.digitalcurrencyexperts.rocketwallet.models.BitcoinModelBlockChainApi.BitcoinTxOutput;

/**
 * Created by santiagp on 3/08/2017.
 */

public class DialogInputOutput extends Dialog {
    private Context context;
    private TextView title, firstTitle, first, secondTitle, second, thirdTitle, third, fourthTitle, fourth, fifthTitle, fifth, sixthTitle, sixth;

    public DialogInputOutput(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_input_output);
        getWindow().getAttributes().windowAnimations = R.style.customAnimations_slide;
        setViews();
    }

    private void setViews() {
        title = (TextView) findViewById(R.id.tv_title);
        firstTitle = (TextView) findViewById(R.id.tv_first_title);
        first = (TextView) findViewById(R.id.tv_first);
        secondTitle = (TextView) findViewById(R.id.tv_second_title);
        second = (TextView) findViewById(R.id.tv_second);
        thirdTitle = (TextView) findViewById(R.id.tv_third_title);
        third = (TextView) findViewById(R.id.tv_third);
        fourthTitle = (TextView) findViewById(R.id.tv_fourth_title);
        fourth = (TextView) findViewById(R.id.tv_fourth);
        fifthTitle = (TextView) findViewById(R.id.tv_fifth_title);
        fifth = (TextView) findViewById(R.id.tv_fifth);
        sixthTitle = (TextView) findViewById(R.id.tv_sixth_title);
        sixth = (TextView) findViewById(R.id.tv_sixth);
    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(cancel);
    }


    public void setValues(BitcoinTxOutput bitcoinTxOutput){
        if (title==null) setViews();
        ((TextView) findViewById(R.id.tv_title)).setText(context.getString(R.string.output));
        firstTitle.setText(context.getString(R.string.address).concat(" "));
        first.setText(bitcoinTxOutput.getAddress());
        secondTitle.setText(context.getString(R.string.value).concat(" "));
        second.setText(String.valueOf(bitcoinTxOutput.getValue()/Constants.SATOSHI_TO_BTC).concat(Constants.BTC));
        thirdTitle.setText(context.getString(R.string.index).concat(" "));
        third.setText(String.valueOf(bitcoinTxOutput.getIndex()));
        fourthTitle.setText(context.getString(R.string.spent_hash).concat(" "));
        fourth.setText(bitcoinTxOutput.getSpentHash());
        fifthTitle.setText(context.getString(R.string.spent_index).concat(" "));
        fifth.setText(String.valueOf(bitcoinTxOutput.getSpentIndex()));
        sixthTitle.setText(context.getString(R.string.is_utxo).concat(" "));
        sixth.setText(String.valueOf(bitcoinTxOutput.getSpentHash()==null));
    }

    public void setValues(BitcoinTxInput bitcoinTxInput){
        if (title==null) setViews();
        ((TextView) findViewById(R.id.tv_title)).setText(context.getString(R.string.input));
        firstTitle.setText(context.getString(R.string.address));
        first.setText(bitcoinTxInput.getAddress());
        secondTitle.setText(context.getString(R.string.value).concat(" "));
        second.setText(String.valueOf(bitcoinTxInput.getValue()/Constants.SATOSHI_TO_BTC).concat(Constants.BTC));
        thirdTitle.setText(context.getString(R.string.index).concat(" "));
        third.setText(String.valueOf(bitcoinTxInput.getIndex()));
        fourthTitle.setText(context.getString(R.string.output_hash).concat(" "));
        fourth.setText(bitcoinTxInput.getOutputHash());
        fifthTitle.setText(context.getString(R.string.output_index).concat(" "));
        fifth.setText(String.valueOf(bitcoinTxInput.getOutputIndex()));
        sixthTitle.setText(context.getString(R.string.type).concat(" "));
        sixth.setText(bitcoinTxInput.getType());
    }
}
