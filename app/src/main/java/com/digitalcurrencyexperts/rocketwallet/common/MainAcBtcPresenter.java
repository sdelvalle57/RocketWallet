package com.digitalcurrencyexperts.rocketwallet.common;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.digitalcurrencyexperts.rocketwallet.R;
import com.digitalcurrencyexperts.rocketwallet.ViewBitcoinTxActivity_;
import com.digitalcurrencyexperts.rocketwallet.adapters.BitcoinTxAdapter;
import com.digitalcurrencyexperts.rocketwallet.models.BitcoinModelBlockChainApi.BitcoinFee;
import com.digitalcurrencyexperts.rocketwallet.models.BitcoinModelBlockChainApi.BitcoinTransaction;
import com.digitalcurrencyexperts.rocketwallet.models.BitcoinModelBlockChainApi.BitcoinTxInput;
import com.digitalcurrencyexperts.rocketwallet.models.BitcoinModelBlockChainApi.BitcoinTxOutput;
import com.digitalcurrencyexperts.rocketwallet.models.ExchangeValues;
import com.digitalcurrencyexperts.rocketwallet.network.NetworkService;
import com.digitalcurrencyexperts.rocketwallet.network.NetworkServiceInterface;
import com.google.common.base.Joiner;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.Service;
import com.google.gson.Gson;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.AddressFormatException;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.InsufficientMoneyException;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Peer;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionConfidence;
import org.bitcoinj.core.listeners.DownloadProgressTracker;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.utils.BriefLogFormatter;
import org.bitcoinj.utils.Threading;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.SendRequest;
import org.bitcoinj.wallet.UnreadableWalletException;
import org.bitcoinj.wallet.Wallet;
import org.bitcoinj.wallet.listeners.WalletCoinsReceivedEventListener;
import org.bitcoinj.wallet.listeners.WalletCoinsSentEventListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Lynx on 4/11/2017.
 */

public class MainAcBtcPresenter extends MultiDexApplication implements Interfaces.MainActivityContract.MainActivityPresenter, WalletCoinsReceivedEventListener, WalletCoinsSentEventListener {

    private Interfaces.MainActivityContract.MainActivityView view;
    private File walletDir; //Context.getCacheDir();

    private NetworkParameters parameters;
    private WalletAppKit walletAppKit;
    private Address address;
    private ArrayList<String> addresses = new ArrayList<>();
    private ArrayList<Transaction> transactions = new ArrayList<>();
    private ArrayList<BitcoinTransaction> formattedTxs = new ArrayList<>();
    private BitcoinTxAdapter bitcoinTxAdapter;
    private Context context;
    private ExchangeValues exchangeValues;
    private BitcoinFee bitcoinFee;
    private SendRequest txRequest;
    private String seedString;
    private String restorePhrase;
    private Handler handler;


    public MainAcBtcPresenter(Interfaces.MainActivityContract.MainActivityView view, File walletDir, Context context) {
        this.view = view;
        this.walletDir = walletDir;
        view.setPresenter(this);
        this.context = context;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void subscribe(ExchangeValues exchangeValues) {
        getWebFee();
        this.exchangeValues = exchangeValues;
        setBtcSDKThread();
        parameters = Constants.IS_PRODUCTION ? MainNetParams.get() : TestNet3Params.get();
        BriefLogFormatter.init();
        setupWallet();

        walletAppKit.setDownloadListener(new DownloadProgressTracker() {
            @Override
            public void onChainDownloadStarted(Peer peer, int blocksLeft) {
                super.onChainDownloadStarted(peer, blocksLeft);
            }

            @Override
            protected void progress(double pct, int blocksSoFar, Date date) {
                super.progress(pct, blocksSoFar, date);
                int percentage = (int) pct;
                view.displayPercentage(percentage);
                try {
                    walletAppKit.wallet().saveToFile(walletAppKit.directory());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void startDownload(int blocks) {
                super.startDownload(blocks);
                view.displayBlockchainStarting(blocks);
            }

            @Override
            protected void doneDownload() {
                super.doneDownload();

            }


        });
        walletAppKit.setBlockingStartup(false);
        walletAppKit.startAsync();
    }

    private void setupWallet() {
        walletAppKit = new WalletAppKit(parameters, walletDir, Constants.WALLET_NAME) {
            @Override
            protected void onSetupCompleted() {
                if (wallet().getImportedKeys().size() < 1) wallet().importKey(new ECKey());
                walletAppKit.peerGroup().setMaxConnections(11);
                walletAppKit.peerGroup().setBloomFilterFalsePositiveRate(0.00001);
                address = wallet().currentReceiveAddress();

                wallet().addCoinsReceivedEventListener(MainAcBtcPresenter.this);
                wallet().addCoinsSentEventListener(MainAcBtcPresenter.this);
                wallet().getWalletTransactions().forEach(walletTransaction ->
                        checkTxsDepth(walletTransaction.getTransaction())
                );

                for (Address address: wallet().getIssuedReceiveAddresses()){
                    addresses.add(address.toBase58());
                }

                view.onSetupCompleted();
                DeterministicSeed seed = wallet().getKeyChainSeed();
                if (seed.getMnemonicCode()!=null)
                    seedString = Joiner.on(" ").join(seed.getMnemonicCode());
            }
        };
        if (restorePhrase!=null){
            try {
                //DeterministicSeed seed = new DeterministicSeed(restorePhrase, null, "", 1503635647);

                DeterministicSeed seed = new DeterministicSeed(restorePhrase, null, "", 1413417600);
                walletAppKit.restoreWalletFromSeed(seed);
            }catch (UnreadableWalletException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkTxsDepth(Transaction tx) {
        transactions.add(tx);
        int depth = tx.getConfidence().getDepthInBlocks();
        if (depth<10){
            getFutureConfirmation(tx, depth);
        }
        getTransaction(tx);
    }

    private void getFutureConfirmation(Transaction tx, int depth) {
        Futures.addCallback(tx.getConfidence().getDepthFuture(depth), new FutureCallback<TransactionConfidence>() {
            @Override
            public void onSuccess(TransactionConfidence result) {
                if (depth<10) getFutureConfirmation(tx, depth+1);
                for (BitcoinTransaction bitcoinTransaction: formattedTxs){
                    if (bitcoinTransaction.getHash().equals(tx.getHash().toString())){
                        bitcoinTransaction.setBlockDepth(tx.getConfidence().getDepthInBlocks());
                        if (bitcoinTxAdapter!=null){
                            bitcoinTxAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
            @Override
            public void onFailure(Throwable t) {
                throw new RuntimeException(t);
            }
        });
    }

    @Override
    public void unsubscribe() {
        walletAppKit.stopAsync();
    }

    @Override
    public void restoreWallet(String phrase) {

        walletAppKit.addListener(new Service.Listener() {
            @Override
            public void terminated (Service.State from) {
                subscribe(exchangeValues);

            }
        }, handler::post);
        this.restorePhrase = phrase;
        unsubscribe();
        //handler.removeCallbacks();
        view.displayMessage(context.getString(R.string.restoring_wallet));
        walletAppKit.stopAsync();
        //setupWallet();
        /*
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //view.showToastMessage();

            }
        }, 2000);
*/
    }

    @Override
    public void refresh() {
        address = walletAppKit.wallet().currentReceiveAddress();
        view.displayMyAddress(address.toBase58());
        showBalance();
    }

    @Override
    public void sendRequest() {
        Float maxSpendable = Float.valueOf(walletAppKit.wallet().getBalance(Wallet.BalanceType.AVAILABLE_SPENDABLE).toPlainString());
        view.setSendValues(exchangeValues.getLast(), maxSpendable);
    }

    @Override
    public void receiveRequest() {
        view.setRequestValues(exchangeValues.getLast(), address.toBase58());
    }

    @Override
    public String seed() {
        return seedString;
    }

    @Override
    public String calculateFee(String address, String amount, String fee, boolean expendAll) {
        String feeKb = null;
        return createRequest(address, amount, feeKb, expendAll);
    }

    @Override
    public void send(String address, String amount, String fee, boolean expendAll) {
        if (txRequest!=null) walletAppKit.peerGroup().broadcastTransaction(txRequest.tx).broadcast();
        else if (createRequest(address, amount, fee, expendAll)!=null)
                walletAppKit.peerGroup().broadcastTransaction(txRequest.tx).broadcast();

    }




    private void setBtcSDKThread() {
        handler = new Handler();
        Threading.USER_THREAD = handler::post;
    }

    @Override
    public void onCoinsSent(Wallet wallet, Transaction tx, Coin prevBalance, Coin newBalance) {
        getFutureConfirmation(tx, 0);
        getTransaction(tx);
        showBalance();
    }

    @Override
    public void onCoinsReceived(Wallet wallet, Transaction tx, Coin prevBalance, Coin newBalance) {
        getFutureConfirmation(tx, 0);
        getTransaction(tx);
        showBalance();
    }

    private String createRequest(String recipientAddress, String amount, String feePerKb, boolean spendAll) {
        Log.i("amount_to_send", amount);

        try {
            Address address = Address.fromBase58(parameters, recipientAddress);
            txRequest = SendRequest.to(address, Coin.parseCoin(amount));
            if (spendAll) txRequest = SendRequest.emptyWallet(address);
            if (feePerKb!=null) txRequest.feePerKb = Coin.parseCoin(feePerKb);
            walletAppKit.wallet().completeTx(txRequest);
            Log.i("fee_x_kb", txRequest.tx.getFee().toPlainString());
            return txRequest.tx.getFee().toPlainString();
        } catch (AddressFormatException a){
            view.showToastMessage(context.getString(R.string.invalid_address));
        } catch (InsufficientMoneyException e) {
            view.showToastMessage(context.getString(R.string.insuficient_money));
        }catch (Wallet.CompletionException e){
            view.showToastMessage(context.getString(R.string.value_too_low));
        }
        return null;
    }

    private void showBalance() {
        Coin balanceCoin = walletAppKit.wallet().getBalance(Wallet.BalanceType.AVAILABLE_SPENDABLE);
        float floatBalance = Float.parseFloat(balanceCoin.toPlainString());
        if (exchangeValues!=null) {
            String fiatBalance = String.format(Locale.ENGLISH, "%.2f", floatBalance * exchangeValues.getLast());
            view.displayBalanceFiat(fiatBalance);
        }
        view.displayBalance(walletAppKit.wallet().getBalance().toPlainString());

        Coin value1 = walletAppKit.wallet().getBalance(Wallet.BalanceType.AVAILABLE);
        Coin value2 = walletAppKit.wallet().getBalance(Wallet.BalanceType.AVAILABLE_SPENDABLE);
        Coin value3 = walletAppKit.wallet().getBalance(Wallet.BalanceType.ESTIMATED);
        Coin value4 = walletAppKit.wallet().getBalance(Wallet.BalanceType.ESTIMATED_SPENDABLE);
        Log.d("myLogs curr", "AVAILABLE" + value1.toFriendlyString());
        Log.d("myLogs curr", "AVAILABLE_SPENDABLE" + value2.toFriendlyString());
        Log.d("myLogs curr", "ESTIMATED" + value3.toFriendlyString());
        Log.d("myLogs curr", "ESTIMATED_SPENDABLE" + value4.toFriendlyString());

    }


    private void checkIfTxInOrTxOut(BitcoinTransaction bitcoinTransaction, Transaction transaction) {
        Log.i("txxx", String.valueOf(bitcoinTransaction.getEstimatedValue()));
        boolean found = false;
        for (BitcoinTxInput bitcoinTxInput : bitcoinTransaction.getBitcoinTxInputs()) {
            String txInputAddress = bitcoinTxInput.getAddress();
            bitcoinTransaction.setBlockDepth(transaction.getConfidence().getDepthInBlocks());
            for (String myAddress : addresses) {
                //If the address is an input of the TX then is a TxOut (outcome), then we have to look in the outputs
                if (txInputAddress.equals(myAddress)) {
                    bitcoinTransaction.setIncome(false);
                    fillValues(bitcoinTransaction, myAddress);
                    found = true; //if is output with this flag we will check if is input as well, if so is a payment made from me to me
                }
            }
        }
        for (BitcoinTxOutput bitcoinTxOutput : bitcoinTransaction.getBitcoinTxOutputs()) {
            String txOutputAddress = bitcoinTxOutput.getAddress();
            bitcoinTransaction.setBlockDepth(transaction.getConfidence().getDepthInBlocks());
            //is an income, if we check if spent_hash is  not null, so this is an UTXO, this must me the value of my account
            for (String myAddress : addresses) {
                if (txOutputAddress.equals(myAddress)) {
                    if (found){
                        BitcoinTransaction bitcoinTransactionCopy = new Gson().fromJson(new Gson().toJson(bitcoinTransaction), BitcoinTransaction.class);
                        bitcoinTransactionCopy.setIncome(true);
                        bitcoinTransactionCopy.setEstimatedValue(bitcoinTxOutput.getValue());
                        formattedTxs.add(bitcoinTransactionCopy);
                        notifyDataset();
                        return;
                    }
                    Log.i("txxx", bitcoinTransaction.getHash() + " "+bitcoinTxOutput.getValue());
                    bitcoinTransaction.setIncome(true);
                    bitcoinTransaction.setEstimatedValue(bitcoinTxOutput.getValue());
                    fillValues(bitcoinTransaction, myAddress);
                }
            }
        }
    }

    private void fillValues(BitcoinTransaction bitcoinTransaction, String myAddress) {
        if (exchangeValues!=null)
            bitcoinTransaction.setEstimatedValueFiat(exchangeValues.getLast());
        bitcoinTransaction.setMyAddress(myAddress);
        for (BitcoinTransaction btx: formattedTxs){
            if (bitcoinTransaction.getHash().equals(btx.getHash())){
                notifyDataset();
                return;
            }
        }
        formattedTxs.add(bitcoinTransaction);
        notifyDataset();
    }

    private void notifyDataset() {
        Collections.sort(formattedTxs, (r1, r2) -> r1.getBlockDepth() -r2.getBlockDepth());
        if (bitcoinTxAdapter==null){
            bitcoinTxAdapter = new BitcoinTxAdapter(context, formattedTxs);
            view.presentAdapter(bitcoinTxAdapter);
            bitcoinTxAdapter.setTxListCallback(pos -> {
                BitcoinTransaction bitcoinTransaction = bitcoinTxAdapter.getItem(pos);
                Bundle bundle = new Bundle();
                bundle.putString(Constants.TX_STRING, new Gson().toJson(bitcoinTransaction));
                bundle.putStringArrayList(Constants.ADDRESSES, addresses);
                view.presentTx(bundle, ViewBitcoinTxActivity_.class);
            });
        }else bitcoinTxAdapter.notifyDataSetChanged();
    }


    private void getTransaction(Transaction transaction){
        HashMap<String, Object> query = new HashMap<>();
        query.put("api_key", NetworkServiceInterface.API_KEY);
        Call<BitcoinTransaction> call = NetworkService.getInstance().getNetworkService().getTransaction(transaction.getHashAsString(), query);
        call.enqueue(new Callback<BitcoinTransaction>() {
            @Override
            public void onResponse(Call<BitcoinTransaction> call, Response<BitcoinTransaction> response) {
                if (response.isSuccessful()) {
                    checkIfTxInOrTxOut(response.body(), transaction);
                }
            }
            @Override
            public void onFailure(Call<BitcoinTransaction> call, Throwable t) {
                view.showToastMessage(t.getMessage());
            }
        });
    }

    private void getWebFee(){
        HashMap<String, Object> query = new HashMap<>();
        query.put("api_key", NetworkServiceInterface.API_KEY);
        Call<BitcoinFee> call = NetworkService.getInstance().getNetworkService().getWebFee(query);
        call.enqueue(new Callback<BitcoinFee>() {
            @Override
            public void onResponse(Call<BitcoinFee> call, Response<BitcoinFee> response) {
                if (response.isSuccessful()) {
                    bitcoinFee = response.body();
                }
            }
            @Override
            public void onFailure(Call<BitcoinFee> call, Throwable t) {
                view.showToastMessage(t.getMessage());
            }
        });
    }

}
