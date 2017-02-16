package com.jamendo.renhaojie.hear.webservices;

import com.jamendo.renhaojie.hear.utils.download.DownloadProgressListener;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by Ren Haojie on 2017/2/12.
 */

public class DownloadProgressInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalRespone = chain.proceed(chain.request());
        return originalRespone.newBuilder()
                .body(new DownloadProgressResponseBody(originalRespone.body()))
                .build();
    }
}
