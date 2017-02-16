package com.jamendo.renhaojie.hear.services.download;

import com.jamendo.renhaojie.hear.activities.PerActivity;
import com.jamendo.renhaojie.hear.utils.download.DownloadManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Ren Haojie on 2017/2/9.
 */

@Module
public class DownloadServiceModule {

    @Provides
    @PerActivity
    public DownloadManager provideDownloadManager() {
        return new DownloadManager();
    }

}
