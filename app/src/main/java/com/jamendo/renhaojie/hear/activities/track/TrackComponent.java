package com.jamendo.renhaojie.hear.activities.track;

import com.jamendo.renhaojie.hear.activities.PerActivity;
import com.jamendo.renhaojie.hear.webservices.AlbumServiceComponent;

import dagger.Component;

/**
 * Created by Ren Haojie on 2017/2/5.
 */

@PerActivity
@Component(modules = TrackModule.class, dependencies = AlbumServiceComponent.class)
public interface TrackComponent {
    void inject(TrackActivity activity);
}
