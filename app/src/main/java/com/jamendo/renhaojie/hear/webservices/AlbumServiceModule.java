package com.jamendo.renhaojie.hear.webservices;

import com.jamendo.renhaojie.hear.AppScope;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by Ren Haojie on 2017/1/13.
 */

@Module
public class AlbumServiceModule {

    @AppScope
    @Provides
    AlbumService provideJamendoService(Retrofit retrofit) {
        return retrofit.create(AlbumService.class);
    }
}
