package com.digitalcurrencyexperts.rocketwallet.common;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.digitalcurrencyexperts.rocketwallet.R;

/**
 * Created by santiagp on 23/08/2017.
 */

public class PopUpRecoverWallet {
    private View principalLayout;
    private Context context;
    private boolean isOpen = false;
    private PopupWindow pwindo;
    private String phrase;
    private EditText tvPhrase;
    private Interfaces.RestoreWalletCallback restoreWalletCallback;

    public PopUpRecoverWallet(Context context) {
        this.context = context;
        this.phrase = phrase;
        setView();
    }

    private void setView() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        principalLayout = inflater.inflate(R.layout.popup_recover_wallet, null);
        pwindo = new PopupWindow(principalLayout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        pwindo.setAnimationStyle(R.style.popup_animation);
        pwindo.setOutsideTouchable(true);
        pwindo.setTouchInterceptor(check_if_outside);
        pwindo.setFocusable(true);
        pwindo.setBackgroundDrawable(new BitmapDrawable(context.getResources(), "Bitmap"));
        principalLayout.findViewById(R.id.tv_title).setOnClickListener(v -> hidePopUp());
        principalLayout.findViewById(R.id.bt_restore).setOnClickListener(v -> {
            phrase = ((EditText)principalLayout.findViewById(R.id.et_phrase)).getText().toString();
            if (phrase.length()>0 && restoreWalletCallback!=null){
                hidePopUp();
                restoreWalletCallback.onRestoreClick(phrase);
            }
        });
    }

    public void setRestoreWalletCallback(Interfaces.RestoreWalletCallback restoreWalletCallback) {
        this.restoreWalletCallback = restoreWalletCallback;
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
        pwindo.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
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

    /*

     */

}


