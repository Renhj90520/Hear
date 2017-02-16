package com.jamendo.renhaojie.hear.activities.main;

import com.jamendo.renhaojie.hear.webservices.AlbumService;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Ren Haojie on 2017/1/13.
 */

@Module
public class MainModule {
    private final MianContract.IMainView mMainView;

    public MainModule(MianContract.IMainView mainView) {
        mMainView = mainView;
    }

    @Provides
    MianContract.IMainView provideMainView() {
        return mMainView;
    }

    @Provides
    IAlbumInteractor provideAlbumInteractor(AlbumService albumService) {
        return new AlbumInteractor(albumService);
    }

    @Provides
    MianContract.IMainPresenter provideMainPresenter(MianContract.IMainView mainView, IAlbumInteractor interactor) {
        return new MainPresenter(mainView, interactor);
    }
}
