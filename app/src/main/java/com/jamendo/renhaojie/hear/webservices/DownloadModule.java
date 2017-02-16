package com.jamendo.renhaojie.hear.webservices;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by Ren Haojie on 2017/2/12.
 */

@Module
public class DownloadModule {

    @Singleton
    @Provides
    RxJavaCallAdapterFactory provideRxJavaCallAdapterFactory() {
        return RxJavaCallAdapterFactory.create();
    }


    @Provides
    @Singleton
    public OkHttpClient provideClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new DownloadProgressInterceptor())
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
                .build();

        return client;
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(OkHttpClient client, RxJavaCallAdapterFactory callAdapterFactory) {
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addCallAdapterFactory(callAdapterFactory)
                .client(client)
                .build();
    }

    @Provides
    @Singleton
    public DownloadAPI provideDownloadApi(Retrofit retrofit) {
        return retrofit.create(DownloadAPI.class);
    }
}
