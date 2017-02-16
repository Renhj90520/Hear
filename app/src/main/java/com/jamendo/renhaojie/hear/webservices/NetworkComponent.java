package com.jamendo.renhaojie.hear.webservices;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Subcomponent;
import retrofit2.Retrofit;

/**
 * Created by Ren Haojie on 2017/1/13.
 */
@Singleton
@Component(modules = NetworkModule.class)
public interface NetworkComponent {
    Retrofit retrofit();
}
