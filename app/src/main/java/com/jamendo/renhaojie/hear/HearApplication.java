package com.jamendo.renhaojie.hear;

import android.app.Application;
import android.drm.DrmStore;
import android.util.Log;

import com.jamendo.renhaojie.hear.webservices.AlbumServiceComponent;
import com.jamendo.renhaojie.hear.webservices.Constants;
import com.jamendo.renhaojie.hear.webservices.DaggerAlbumServiceComponent;
import com.jamendo.renhaojie.hear.webservices.DaggerDownloadComponent;
import com.jamendo.renhaojie.hear.webservices.DaggerNetworkComponent;
import com.jamendo.renhaojie.hear.webservices.DownloadComponent;
import com.jamendo.renhaojie.hear.webservices.NetworkComponent;
import com.jamendo.renhaojie.hear.webservices.NetworkModule;

/**
 * Created by Ren Haojie on 2017/1/10.
 */

public class HearApplication extends Application {
    AlbumServiceComponent mAlbumServiceComponent;
    DownloadComponent mDownloadComponent;

    public AlbumServiceComponent getAlbumServiceComponent() {
        return mAlbumServiceComponent;
    }

    public DownloadComponent getDownloadComponent() {
        return mDownloadComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        resolveDependency();
    }

    private void resolveDependency() {
        mAlbumServiceComponent = DaggerAlbumServiceComponent.builder()
                .networkComponent(getNetworkComponet())
                .build();

        mDownloadComponent = DaggerDownloadComponent.builder()
                .build();

    }

    private NetworkComponent getNetworkComponet() {
        return DaggerNetworkComponent.builder()
                .networkModule(new NetworkModule(Constants.BASE_URL))
                .build();
    }
}
