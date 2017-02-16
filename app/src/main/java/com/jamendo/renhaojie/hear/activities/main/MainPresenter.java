package com.jamendo.renhaojie.hear.activities.main;

import android.widget.ImageView;

import com.jamendo.renhaojie.hear.adapters.AlbumListAdapter;
import com.jamendo.renhaojie.hear.models.Album;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Ren Haojie on 2017/1/12.
 */

public class MainPresenter implements MianContract.IMainPresenter, AlbumListAdapter.OnAlbumItemClickListener {

    private IAlbumInteractor mInteractor;
    private MianContract.IMainView mMainView;
    private List<Album> mAlbumList = new ArrayList<>();
    private AlbumListAdapter mAdapter;
    private int page;
    private int limit = 20;

    @Inject
    public MainPresenter(MianContract.IMainView mainView, IAlbumInteractor interactor) {
        mMainView = mainView;
        mInteractor = interactor;
        mAdapter = new AlbumListAdapter((MainActivity) mMainView, mAlbumList);
        mAdapter.setOnAlbumClickListener(MainPresenter.this);
    }

    @Override
    public void loadAlbums(int lastVisibleIndex) {
        if (lastVisibleIndex >= mAlbumList.size() - 1) {
            mMainView.showProgress();
            int offset = page * limit;
            mInteractor.loadAlbums(offset, new IAlbumInteractor.OnLoadFinishedListener() {
                @Override
                public void onLoadFinished(List<Album> albums) {
                    int currentSize = mAlbumList.size();
                    mAlbumList.addAll(albums);
                    mAdapter.notifyItemInserted(currentSize);
                    if (page == 0) {
                        mMainView.setItems(mAdapter);
                    }
                    page++;
                    mMainView.hideProgress();
                }

                @Override
                public void onLoadError() {
                    mMainView.showMessage("Network Error");
                    mMainView.hideProgress();
                }
            });
        }
    }

    @Override
    public void refreshAlbums() {
        mMainView.showProgress();
        mInteractor.loadAlbums(0, new IAlbumInteractor.OnLoadFinishedListener() {
            @Override
            public void onLoadFinished(List<Album> albums) {
                mAlbumList = albums;
                mAdapter = new AlbumListAdapter((MainActivity) mMainView, mAlbumList);
                mAdapter.setOnAlbumClickListener(MainPresenter.this);
                mAdapter.notifyDataSetChanged();
                mMainView.hideProgress();
                page = 1;
            }

            @Override
            public void onLoadError() {
                mMainView.showMessage("Network Error");
                mMainView.hideProgress();
            }
        });
    }

    @Override
    public void onAlbumClick(int position, ImageView image_album) {
        Album album = mAlbumList.get(position);
        mMainView.navigateToTrack(album.getId(), album.getName(), image_album);
    }
}
