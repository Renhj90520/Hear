package com.jamendo.renhaojie.hear.activities.tracks;

import com.jamendo.renhaojie.hear.models.AlbumTrack;

import java.util.List;

/**
 * Created by Ren Haojie on 2017/1/12.
 */

public interface ITracksInteractor {
    public interface OnTracksLoadFinishListener {
        void onTrackLoadFinish(AlbumTrack[] tracks);
    }

    void loadTracks(String albumId, OnTracksLoadFinishListener listener);
}
