package com.jamendo.renhaojie.hear.activities.tracks;

import com.jamendo.renhaojie.hear.activities.PerActivity;
import com.jamendo.renhaojie.hear.webservices.AlbumServiceComponent;

import dagger.Component;

/**
 * Created by Ren Haojie on 2017/1/14.
 */

@PerActivity
@Component(modules = TracksModule.class, dependencies = AlbumServiceComponent.class)
public interface TracksComponent {
    void inject(TracksActivity activity);
}
