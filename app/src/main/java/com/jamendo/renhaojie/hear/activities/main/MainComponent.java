package com.jamendo.renhaojie.hear.activities.main;

import com.jamendo.renhaojie.hear.activities.PerActivity;
import com.jamendo.renhaojie.hear.webservices.AlbumServiceComponent;

import dagger.Component;

/**
 * Created by Ren Haojie on 2017/1/13.
 */

@PerActivity
@Component(modules = MainModule.class, dependencies = AlbumServiceComponent.class)
public interface MainComponent {
    void inject(MainActivity mainActivity);
}
