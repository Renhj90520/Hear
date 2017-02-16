package com.jamendo.renhaojie.hear.activities.main;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.jamendo.renhaojie.hear.adapters.AlbumListAdapter;
import com.jamendo.renhaojie.hear.models.Album;

import java.util.List;

/**
 * Created by Ren Haojie on 2017/1/12.
 */

public interface MianContract {

    public interface IMainView {
        void showProgress();

        void hideProgress();

        void setItems(AlbumListAdapter adapter);

        void navigateToTrack(String albumId, String albumName, ImageView sharedImage);

        void showMessage(String message);
    }

    public interface IMainPresenter {
        void loadAlbums(int lastVisible);

        void refreshAlbums();
    }
}
