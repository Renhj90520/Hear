package com.jamendo.renhaojie.hear.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.jamendo.renhaojie.hear.HearApplication;
import com.jamendo.renhaojie.hear.media.PlayerEngine;
import com.jamendo.renhaojie.hear.media.PlayerEngineImpl;

/**
 * Created by Ren Haojie on 2017/1/10.
 */

public class PlayerService extends Service {
    public static final String ACTION_PLAY = "play";
    public static final String ACTION_NEXT = "next";
    public static final String ACTION_PREV = "prev";
    public static final String ACTION_STOP = "stop";
    private static final String TAG = "PlayerService";

    private PlayerEngine mPlayerEngine;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "PlayerService onCreate");

        mPlayerEngine = new PlayerEngineImpl();
        HearApplication.getInstance().setConcretePlayerEngine(mPlayerEngine);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int flg = super.onStartCommand(intent, flags, startId);
        if (intent != null) {
            String action = intent.getAction();
            Log.i(TAG, "onStartCommand: " + action);

            switch (action) {
                case ACTION_PLAY:
                    updatePlaylist();

                    mPlayerEngine.play();
                    break;
                case ACTION_PREV:

                    break;
                case ACTION_NEXT:

                    break;
                case ACTION_STOP:
                    stopSelfResult(startId);
                    break;
            }
        }
        return flg;
    }

    private void updatePlaylist() {
        if (mPlayerEngine.getPlaylist() != HearApplication.getInstance().fetchPlaylist()) {
            mPlayerEngine.openPlaylist(HearApplication.getInstance().fetchPlaylist());
        }
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Player Service onDestroy");

        HearApplication.getInstance().setConcretePlayerEngine(null);
        if (mPlayerEngine != null) {
            mPlayerEngine.stop();
            mPlayerEngine = null;
        }
        super.onDestroy();
    }
}
