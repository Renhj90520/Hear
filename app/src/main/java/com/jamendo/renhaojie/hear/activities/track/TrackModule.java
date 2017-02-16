package com.jamendo.renhaojie.hear.activities.track;

import com.jamendo.renhaojie.hear.webservices.AlbumService;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Ren Haojie on 2017/2/5.
 */

@Module
public class TrackModule {

    private TrackContract.ITrackView mTrackView;

    public TrackModule(TrackContract.ITrackView trackView) {
        mTrackView = trackView;
    }

    @Provides
    TrackContract.ITrackView provideTrackView() {
        return mTrackView;
    }

    @Provides
    ITrackInteractor provideTrackInteractor(AlbumService service) {
        return new TrackInteractor(service);
    }

}
