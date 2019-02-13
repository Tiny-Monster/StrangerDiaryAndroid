package com.tinymonster.strangerdiary.net;

import android.content.Context;

import com.tinymonster.strangerdiary.net.interceptor.SaveTokenInterceptor;
import com.tinymonster.strangerdiary.net.interceptor.SetTokenInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络工具类
 * Created by TinyMonster on 10/01/2019.
 */

public class RxRetrofit {
    private Retrofit retrofit;
    private static ApiServer apiServer;

    /**
     * 静态内部类实现单例模式
     */
    private static final class Holder{
        private static final RxRetrofit INSTANCE=new RxRetrofit();
    }

    public static RxRetrofit getInstance() {
        return Holder.INSTANCE;
    }

    public static ApiServer Api(){
        if(apiServer==null)
            throw new IllegalStateException("You must invoke init method first in Application");
        return apiServer;
    }

    public void initRxRetrofit(final Context context,String baseUrl){
        OkHttpClient okHttpClient=new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS)
                .addInterceptor(new SetTokenInterceptor())
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiServer = retrofit.create(ApiServer.class);
    }
}

