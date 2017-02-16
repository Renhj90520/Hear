package com.jamendo.renhaojie.hear.utils.download;

/**
 * Created by Ren Haojie on 2017/2/12.
 */

public interface DownloadProgressListener {
    void update(long bytesRead, long contentLength, boolean done);
}
