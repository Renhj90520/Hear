package com.jamendo.renhaojie.hear.activities.tracks;

import com.jamendo.renhaojie.hear.models.Album;
import com.jamendo.renhaojie.hear.models.AlbumTrack;

import java.util.List;

/**
 * Created by Ren Haojie on 2017/1/12.
 */

public interface TracksContract {
    public interface ITracksView {
        void showProgress();

        void hideProgress();

        void setItems(AlbumTrack[] tracks);

        void play(String trackId, String url);

        void pause();

        void setCurrentPlayingName(String currentName);

        void setCurrentPlayingIndex(int position);

        void navigateToTrack(String[] trackIds, int index);
    }

    public interface ITracksPresenter {
        void loatTracks(String albumId);

        void play();

        void play(int position);

        void pause();

        void stop();

        void next();

        void prev();
    }
}
