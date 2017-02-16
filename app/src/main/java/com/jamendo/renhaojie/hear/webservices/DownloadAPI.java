package com.jamendo.renhaojie.hear.webservices;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Ren Haojie on 2017/2/12.
 */

public interface DownloadAPI {
    @Streaming
    @GET
    public Observable<ResponseBody> downloadTrack(@Url String url);
}
