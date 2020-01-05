package com.cloudcode.PromotionUniquier.Utilities;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppController extends Application {
    public static final String BASE_URL = "http://pu.mycit.co.in/server/"; //live

    private static Retrofit retrofit;
    private static AppController mInstance;
    private SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "PROM_pref" ;


    //@return instance of app class
    public static synchronized AppController getInstance() {
        return mInstance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

    }

    public boolean inNetwork() {
        boolean isConnected = false;
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo nInfo = manager != null ? manager.getActiveNetworkInfo() : null;
        if (nInfo != null && nInfo.isConnectedOrConnecting())
            isConnected = true;
        return isConnected;
    }


    public Retrofit getClient() {
        if (retrofit == null) {
            //had to increase the timeout, dont change
            OkHttpClient okHttpClient;

            okHttpClient = new OkHttpClient.Builder()
//                    .addNetworkInterceptor(new com.facebook.stetho.okhttp3.StethoInterceptor())
                    .writeTimeout(100, TimeUnit.SECONDS)
                    .readTimeout(100, TimeUnit.SECONDS)
                    .build();

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }



}
