package com.jamendo.renhaojie.hear.activities.tracks;

import com.jamendo.renhaojie.hear.webservices.AlbumService;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Ren Haojie on 2017/1/14.
 */

@Module
public class TracksModule {
    private TracksContract.ITracksView mTrackView;

    public TracksModule(TracksContract.ITracksView trackView) {
        mTrackView = trackView;
    }

    @Provides
    TracksContract.ITracksView provideTrackView() {
        return mTrackView;
    }

    @Provides
    ITracksInteractor provideInteractor(AlbumService service) {
        return new TracksInteractor(service);
    }
}
