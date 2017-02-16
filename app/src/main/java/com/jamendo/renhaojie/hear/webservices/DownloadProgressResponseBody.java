package com.jamendo.renhaojie.hear.webservices;

import android.util.Log;

import com.jamendo.renhaojie.hear.utils.RxBus;
import com.jamendo.renhaojie.hear.utils.download.DownloadProgressListener;
import com.jamendo.renhaojie.hear.utils.download.FileLoadEvent;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by Ren Haojie on 2017/2/12.
 */

public class DownloadProgressResponseBody extends ResponseBody {
    private static final String TAG = "DownloadResponseBody";

    private ResponseBody mResponseBody;
    private BufferedSource mBufferedSource;

    public DownloadProgressResponseBody(ResponseBody responseBody) {
        mResponseBody = responseBody;
    }

    @Override
    public MediaType contentType() {
        return mResponseBody.contentType();
    }

    @Override
    public long contentLength() {
        return mResponseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (mBufferedSource == null) {
            mBufferedSource = Okio.buffer(source(mResponseBody.source()));
        }
        return mBufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);

                totalBytesRead += bytesRead != -1 ? bytesRead : 0;

                Log.i(TAG, "current bytes readed:" + totalBytesRead);
                RxBus.getInstance().post(new FileLoadEvent(totalBytesRead, contentLength()));
                return bytesRead;
            }
        };
    }
}
