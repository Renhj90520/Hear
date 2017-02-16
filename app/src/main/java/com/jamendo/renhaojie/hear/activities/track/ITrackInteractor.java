package com.jamendo.renhaojie.hear.activities.track;

import com.jamendo.renhaojie.hear.models.Track;

/**
 * Created by Ren Haojie on 2017/2/4.
 */

public interface ITrackInteractor {
    public interface OnTrackInfoLoadedListener {
        void onTrackInfoLoaded(Track track);
    }

    void loadTrackInfo(String trackId, OnTrackInfoLoadedListener listener);
}
