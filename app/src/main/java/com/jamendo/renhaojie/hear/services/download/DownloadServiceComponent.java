package com.jamendo.renhaojie.hear.services.download;

import com.jamendo.renhaojie.hear.activities.PerActivity;
import com.jamendo.renhaojie.hear.webservices.DownloadComponent;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Ren Haojie on 2017/2/9.
 */
@PerActivity
@Component(modules = DownloadServiceModule.class, dependencies = DownloadComponent.class)
public interface DownloadServiceComponent {
    void inject(DownloadService downloadService);
}
