package com.jamendo.renhaojie.hear.utils;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ProgressBar;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Ren Haojie on 2017/1/9.
 */

public class AudioPlayer implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnPreparedListener {
    private static final String TAG = "AudioPlayer";
    private MediaPlayer mMediaPlayer;
    private ProgressBar seekProgress;
    private boolean isPaused = false;
    private String current_url = "";

    public AudioPlayer() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

    }

    public void play(String audioUrl) {
        try {
            Log.i(TAG, "current url:" + current_url);
            Log.i(TAG, "audioUrl:" + audioUrl);
            if (isPaused && current_url.equals(audioUrl)) {
                mMediaPlayer.start();
            } else {
                mMediaPlayer.reset();
                mMediaPlayer.setDataSource(audioUrl);
                mMediaPlayer.prepareAsync();

                mMediaPlayer.setOnBufferingUpdateListener(this);
                mMediaPlayer.setOnPreparedListener(this);
                current_url = audioUrl;
            }
            isPlaying = true;
            isPaused = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        int currentposition = mMediaPlayer.getCurrentPosition();
        Log.i(TAG, "play: currentposition:" + currentposition);
        mMediaPlayer.seekTo(currentposition);
    }

    public void pause() {
//        mMediaPlayer != null &&
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            isPaused = true;
        }
        isPlaying = false;
    }

    public void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
            current_url = "";
        }
        isPlaying = false;
    }

    private static final int step = 10 * 1000;//10秒

    public void forward() {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(mMediaPlayer.getCurrentPosition() + step);
        }
    }

    public void rewind() {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(mMediaPlayer.getCurrentPosition() - step);
        }
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    private boolean isPlaying = false;

    public boolean isPlaying() {
        return mMediaPlayer == null ? false : isPlaying || mMediaPlayer.isPlaying();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        Log.i("Buffering", "onPrepared: start playing ? " + mp.isPlaying());
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
//        Log.d("Buffering", "currentPosition: " + mp.getCurrentPosition() + " duration: " + mp.getDuration() + "percent:" + percent);

        if (!isPaused) {
            mp.start();
        }
    }

    public void setOnAudioCompletionListener(MediaPlayer.OnCompletionListener listener) {
        mMediaPlayer.setOnCompletionListener(listener);
    }

    public void seekTo(int progress) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(progress);
        }
    }

    public int getCurrentProgress() {
        return mMediaPlayer.getCurrentPosition();
    }
}
