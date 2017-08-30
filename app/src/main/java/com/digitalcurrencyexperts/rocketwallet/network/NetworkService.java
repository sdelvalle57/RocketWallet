package com.digitalcurrencyexperts.rocketwallet.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Santiago Del Valle on 13/04/2017.
 * Company name Zombytes Devs
 * Contact sdelvalle57@gmail.com
 */

public class NetworkService {

    private static NetworkService instance;
    private static Retrofit retrofit;
    private NetworkServiceInterface networkServiceInterface;

    public static NetworkService getInstance() {
        if (instance == null) {
            instance = new NetworkService();
        }

        return instance;
    }

    private NetworkService() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(NetworkServiceInterface.BLOCKCHAIN_INFO_API)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        networkServiceInterface = retrofit.create(NetworkServiceInterface.class);
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }

    public NetworkServiceInterface getNetworkService() {
        return networkServiceInterface;
    }
}
