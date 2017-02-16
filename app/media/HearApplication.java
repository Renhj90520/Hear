package com.jamendo.renhaojie.hear;

import android.app.Application;
import android.content.Intent;

import com.jamendo.renhaojie.hear.media.PlayList;
import com.jamendo.renhaojie.hear.media.PlayerEngine;
import com.jamendo.renhaojie.hear.services.PlayerService;

/**
 * Created by Ren Haojie on 2017/1/10.
 */

public class HearApplication extends Application {

    private static HearApplication instance;

    public PlayerEngine mServicePlayerEngine;

    private PlayerEngine mIntentPlayerEngine;

    public static HearApplication getInstance() {
        return instance;
    }

    private PlayList mPlayList;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
    }

    public void setConcretePlayerEngine(PlayerEngine playerEngine) {
        this.mServicePlayerEngine = playerEngine;
    }

    public PlayerEngine getPlayerEngineInterface() {
        if (mIntentPlayerEngine == null) {
            mIntentPlayerEngine = new IntentPlayerEngine();
        }
        return mIntentPlayerEngine;
    }

    public PlayList fetchPlaylist() {
        return mPlayList;
    }

    private class IntentPlayerEngine implements PlayerEngine {

        @Override
        public void openPlaylist(PlayList playList) {
            mPlayList = playList;
            if (mServicePlayerEngine != null) {
                mServicePlayerEngine.openPlaylist(playList);
            }
        }

        @Override
        public void play() {
            if (mServicePlayerEngine != null) {
                mServicePlayerEngine.play();
            } else {
                startAction(PlayerService.ACTION_PLAY);
            }
        }

        @Override
        public boolean isPlaying() {
            return mServicePlayerEngine == null ? false : mServicePlayerEngine.isPlaying();
        }

        @Override
        public void pause() {
            if (mServicePlayerEngine != null) {
                mServicePlayerEngine.pause();
            }
        }

        @Override
        public void stop() {
            startAction(PlayerService.ACTION_STOP);
        }

        @Override
        public void forward() {
            if (mServicePlayerEngine != null) {
                mServicePlayerEngine.forward();
            }
        }

        @Override
        public void rewind() {
            if (mServicePlayerEngine != null) {
                mServicePlayerEngine.rewind();
            }
        }

        @Override
        public PlayList getPlaylist() {
            return mPlayList;
        }

        private void startAction(String action) {
            Intent intent = new Intent(HearApplication.this, PlayerService.class);
            intent.setAction(action);
            startService(intent);
        }
    }
}
