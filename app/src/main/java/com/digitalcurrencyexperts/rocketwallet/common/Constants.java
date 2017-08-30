package com.digitalcurrencyexperts.rocketwallet.common;


import com.digitalcurrencyexperts.rocketwallet.R;

/**
 * Created by santiagp on 6/07/2017.
 */

public class Constants  {


    public static final String ADDRESSES = "addresses";
    public static final String AMOUNT = "amount";
    public static final String AUD = "AUD";
    public static final String BITCOIN = "bitcoin";
    public static final String BTC = "BTC";
    public static final String FRAGMENT_VISIBLE = "visible_frag";
    public static final String FIAT = "fiat";
    public static final String HOME_FRAGMENT = "homeFragment";
    public static final String IMAGES = "images";
    public static final String LOW_PRIORITY = "Low Priority Fee";
    public static final String MEDIUM_PRIORITY = "Medium Priority Fee";
    public static final String STANDARD = "Standard Fee";
    public static final String TRANSACTIONS = "transactions";
    public static final String TITTLES = "tittles";
    public static final String TX_STRING = "tx_string";
    public static final String TX_IN = "txIn";
    public static final String TX_OUT = "txOut";
    public static final String USD = "USD";
    public static final String WALLET_NAME = "users_wallet";


    public static final int BTC_LENGHT = 34;
    public static final int FRAGMENT_HOME_POSITION = 0;
    public static final float SATOSHI_TO_BTC = 100000000;
    public static boolean IS_PRODUCTION = false;

    public static final int CAMERA_PERMISSION = 100;

    //Nav Drawer
    public static final int[] menu = {
            R.string.home,
            R.string.home,
            R.string.home,
            R.string.home,
            R.string.close_session};
    public static final int[] images = {
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.drawable.vector_close_session};

}
