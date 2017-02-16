package com.jamendo.renhaojie.hear.services.download;

import android.app.DownloadManager;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.jamendo.renhaojie.hear.HearApplication;
import com.jamendo.renhaojie.hear.models.Track;
//import com.jamendo.renhaojie.hear.utils.download.DownloadJob;
//import com.jamendo.renhaojie.hear.utils.download.DownloadManager;
//import com.jamendo.renhaojie.hear.webservices.DownloadAPI;

import java.util.HashMap;

import javax.inject.Inject;

/**
 * Created by Ren Haojie on 2017/2/9.
 */

public class DownloadService extends Service {

    private static final String TAG = "DownloadService";
    public static final String KEY_TRACK = "KEY_TRACK";

    private HashMap<String, Long> urlidMap = new HashMap<>();
    //    @Inject
//    DownloadAPI mService;
//    @Inject
//    DownloadManager mDownloadManager;
    DownloadManager mDownloadManager;

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerDownloadServiceComponent.builder().downloadComponent(((HearApplication) getApplication()).getDownloadComponent())
                .build().inject(this);

        mDownloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        Log.i(TAG, "DownloadService created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            Track track = intent.getParcelableExtra(KEY_TRACK);
//        DownloadJob dJob = new DownloadJob(this, mService, track, startId);
//        mDownloadManager.addDownloadJob(dJob);
//        mDownloadManager.doDownload();
            boolean isExist = false;
            String url = track.getAudiodownload();
            if (urlidMap.containsKey(url)) {
                long id = urlidMap.get(url);
                Cursor existCursor = mDownloadManager.query(new DownloadManager.Query().setFilterById(id));
                isExist = existCursor.getCount() > 0;
            }
            if (!isExist) {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_MUSIC, "hear/" + track.getFileName());
                mDownloadManager.enqueue(request);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "DownlaodService destoried");
        super.onDestroy();
    }
}
