package com.digitalcurrencyexperts.rocketwallet.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.digitalcurrencyexperts.rocketwallet.R;
import com.digitalcurrencyexperts.rocketwallet.common.Constants;
import com.digitalcurrencyexperts.rocketwallet.common.Interfaces;
import com.digitalcurrencyexperts.rocketwallet.models.BitcoinModelBlockChainApi.BitcoinTransaction;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by santiagp on 12/07/2017.
 */

public class BitcoinTxAdapter extends RecyclerView.Adapter<BitcoinTxAdapter.ViewHolder> {

    private Context context;
    private ArrayList<BitcoinTransaction> items = new ArrayList<>();
    private Interfaces.TxListCallback txListCallback;

    public BitcoinTxAdapter(Context context, ArrayList<BitcoinTransaction> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_transaction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final BitcoinTransaction bitcoinTransaction = items.get(position);
        final float floatValue = bitcoinTransaction.getEstimatedValue();
        final float floatFiatValue = floatValue*bitcoinTransaction.getEstimatedValueFiat()/ Constants.SATOSHI_TO_BTC;
        viewHolder.tvDate.setText(formateDate(bitcoinTransaction.getFirstSeenAt()));

        int color = bitcoinTransaction.isIncome() ? ContextCompat.getColor(context, android.R.color.holo_green_light) : ContextCompat.getColor(context, android.R.color.holo_red_light);
        String plusOrMinus = bitcoinTransaction.isIncome() ? "+":"-";
        viewHolder.tvAmountBtc.setTextColor(color);
        viewHolder.tvAmountFiat.setTextColor(color);
        viewHolder.tvAmountBtc.setText(plusOrMinus.concat(String.valueOf(floatValue/ Constants.SATOSHI_TO_BTC)).concat(" BTC"));
        viewHolder.tvAmountFiat.setText(plusOrMinus.concat(String.format(Locale.ENGLISH, "%.2f", floatFiatValue)).concat(" AUD"));
        if (bitcoinTransaction.getBlockDepth()>=10){
            viewHolder.tvConfirms.setText("9");
            viewHolder.tvPlus.setVisibility(View.VISIBLE);
        }else {
            viewHolder.tvPlus.setVisibility(View.GONE);
            viewHolder.tvConfirms.setText(String.valueOf(bitcoinTransaction.getBlockDepth()));
        }
        viewHolder.mainView.setOnClickListener(v -> {
            if (txListCallback!=null) txListCallback.onTxClick(position);
        });
    }


    public BitcoinTransaction getItem(int position) {
        return items.get(position) ;
    }

    public void setTxListCallback(Interfaces.TxListCallback txListCallback) {
        this.txListCallback = txListCallback;
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvAmountBtc, tvAmountFiat, tvConfirms, tvPlus;

        LinearLayout llConfirms;
        View mainView;

        public ViewHolder(View itemView) {
            super(itemView);
            mainView = itemView;
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvAmountBtc = (TextView) itemView.findViewById(R.id.tv_amount_btc);
            tvAmountFiat = (TextView) itemView.findViewById(R.id.tv_amount_fiat);
            tvPlus = (TextView) itemView.findViewById(R.id.tv_plus);
            llConfirms = (LinearLayout) itemView.findViewById(R.id.ll_confirms);
            tvConfirms = (TextView) itemView.findViewById(R.id.tv_confirms);
        }
    }

    private String formateDate(String dateString){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ");
        Date result;
        String finalDate = dateString;
        try {
            result = df.parse(dateString);
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            finalDate = sdf.format(result);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  finalDate;
    }
}

