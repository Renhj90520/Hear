package com.jamendo.renhaojie.hear.utils.download;

import android.content.Context;
import android.util.Log;

import com.jamendo.renhaojie.hear.models.Album;
import com.jamendo.renhaojie.hear.models.Track;
import com.jamendo.renhaojie.hear.utils.FileUtil;
import com.jamendo.renhaojie.hear.utils.RxBus;
import com.jamendo.renhaojie.hear.webservices.AlbumService;
import com.jamendo.renhaojie.hear.webservices.DownloadAPI;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Ren Haojie on 2017/2/9.
 */

public class DownloadJob {
    private static final String TAG = "DownloadJob";

    DownloadState mState;
    DownloadCompleteListener mCompleteListener;

    private Track mTrack;
    private int mStartId;
    private Context mContext;
    private DownloadAPI mService;

    private long mProgress;
    private long mTotalSize;
    private long mDownloadedSize;

    Observable<ResponseBody> call;

    public DownloadJob(Context context, DownloadAPI service, Track track, int startId) {
        mContext = context;
        mService = service;
        mTrack = track;
        mStartId = startId;
        mState = DownloadState.PrepareDownload;
    }

    public void start() {
        mState = DownloadState.Downloading;
        call = mService.downloadTrack(mTrack.getAudiodownload());
        call.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        mTotalSize = responseBody.contentLength();
                        FileUtil.saveFile(responseBody, FileUtil.getDir(mContext), mTrack.getFileName(), this);
                        mState = DownloadState.Downloaded;
                        if (mCompleteListener != null) {
                            mCompleteListener.onDownloadComplete(DownloadJob.this);
                        }
                        Log.i(TAG, "download complete");
                    }
                });

        Subscription subscription = RxBus.getInstance().doSubscribe(FileLoadEvent.class, new Action1<FileLoadEvent>() {
            @Override
            public void call(FileLoadEvent fileLoadEvent) {
                Log.i(TAG, "In downlaodJob current read bytes:" + fileLoadEvent.getBytesLoaded() + " total size:" + fileLoadEvent.getTotalSize());
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        });
        RxBus.getInstance().addSubscription(this, subscription);
    }

    public void pause() {

    }

    public void resume() {

    }

    public void cancel() {

    }

    public long getTotalSize() {
        return mTotalSize;
    }

    public void setTotalSize(long totalSize) {
        mTotalSize = totalSize;
    }

    public long getProgress() {
        return mProgress;
    }

    public void setProgress(long progress) {
        mProgress = progress;
    }

    public long getDownloadedSize() {
        return mDownloadedSize;
    }

    public void setDownloadedSize(long downloadedSize) {
        mDownloadedSize = downloadedSize;
    }

    public void setDownloadCompleteListener(DownloadCompleteListener listener) {
        mCompleteListener = listener;
    }
}
