package com.jamendo.renhaojie.hear.activities.track;

import android.content.ContentValues;

import com.jamendo.renhaojie.hear.models.Track;

/**
 * Created by Ren Haojie on 2017/2/3.
 */

public class TrackContract {
    public interface ITrackView {
        void setTrackInfo(Track track);

        void play(String trackId, String url);

        void addMark(Track track);

        void deleteMark(String trackId);

        void addDownload(ContentValues track);

        void doDownload(Track track);
    }

    public interface ITrackPresenter {
        void loadTrackInfo(String trackId);

        void play();

        void addMark();

        void doDownload();

        boolean isExist();

        void addDownload();

//        void deleteMark(String TrackId);
    }
}
