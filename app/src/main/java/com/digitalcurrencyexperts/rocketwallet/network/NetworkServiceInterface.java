package com.digitalcurrencyexperts.rocketwallet.network;


import android.support.annotation.Nullable;

import com.digitalcurrencyexperts.rocketwallet.models.BitcoinExchange;
import com.digitalcurrencyexperts.rocketwallet.models.BitcoinModelBlockChainApi.BitcoinFee;
import com.digitalcurrencyexperts.rocketwallet.models.BitcoinModelBlockChainApi.BitcoinTransaction;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by Santiago Del Valle on 11/07/2017.
 * Company name Zombytes Devs
 * Contact sdelvalle57@gmail.com
 */

public interface NetworkServiceInterface {
    public static final String BLOCKCHAIN_INFO_API = "https://api.blocktrail.com/v1/tBTC/";
    public static final String BLOCKCHAIN_TICKER = "https://blockchain.info/es/ticker";
    static String API_KEY = "1035dafac1380cdd63042f9bf63fe7d82767e2d9";
    static int LIMIT = 200;
    //API sec: 856892913c52fd1f2dbe3b8240d04bd7daf64b29
    //https://api.blocktrail.com/v1/BTC/fee-per-kb?api_key=MY_APIKEY

    @GET("transaction/{transaction}")
    Call<BitcoinTransaction> getTransaction(@Path("transaction") String transaction, @Nullable @QueryMap Map<String, Object> params);

    @GET("fee-per-kb")
    Call<BitcoinFee> getWebFee(@Nullable @QueryMap Map<String, Object> params);

    @GET
    Call<BitcoinExchange> getBitcoinExchange(@Url String url);
   // Call<OrderBookResponse> getOrderBook(@Field("market") String market, @Field("type") String type, @Field("depth") int depth);

//    @FormUrlEncoded
//    @POST("login.php")
//    Call<ResponseLogin> userLogin(@FieldMap Map<String, Object> params);
}
