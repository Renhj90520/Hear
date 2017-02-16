package com.jamendo.renhaojie.hear.webservices;

import com.jamendo.renhaojie.hear.AppScope;
import com.jamendo.renhaojie.hear.activities.main.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Ren Haojie on 2017/1/13.
 */

@AppScope
@Component(modules = AlbumServiceModule.class, dependencies = NetworkComponent.class)
public interface AlbumServiceComponent {
    AlbumService jamendoService();
//    void inject(MainActivity activity);
}
