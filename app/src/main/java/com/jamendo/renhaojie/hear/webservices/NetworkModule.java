package com.jamendo.renhaojie.hear.webservices;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ren Haojie on 2017/1/13.
 */

@Module
public class NetworkModule {
    private String base_url;

    public NetworkModule(String base_url) {
        this.base_url = base_url;
    }

    @Singleton
    @Provides
    RxJavaCallAdapterFactory provideRxJavaCallAdapterFactory() {
        return RxJavaCallAdapterFactory.create();
    }

    @Singleton
    @Provides
    GsonConverterFactory provideGsonConverterFactory() {
        return GsonConverterFactory.create();
    }

//    @Singleton
//    @Provides
//    OkHttpClient provideOkHttpClient() {
//
//        return new OkHttpClient().newBuilder()
//                .readTimeout(15, TimeUnit.SECONDS)
//                .connectTimeout(10, TimeUnit.SECONDS)
//                .writeTimeout(15, TimeUnit.SECONDS)
//                .build();
//    }

    @Singleton
    @Provides
    Retrofit provideRetrofit(GsonConverterFactory converterFactory, RxJavaCallAdapterFactory callAdapterFactory) {
        return new Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(converterFactory)
                .addCallAdapterFactory(callAdapterFactory)
//                .client(client)
                .build();
    }
}
