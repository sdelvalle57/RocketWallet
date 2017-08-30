package com.digitalcurrencyexperts.rocketwallet.common;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.digitalcurrencyexperts.rocketwallet.models.ExchangeValues;

import org.bitcoinj.core.Transaction;
import org.bitcoinj.kits.WalletAppKit;

/**
 * Created by santiagp on 6/07/2017.
 */

public class Interfaces {
    public interface IFragmentPosition{
        void setPosition(int position);
        int getPosition();
    }

    public interface BitcoinCallbacks{
        void onSetupCompleted(WalletAppKit walletAppKit);
        void onNewAddress(String address);
        void onNewConfirmation(Transaction tx);
        void onCoinsReceived(Transaction tx);
        void onCoinsSent(Transaction tx);
    }

    public interface PermissionCallback {
        void onCameraPermissionGranted();
    }

    public interface GetFee{
        void onValueChanged(String address, String amount, String feePerKb, boolean spendAll);
        void onSendPressed(String address, String amount, String feePerKb, boolean spendAll);
    }

    public interface TxListCallback {
        void onTxClick(int pos);
    }

    public interface OptionsListCallback {
        void onOptionClick(int pos);
    }

    public interface RestoreWalletCallback
    {
        void onRestoreClick(String phrase);
    }
    public interface MainActivityContract {
        interface MainActivityView extends BaseView<MainActivityPresenter> {
            void displayPercentage(int percent);
            void displayBlockchainStarting(int blocksLeft);

            void displayBalance(String myBalance);
            void displayBalanceFiat(String myBalance);

            void displayMyAddress(String myAddress);
            void displayMessage(String message);
            void presentAdapter(RecyclerView.Adapter adapter);
            void showToastMessage(String message);
            void onSetupCompleted();
            void setSendValues(Float exchangeValue, Float maxSpendable);
            void setRequestValues(Float exchangeValue, String address);
            void presentTx(Bundle extras, Class activity);
        }
        interface MainActivityPresenter extends BasePresenter {
            void refresh();
            void sendRequest();
            void receiveRequest();
            String seed();
            String calculateFee(String address, String amount, String fee, boolean expendAll);
            void send(String address, String amount, String fee, boolean expendAll);
            void restoreWallet(String phrase);
        }
    }

    public interface BaseView<T extends BasePresenter> {
        void setPresenter(T presenter);
    }

    public interface BasePresenter {
        void subscribe(ExchangeValues exchangeValues);
        void unsubscribe();
    }
}
