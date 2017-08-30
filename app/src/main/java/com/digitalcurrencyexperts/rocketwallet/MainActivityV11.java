package com.digitalcurrencyexperts.rocketwallet;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalcurrencyexperts.rocketwallet.adapters.AdapterPagerCoins;
import com.digitalcurrencyexperts.rocketwallet.common.Constants;
import com.digitalcurrencyexperts.rocketwallet.common.DialogOptions;
import com.digitalcurrencyexperts.rocketwallet.common.DialogRequest;
import com.digitalcurrencyexperts.rocketwallet.common.DialogSend;
import com.digitalcurrencyexperts.rocketwallet.common.Interfaces;
import com.digitalcurrencyexperts.rocketwallet.common.MainAcBtcPresenter;
import com.digitalcurrencyexperts.rocketwallet.common.PopUpBackupWallet;
import com.digitalcurrencyexperts.rocketwallet.common.PopUpRecoverWallet;
import com.digitalcurrencyexperts.rocketwallet.common.QrCreator;
import com.digitalcurrencyexperts.rocketwallet.models.BitcoinExchange;
import com.digitalcurrencyexperts.rocketwallet.models.CoinModel;
import com.digitalcurrencyexperts.rocketwallet.network.NetworkService;
import com.digitalcurrencyexperts.rocketwallet.network.NetworkServiceInterface;
import com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.text.NumberFormat;
import java.util.Vector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@EActivity(R.layout.activity_main_11)
public class MainActivityV11 extends BaseActivity implements Interfaces.MainActivityContract.MainActivityView, View.OnClickListener, Interfaces.OptionsListCallback, Interfaces.RestoreWalletCallback {

    @ViewById(R.id.bt_nav_bar)
    Button btNavBar;
    @ViewById(R.id.view_pager)
    ViewPager viewPager;
    @ViewById(R.id.ll_pager_indicator)
    LinearLayout pager_indicator;
    @ViewById(R.id.ll_syncing) LinearLayout llSyncing;
   // @FragmentById(R.id.left_drawer) MenuFragment drawerFragment;

    @ViewById protected RecyclerView rvTransactionList;
    @ViewById protected TextView tvBalance;
    @ViewById protected TextView tvBalanceFiat;
    @ViewById protected TextView tvSyncing;
    @ViewById protected TextView tvSyncingMessage;
    @ViewById protected ImageView ivQRcode;
    @ViewById protected ImageView ivCopyAdress;
    @ViewById protected ImageView expandedQR;
    @ViewById protected TextView tvAddress;
    @ViewById protected FABToolbarLayout fabToolbar;
    @ViewById protected FloatingActionButton fab;
    @ViewById protected DrawerLayout drawer_layout;
    @ViewById protected LinearLayout request, options, send, close;
    @SystemService
    protected ClipboardManager clipboardManager;

    private Interfaces.MainActivityContract.MainActivityPresenter presenter;
    private CoinModel coinModel;
    private DialogSend sendDialog;
    private DialogRequest requestDialog;
    private Animator mCurrentAnimator;

    @AfterInject protected void initData() {
        coinModel = new CoinModel(Constants.BTC, Constants.BITCOIN, Constants.BTC_LENGHT);
        new MainAcBtcPresenter(this, getCacheDir(), this);
    }

    @AfterViews
    protected void initUI() {
        setListeners();
        setViewPager();
        getBitcoinExchange();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //TODO: DrawerFragment disabled
        /*
        drawerFragment.setUp(R.id.left_drawer, (DrawerLayout)findViewById(R.id.drawer_layout), null);
        drawerFragment.setLogo(btNavBar);
        */
    }

    @Override
    @UiThread
    public void displayPercentage(int percent) {
        if (percent<100) {
            tvSyncingMessage.setText(getString(R.string.syncing_with_blockchain));
            llSyncing.setVisibility(View.VISIBLE);
            tvSyncing.setText(" ".concat(String.valueOf(percent)).concat("%"));
        }else llSyncing.setVisibility(View.GONE);
    }

    @Override
    @UiThread
    public void displayMessage(String message) {
        tvSyncingMessage.setText(message);
        tvSyncing.setText("");
    }

    @Override
    @UiThread
    public void displayBlockchainStarting(int blockLeft) {
        if (blockLeft>0) {
            tvSyncingMessage.setText(getString(R.string.blockchain_sync_starting));
            llSyncing.setVisibility(View.VISIBLE);
            tvSyncing.setText(" ".concat(NumberFormat.getInstance().format(blockLeft)).concat(" ").concat(getString(R.string.blocks_left)));
        }else llSyncing.setVisibility(View.GONE);
    }

    @Override
    public void displayBalance(String myBalance) {
        tvBalance.setText(myBalance.concat(" ").concat(coinModel.getCoinAbb()));
    }

    @Override
    public void displayBalanceFiat(String myBalance) {
        tvBalanceFiat.setText(myBalance.concat(" ").concat(coinModel.getFiat()));
    }

    @Override
    public void displayMyAddress(String myAddress) {
        tvAddress.setText(myAddress);
        ivQRcode.setImageBitmap(new QrCreator().createQr(coinModel.getCoinName(), myAddress));
        expandedQR.setImageBitmap(new QrCreator().createQr(coinModel.getCoinName(), myAddress));
    }


    @Override
    public void showToastMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }


    @Override
    @UiThread
    public void onSetupCompleted() {
        presenter.refresh();
    }

    @Override
    public void setSendValues(Float exchangeValue, Float maxSpendable) {
        configSendDialog(exchangeValue, maxSpendable);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((this ),
                    new String[]{Manifest.permission.CAMERA},
                    Constants.CAMERA_PERMISSION);
        }
//        PopUpSendTx popUpSendTx = new PopUpSendTx(this, exchangeValue, maxSpendable);
//
//        popUpSendTx.setFiatValue(exchangeValue);
//        popUpSendTx.setMaxSpendable(maxSpendable);
//        popUpSendTx.setCoinModel(coinModel);
//        popUpSendTx.setGetFee(new Interfaces.GetFee() {
//            @Override
//            public void onValueChanged(String address, String amount, String fee, boolean expendAll) {
//                String totalFee = presenter.calculateFee(address, amount, fee, expendAll);
//                if (totalFee!=null) popUpSendTx.setTotalFee(totalFee);
//                else popUpSendTx.setTotalFee(null);
//            }
//
//            @Override
//            public void onSendPressed(String address, String amount, String fee, boolean expendAll) {
//                presenter.send(address, amount, fee, expendAll);
//            }
//        });
//        popUpSendTx.showPopUp();

        sendDialog.show();
    }

    @Override
    public void setRequestValues(Float exchangeValue, String address) {
        configRequestDialog(exchangeValue, address);
        requestDialog.show();
    }

    @Override
    public void presentTx(Bundle extras, Class activity) {
        Intent intent = new Intent(this, activity);
        intent.putExtras(extras);
        startActivity(intent);
    }

    @Override
    public void setPresenter(Interfaces.MainActivityContract.MainActivityPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void presentAdapter(RecyclerView.Adapter adapter) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvTransactionList.setLayoutManager(layoutManager);
        rvTransactionList.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.request:
                fabToolbar.hide();
                presenter.receiveRequest();
                break;
            case R.id.options:
                //Intent intent = new Intent(this, OptionsActivity_.class);
                //startActivity(intent);
                DialogOptions dialogOptions = new DialogOptions(this);
                dialogOptions.setOptionsListCallback(this);
                dialogOptions.show();
                fabToolbar.hide();
                break;
            case  R.id.send:
                fabToolbar.hide();
                presenter.sendRequest();
                break;
            case R.id.close:
                fabToolbar.hide();
                break;
        }
    }

    private void setListeners() {
        ivCopyAdress.setOnClickListener(v -> {
            ClipData clip = ClipData.newPlainText("My wallet address", tvAddress.getText().toString());
            clipboardManager.setPrimaryClip(clip);
            showToastMessage(getString(R.string.copied_to_clipboard));
        });
        ivQRcode.setOnClickListener(v -> zoomQR());
        fab.setOnClickListener(v -> fabToolbar.show());
        request.setOnClickListener(this);
        options.setOnClickListener(this);
        send.setOnClickListener(this);
        close.setOnClickListener(this);
    }

    private void setViewPager() {
        Vector<Integer> pages = new Vector<>();
        pages.add(R.drawable.vector_bitcoin);
        //pages.add(R.drawable.vector_ethereum);
        AdapterPagerCoins adapter = new AdapterPagerCoins(pages, this);
        viewPager.setClipToPadding(false);
        viewPager.setPadding(60,0,60,0);
        viewPager.setPageMargin(40);
        viewPager.setAdapter(adapter);
        int dotsCount = adapter.getCount();
        ImageView[] dots = new ImageView[dotsCount];

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dotsCount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_selecteditem_dot));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), pages.get(position)));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setTag(i);
            dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.non_selecteditem_dot));
            dots[0].setImageDrawable(ContextCompat.getDrawable(this, pages.get(0)));
            int size = (int) getResources().getDimension(R.dimen.pager_item_size);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
            params.setMargins(4, 0, 4, 0);
            pager_indicator.addView(dots[i], params);
            dots[i].setOnClickListener(v -> {
                viewPager.setCurrentItem((Integer) v.getTag());
            });
        }
    }

    private void getBitcoinExchange() {
        Call<BitcoinExchange> call = NetworkService.getInstance().getNetworkService().getBitcoinExchange(NetworkServiceInterface.BLOCKCHAIN_TICKER);
        call.enqueue(new Callback<BitcoinExchange>() {
            @Override
            public void onResponse(Call<BitcoinExchange> call, Response<BitcoinExchange> response) {
                coinModel.setBitcoinExchane(response.body(), MainActivityV11.this);
                presenter.subscribe(coinModel.getFiatValues());
            }
            @Override
            public void onFailure(Call<BitcoinExchange> call, Throwable t) {
                presenter.subscribe(null);
                showToastMessage(t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void configSendDialog(Float exchangeValue, Float maxSpendable) {
        sendDialog = new DialogSend(this);
        sendDialog.setFiatValue(exchangeValue);
        sendDialog.setMaxSpendable(maxSpendable);
        sendDialog.setCoinModel(coinModel);
        sendDialog.setGetFee(new Interfaces.GetFee() {
            @Override
            public void onValueChanged(String address, String amount, String fee, boolean expendAll) {
                String totalFee = presenter.calculateFee(address, amount, fee, expendAll);
                if (totalFee!=null) sendDialog.setTotalFee(totalFee);
                else sendDialog.setTotalFee(null);
            }

            @Override
            public void onSendPressed(String address, String amount, String fee, boolean expendAll) {
                presenter.send(address, amount, fee, expendAll);
            }
        });
        Animation animRight = AnimationUtils.loadAnimation(this, R.anim.rotate_right);
        Animation animLeft = AnimationUtils.loadAnimation(this, R.anim.rotate_left);
        sendDialog.setOnShowListener(dialog -> fab.startAnimation(animRight));
        sendDialog.setOnCancelListener(dialog -> fab.startAnimation(animLeft));
        sendDialog.setOnDismissListener(dialog -> fab.startAnimation(animLeft));
    }

    private void configRequestDialog(Float exchangeValue, String address){
        requestDialog = new DialogRequest(this);
        requestDialog.setFiatValue(exchangeValue);
        requestDialog.setCoinModel(coinModel);
        requestDialog.setAddress(address);
        Animation animRight = AnimationUtils.loadAnimation(this, R.anim.rotate_right);
        Animation animLeft = AnimationUtils.loadAnimation(this, R.anim.rotate_left);
        requestDialog.setOnShowListener(dialog -> fab.startAnimation(animRight));
        requestDialog.setOnCancelListener(dialog -> fab.startAnimation(animLeft));
        requestDialog.setOnDismissListener(dialog -> fab.startAnimation(animLeft));
    }


    private void zoomQR() {
        int mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
        if (mCurrentAnimator != null) mCurrentAnimator.cancel();
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();
        ivQRcode.getGlobalVisibleRect(startBounds);
        drawer_layout.getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height() > (float) startBounds.width() / startBounds.height()) {
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }
        ivQRcode.setAlpha(0f);
        expandedQR.setVisibility(View.VISIBLE);
        expandedQR.setPivotX(0f);
        expandedQR.setPivotY(0f);
        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(expandedQR, View.X, startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedQR, View.Y, startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedQR, View.SCALE_X, startScale, 1f))
                .with(ObjectAnimator.ofFloat(expandedQR, View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }
            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;
        final float startScaleFinal = startScale;
        expandedQR.setOnClickListener(view -> {
            if (mCurrentAnimator != null) {
                mCurrentAnimator.cancel();
            }
            AnimatorSet set1 = new AnimatorSet();
            set1.play(ObjectAnimator
                    .ofFloat(expandedQR, View.X, startBounds.left))
                    .with(ObjectAnimator.ofFloat(expandedQR, View.Y,startBounds.top))
                    .with(ObjectAnimator.ofFloat(expandedQR, View.SCALE_X, startScaleFinal))
                    .with(ObjectAnimator.ofFloat(expandedQR, View.SCALE_Y, startScaleFinal));
            set1.setDuration(mShortAnimationDuration);
            set1.setInterpolator(new DecelerateInterpolator());
            set1.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    ivQRcode.setAlpha(1f);
                    expandedQR.setVisibility(View.GONE);
                    mCurrentAnimator = null;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    ivQRcode.setAlpha(1f);
                    expandedQR.setVisibility(View.GONE);
                    mCurrentAnimator = null;
                }
            });
            set1.start();
            mCurrentAnimator = set1;
        });
    }

    @Override
    public void onOptionClick(int pos) {
        switch (pos){
            case 0:
                PopUpBackupWallet popUpBackupWallet = new PopUpBackupWallet(this, presenter.seed());
                popUpBackupWallet.showPopUp();
                break;
            case 1:
                PopUpRecoverWallet popUpRecoverWallet = new PopUpRecoverWallet(this);
                popUpRecoverWallet.setRestoreWalletCallback(this);
                popUpRecoverWallet.showPopUp();
                break;
        }
    }

    @Override
    public void onRestoreClick(String phrase) {
        presenter.restoreWallet(phrase);
    }
}
