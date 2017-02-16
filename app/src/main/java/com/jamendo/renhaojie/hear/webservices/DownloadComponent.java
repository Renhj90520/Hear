package com.jamendo.renhaojie.hear.webservices;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Ren Haojie on 2017/2/12.
 */

@Singleton
@Component(modules = DownloadModule.class)
public interface DownloadComponent {
    DownloadAPI downloadAPI();
}
