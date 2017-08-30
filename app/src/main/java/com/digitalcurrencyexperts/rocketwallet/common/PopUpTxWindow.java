package com.digitalcurrencyexperts.rocketwallet.common;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.digitalcurrencyexperts.rocketwallet.R;
import com.digitalcurrencyexperts.rocketwallet.adapters.AdapterPagerTx;
import com.digitalcurrencyexperts.rocketwallet.models.BitcoinModelBlockChainApi.BitcoinTxInput;
import com.digitalcurrencyexperts.rocketwallet.models.BitcoinModelBlockChainApi.BitcoinTxOutput;

import java.util.ArrayList;
import java.util.Vector;


/**
 * Created by Santiago on 21/05/2015.
 */
public class PopUpTxWindow {
    private View principalLayout;
    private TextView popUpTitle;
    private Context context;
    private boolean isOpen = false;
    private ViewPager mPager;
    private PopupWindow pwindo;
    private int size;
    private ImageView goLeft, goRight;
    private LinearLayout pager_indicator;


    private int total = 0;
    public PopUpTxWindow(Context context, ArrayList<BitcoinTxOutput> bitcoinTxOutputs, BitcoinTxOutput bitcoinTxOutput) {
        this.context = context;
        this.size = bitcoinTxOutputs.size();
        setView();
        setValues(bitcoinTxOutputs, bitcoinTxOutput);
    }

    public PopUpTxWindow(Context context, ArrayList<BitcoinTxInput> bitcoinTxInputs, BitcoinTxInput bitcoinTxInput) {
        this.context = context;
        this.size = bitcoinTxInputs.size();
        setView();
        setValues(bitcoinTxInputs, bitcoinTxInput);
    }

    private void setView() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        principalLayout = inflater.inflate(R.layout.popup, null);
        pwindo = new PopupWindow(principalLayout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        pwindo.setAnimationStyle(R.style.popup_animation);
        pwindo.setOutsideTouchable(true);
        pwindo.setTouchInterceptor(check_if_outside);
        pwindo.setFocusable(true);
        pwindo.setBackgroundDrawable(new BitmapDrawable(context.getResources(), "Bitmap"));
        goLeft = (ImageView) principalLayout.findViewById(R.id.go_left);
        goRight = (ImageView) principalLayout.findViewById(R.id.go_right);
        mPager = (ViewPager) principalLayout.findViewById(R.id.view_pager);
        popUpTitle = (TextView) principalLayout.findViewById(R.id.tv_title);
        pager_indicator = (LinearLayout) principalLayout.findViewById(R.id.ll_pager_indicator) ;
        principalLayout.findViewById(R.id.tv_title).setOnClickListener(v -> hidePopUp());
    }

    public void setValues(ArrayList<BitcoinTxOutput> bitcoinTxOutputs, BitcoinTxOutput bitcoinTxOutput){
        Vector<View> pages = new Vector<>();
        for (BitcoinTxOutput thisTx: bitcoinTxOutputs){
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_input_output, null, false);
            popUpTitle.setText(context.getString(R.string.output));
            ((TextView) view.findViewById(R.id.tv_first_title)).setText(context.getString(R.string.address).concat(" "));
            ((TextView) view.findViewById(R.id.tv_first)).setText(thisTx.getAddress());
            ((TextView) view.findViewById(R.id.tv_second_title)).setText(context.getString(R.string.value).concat(" "));
            ((TextView) view.findViewById(R.id.tv_second)).setText(String.valueOf(thisTx.getValue()/Constants.SATOSHI_TO_BTC).concat(Constants.BTC));
            ((TextView) view.findViewById(R.id.tv_third_title)).setText(context.getString(R.string.index).concat(" "));
            ((TextView) view.findViewById(R.id.tv_third)).setText(String.valueOf(thisTx.getIndex()));
            ((TextView) view.findViewById(R.id.tv_fourth_title)).setText(context.getString(R.string.spent_hash).concat(" "));
            ((TextView) view.findViewById(R.id.tv_fourth)).setText(thisTx.getSpentHash());
            ((TextView) view.findViewById(R.id.tv_fifth_title)).setText(context.getString(R.string.spent_index).concat(" "));
            ((TextView) view.findViewById(R.id.tv_fifth)).setText(String.valueOf(thisTx.getSpentIndex()));
            ((TextView) view.findViewById(R.id.tv_sixth_title)).setText(context.getString(R.string.is_utxo).concat(" "));
            ((TextView) view.findViewById(R.id.tv_sixth)).setText(String.valueOf(thisTx.getSpentHash()==null));
            pages.add(view);
            setAdapter(pages);
            mPager.setCurrentItem(bitcoinTxOutput.getIndex());
        }
    }

    private void setValues(ArrayList<BitcoinTxInput> bitcoinTxInputs, BitcoinTxInput bitcoinTxInput) {
        Vector<View> pages = new Vector<>();
        for (BitcoinTxInput thisTx: bitcoinTxInputs){
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_input_output, null, false);
            popUpTitle.setText(context.getString(R.string.input));
            ((TextView) view.findViewById(R.id.tv_first_title)).setText(context.getString(R.string.address));
            ((TextView) view.findViewById(R.id.tv_first)).setText(thisTx.getAddress());
            ((TextView) view.findViewById(R.id.tv_second_title)).setText(context.getString(R.string.value).concat(" "));
            ((TextView) view.findViewById(R.id.tv_second)).setText(String.valueOf(thisTx.getValue()/Constants.SATOSHI_TO_BTC).concat(Constants.BTC));
            ((TextView) view.findViewById(R.id.tv_third_title)).setText(context.getString(R.string.index).concat(" "));
            ((TextView) view.findViewById(R.id.tv_third)).setText(String.valueOf(thisTx.getIndex()));
            ((TextView) view.findViewById(R.id.tv_fourth_title)).setText(context.getString(R.string.output_hash).concat(" "));
            ((TextView) view.findViewById(R.id.tv_fourth)).setText(thisTx.getOutputHash());
            ((TextView) view.findViewById(R.id.tv_fifth_title)).setText(context.getString(R.string.output_index).concat(" "));
            ((TextView) view.findViewById(R.id.tv_fifth)).setText(String.valueOf(thisTx.getOutputIndex()));
            ((TextView) view.findViewById(R.id.tv_sixth_title)).setText(context.getString(R.string.type).concat(" "));
            ((TextView) view.findViewById(R.id.tv_sixth)).setText(bitcoinTxInput.getType());
            pages.add(view);
        }
        setAdapter(pages);
        mPager.setCurrentItem(bitcoinTxInput.getIndex());
    }

    private void setAdapter(Vector<View> pages){
        AdapterPagerTx adapter = new AdapterPagerTx(pages);
        //ImageView[] dots = new ImageView[pages.size()];

        //int dotsCount = adapter.getCount();
        mPager.setClipToPadding(false);
        mPager.setPadding(60,0,60,0);
        mPager.setPageMargin(40);
        mPager.setAdapter(adapter);

        goLeft.setOnClickListener(v -> mPager.setCurrentItem(mPager.getCurrentItem()-1));
        goRight.setOnClickListener(v -> mPager.setCurrentItem(mPager.getCurrentItem()+1));
        /*
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dotsCount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(context, R.drawable.non_selecteditem_dot));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(context, R.drawable.vector_bitcoin));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(context);
            dots[i].setTag(i);
            dots[i].setImageDrawable(ContextCompat.getDrawable(context, R.drawable.non_selecteditem_dot));
            dots[0].setImageDrawable(ContextCompat.getDrawable(context, R.drawable.vector_bitcoin));
            int size = (int) context.getResources().getDimension(R.dimen.pager_item_size);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
            params.setMargins(4, 0, 4, 0);
            pager_indicator.addView(dots[i], params);
            dots[i].setOnClickListener(v -> {
                mPager.setCurrentItem((Integer) v.getTag());
            });
        }
        */
    }

    public void showPopUp(){
        int y = 0;
        if (Build.VERSION.SDK_INT >= 21) {
            Resources resources = context.getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                y=resources.getDimensionPixelSize(resourceId);
            }
        }
        if(size==1) {
            goLeft.setVisibility(View.INVISIBLE);
            goRight.setVisibility(View.INVISIBLE);
        }
        pwindo.showAtLocation(principalLayout, Gravity.BOTTOM, 0, y);
    }
    public void hidePopUp(){
        pwindo.dismiss();
        isOpen = false;
    }


    private View.OnTouchListener check_if_outside = (v, event) -> {
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            hidePopUp();
            return true;
        }
        return false;
    };


}


