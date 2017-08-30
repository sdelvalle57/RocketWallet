package com.digitalcurrencyexperts.rocketwallet.common;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.digitalcurrencyexperts.rocketwallet.R;
import com.digitalcurrencyexperts.rocketwallet.models.CoinModel;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by santiagp on 19/07/2017.
 */

public class DialogSend extends Dialog {
    private CameraSource cameraSource;
    private BarcodeDetector barcodeDetector;
    private boolean camVisible = false;
    private SurfaceView cameraView;
    private Context context;
    private float fiatValue;
    private TextView etAmount;
    private TextView etAmountFiat;
    private boolean enableAmountListener = true;
    private TextView etAddress;
    private float maxSpendable;
    private TextView tvMaxSpendable;
    private Interfaces.GetFee getFee;
    private CoinModel coinModel;

    public DialogSend(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.send);
        setContentView(R.layout.dialog_send);
        getWindow().getAttributes().windowAnimations = R.style.customAnimations_slide;
        barcodeDetector = new BarcodeDetector.Builder(getContext()).setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE).build();
        ImageView ivQr = (ImageView) findViewById(R.id.iv_qr);
        etAmount = (TextView) findViewById(R.id.et_amount);
        etAmountFiat = (TextView) findViewById(R.id.et_amount_fiat);
        etAddress = (TextView) findViewById(R.id.et_address);
        tvMaxSpendable = (TextView) findViewById(R.id.tv_max_expendable);
        //spinnerFee = (Spinner) findViewById(R.id.sp_fee);

        tvMaxSpendable.setText(String.format(Locale.ENGLISH, "%.6f", maxSpendable));
        Button send = (Button) findViewById(R.id.bt_send);
        addTextViewLesteners();

//        spinnerFee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                calculateFee();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
        ivQr.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                if (!camVisible) {
                    setCamera();
                    cameraView.setVisibility(View.VISIBLE);
                    configBarcode();
                    camVisible = true;
                    clearFocus();
                }else {
                    stopCamera();
                }
            }
        });

        tvMaxSpendable.setOnClickListener(v -> {
            clearFocus();
            etAmount.setText(String.format(Locale.ENGLISH, "%.6f", maxSpendable));
        });

        send.setOnClickListener(v -> {
            clearFocus();
            try {
                if (etAmount.getText().length() > 0 && Float.valueOf(etAmount.getText().toString()) > 0 && getFee != null && etAddress.getText().length() == 34) {
                    getFee.onSendPressed(etAddress.getText().toString(), etAmount.getText().toString(), null, etAmount.getText().toString().equals(tvMaxSpendable.getText().toString()));
                    dismiss();
                }
            }catch (NumberFormatException e){

            }
        });
    }

    private void clearFocus() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void createSurfaceView() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll_camera);
        cameraView = new SurfaceView(context);
        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250, context.getResources().getDisplayMetrics());
        cameraView.setLayoutParams(new LinearLayout.LayoutParams(size, size));
        cameraView.setVisibility(View.GONE);
        ll.addView(cameraView);
    }

    private void stopCamera() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll_camera);
        if (cameraSource!=null && cameraView!=null) {
            ll.removeView(cameraView);
            cameraSource.stop();
            camVisible = false;
        }
    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(cancel);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void onBackPressed() {
        stopCamera();
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        stopCamera();
        super.onStop();
    }


    private void setCamera() {
        createSurfaceView();
        cameraSource = new CameraSource
                .Builder(getContext(), barcodeDetector)
                .setRequestedPreviewSize(640, 480)
                .setAutoFocusEnabled(true)
                .build();
        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    cameraSource.start(cameraView.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });
    }
    private void configBarcode() {
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }
            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0 ) {
                    String value = barcodes.valueAt(0).displayValue;
                    if (value!=null && value.length()>0){
                        readValues(value);
                    }
                };
            }
        });
    }

    private void readValues(String value) {
        String coinName = null;
        String addressAndAmount = null;
        String address = null;
        String amount = null;
        if (value.contains(":")){
            coinName = value.split(":")[0];
            addressAndAmount = value.split(":")[1];
        }else addressAndAmount = value;

        if (addressAndAmount.contains("?")){
            address = addressAndAmount.split(Pattern.quote("?"))[0];
            if(addressAndAmount.split(Pattern.quote("?"))[1].contains("=")){
                amount = addressAndAmount.split(Pattern.quote("?"))[1].split("=")[1];
            }
        }else address = addressAndAmount;

        String finalAddress = address;
        String finalAmount = amount;
        ((AppCompatActivity)context).runOnUiThread(() -> {
            if (finalAddress !=null) etAddress.setText(finalAddress);
            if (finalAmount !=null)  etAmount.setText(finalAmount);
            enableAmountListener = true;
            stopCamera();
        });
    }

    public void setFiatValue(float fiatValue) {
        this.fiatValue = fiatValue;
    }

    public void setMaxSpendable(float maxSpendable) {
        this.maxSpendable = maxSpendable;
    }

    private void addTextViewLesteners() {
        setAmountListener();
        setFiatAmountListener();
        setAddressListener();
    }

    public void setTotalFee(String totalFee) {
        if (totalFee != null) {
            Float floatFee = Float.valueOf(totalFee);
            Float amount = Float.valueOf(etAmount.getText().toString());
            ((TextView) findViewById(R.id.tv_fee)).setText(totalFee.concat(" ").concat(coinModel.getCoinAbb()));
            ((TextView) findViewById(R.id.tv_fee_fiat)).setText(String.format(Locale.ENGLISH, "%.3f", floatFee * fiatValue).concat(" ".concat(coinModel.getFiat())));
            ((TextView) findViewById(R.id.tv_total)).setText(String.format(Locale.ENGLISH, "%.6f", amount - floatFee).concat(" ".concat(coinModel.getCoinAbb())));
            ((TextView) findViewById(R.id.tv_total_fiat)).setText(String.format(Locale.ENGLISH, "%.3f", (amount - floatFee) * fiatValue).concat(" ").concat(coinModel.getFiat()));
        }else {
            ((TextView) findViewById(R.id.tv_fee)).setText("");
            ((TextView) findViewById(R.id.tv_fee_fiat)).setText("");
            ((TextView) findViewById(R.id.tv_total)).setText("");
            ((TextView) findViewById(R.id.tv_total_fiat)).setText("");

        }
    }

    public void setGetFee(Interfaces.GetFee getFee) {
        this.getFee = getFee;
    }

    private void setAmountListener() {
        etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etAmount.getText().length()>0 && getFee!=null) calculateFee();
                if ( (etAmount.getText().length()>0) && enableAmountListener) {
                    try {
                        String fiatAmount = String.format(Locale.ENGLISH, "%.3f", Float.valueOf(etAmount.getText().toString()) * fiatValue);
                        etAmountFiat.setText(fiatAmount);
                    }catch (NumberFormatException e){
                        etAmount.setText("");
                    }

                }else if (enableAmountListener) etAmountFiat.setText("");
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setAddressListener(){
        etAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etAddress.getText().length()==coinModel.getAddressLenght()) calculateFee();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void calculateFee() {

        try {
            if (Float.parseFloat(etAmount.getText().toString())>0 && etAmount.getText().length() > 0 && getFee != null && etAddress.getText().length() == 34) {
                getFee.onValueChanged(etAddress.getText().toString(), etAmount.getText().toString(), null, etAmount.getText().toString().equals(tvMaxSpendable.getText().toString()));
            }
        }catch (NumberFormatException e){

        }
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
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void setCoinModel(CoinModel coinModel) {
        this.coinModel = coinModel;
    }
}
