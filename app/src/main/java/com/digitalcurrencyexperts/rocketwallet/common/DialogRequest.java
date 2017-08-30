package com.digitalcurrencyexperts.rocketwallet.common;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalcurrencyexperts.rocketwallet.R;
import com.digitalcurrencyexperts.rocketwallet.models.CoinModel;

import java.util.Locale;

/**
 * Created by santiagp on 3/08/2017.
 */

public class DialogRequest extends Dialog {
    private Context context;
    private float fiatValue;
    private TextView etAmount;
    private TextView etAmountFiat;
    private boolean enableAmountListener = true;
    private String address;
    private CoinModel coinModel;
    private ImageView ivQr;

    public DialogRequest(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.send);
        setContentView(R.layout.dialog_request);
        getWindow().getAttributes().windowAnimations = R.style.customAnimations_slide;
        ivQr = (ImageView) findViewById(R.id.iv_qr);
        etAmount = (TextView) findViewById(R.id.et_amount);
        etAmountFiat = (TextView) findViewById(R.id.et_amount_fiat);
        addTextViewLesteners();
        buildQrString();
        setIconsListeners();

    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(cancel);
    }

    private void buildQrString() {
        new AsyncTask<Void, Void, Bitmap>(){
            @Override
            protected Bitmap doInBackground(Void... params) {
                String qrString = getQrString();
                QrCreator qrCreator = new QrCreator();
                return qrCreator.createQr(coinModel.getCoinName(), qrString);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                if (bitmap!=null) ivQr.setImageBitmap(bitmap);
            }
        }.execute();
    }

    private String getQrString() {
        String qrString = address;
        if (etAmount.getText().toString().length()>0 && Float.valueOf(etAmount.getText().toString())>0){
            qrString = qrString.concat("?").concat(Constants.AMOUNT).concat("=").concat(etAmount.getText().toString());
        }
        return qrString;
    }

    public void setFiatValue(float fiatValue) {
        this.fiatValue = fiatValue;
    }


    private void addTextViewLesteners() {
        setAmountListener();
        setFiatAmountListener();
    }

    private void setAmountListener() {
        etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ( (etAmount.getText().length()>0) && enableAmountListener) {
                    try {
                        String fiatAmount = String.format(Locale.ENGLISH, "%.3f", Float.valueOf(etAmount.getText().toString()) * fiatValue);
                        etAmountFiat.setText(fiatAmount);

                    }catch (NumberFormatException e){
                        etAmount.setText("");
                    }
                    buildQrString();

                }else if (enableAmountListener) etAmountFiat.setText("");
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setFiatAmountListener(){
        etAmountFiat.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus){
                enableAmountListener = false;
            }
            else{
                enableAmountListener = true;
            }
        });
        etAmountFiat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etAmountFiat.getText().length()>0 && !enableAmountListener) {
                    String amount = String.format(Locale.ENGLISH, "%.6f", Float.valueOf(etAmountFiat.getText().toString()) / fiatValue);
                    etAmount.setText(amount);
                }else if (!enableAmountListener) etAmount.setText("");
                buildQrString();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void setIconsListeners() {
        findViewById(R.id.iv_share).setOnClickListener(v -> {
            String s = coinModel.getCoinName().concat(":").concat(getQrString());
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, s);
            sendIntent.setType("text/plain");
            context.startActivity(sendIntent);

        });
        findViewById(R.id.tv_copy).setOnClickListener(v -> {
            String s = coinModel.getCoinName().concat(":").concat(getQrString());
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(null, s);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, context.getString(R.string.copied_to_clipboard), Toast.LENGTH_SHORT).show();
        });
    }

    public void setCoinModel(CoinModel coinModel) {
        this.coinModel = coinModel;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
