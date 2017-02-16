package com.jamendo.renhaojie.hear.activities.main;

import com.jamendo.renhaojie.hear.models.Album;

import java.util.List;

/**
 * Created by Ren Haojie on 2017/1/12.
 */

public interface IAlbumInteractor {
    interface OnLoadFinishedListener {
        void onLoadFinished(List<Album> albums);

        void onLoadError();
    }

    void loadAlbums(int offset, OnLoadFinishedListener listener);
}
