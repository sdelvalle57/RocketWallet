package com.digitalcurrencyexperts.rocketwallet.common;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalcurrencyexperts.rocketwallet.R;


/**
 * Created by santiagp on 23/08/2017.
 */

public class PopUpBackupWallet {
    private View principalLayout;
    private Context context;
    private boolean isOpen = false;
    private PopupWindow pwindo;
    private String phrase;
    private TextView tvPhrase;

    public PopUpBackupWallet(Context context, String phrase) {
        this.context = context;
        this.phrase = phrase;
        setView();
        setValues();
    }

    private void setView() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        principalLayout = inflater.inflate(R.layout.popup_backup_wallet, null);
        pwindo = new PopupWindow(principalLayout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        pwindo.setAnimationStyle(R.style.popup_animation);
        pwindo.setOutsideTouchable(true);
        pwindo.setTouchInterceptor(check_if_outside);
        pwindo.setFocusable(true);
        pwindo.setBackgroundDrawable(new BitmapDrawable(context.getResources(), "Bitmap"));
        principalLayout.findViewById(R.id.tv_title).setOnClickListener(v -> hidePopUp());
        tvPhrase = (TextView) principalLayout.findViewById(R.id.tv_phrase);
    }

    private void setValues(){
        tvPhrase.setText(phrase);
        tvPhrase.setOnLongClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(null, phrase);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, context.getString(R.string.copied_to_clipboard), Toast.LENGTH_SHORT).show();
            tvPhrase.setTextColor(ContextCompat.getColor(context, android.R.color.holo_blue_light));
            goToNormal();
            return false;
        });
        ((ImageView)principalLayout.findViewById(R.id.ivQRcode)).setImageBitmap(new QrCreator().createQrFromText(phrase));
    }

    private void goToNormal (){
        Handler handler = new Handler();
        handler.postDelayed(() -> tvPhrase.setTextColor(ContextCompat.getColor(context, android.R.color.white)), 1000);
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


