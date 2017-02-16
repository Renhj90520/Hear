package com.jamendo.renhaojie.hear.media;

import android.media.MediaPlayer;

import com.jamendo.renhaojie.hear.models.AlbumTrack;

import java.io.IOException;

/**
 * Created by Ren Haojie on 2017/1/9.
 */

public class PlayerEngineImpl implements PlayerEngine, MediaPlayer.OnPreparedListener {
    private InternalMedialPlayer mMediaPlayer;
    private static final int step = 10 * 1000;//10秒

    private PlayList mPlayList = null;

    @Override
    public void openPlaylist(PlayList playList) {
        if (!playList.isEmpty()) {
            mPlayList = playList;
        } else {
            mPlayList = null;
        }
    }

    @Override
    public void play() {
        if (mPlayList != null) {

            if (mMediaPlayer == null) {
                mMediaPlayer = build();
            }

            if (mMediaPlayer != null && mMediaPlayer.track != mPlayList.getSelectedTrack()) {
                cleanup();
                mMediaPlayer = build();
            }

            if (mMediaPlayer == null)
                return;

            if (!mMediaPlayer.preparing) {

                if (!mMediaPlayer.isPlaying()) {
                    mMediaPlayer.start();
                }
            } else {
                mMediaPlayer.playAfterPreparing = true;
            }
        }
    }

    private void cleanup() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public boolean isPlaying() {
        return mMediaPlayer == null ? false : mMediaPlayer.isPlaying();
    }

    @Override
    public void pause() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
    }

    @Override
    public void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public void forward() {
        mMediaPlayer.seekTo(mMediaPlayer.getCurrentPosition() + step);
    }

    @Override
    public void rewind() {
        mMediaPlayer.seekTo(mMediaPlayer.getCurrentPosition() - step);
    }

    @Override
    public PlayList getPlaylist() {
        return mPlayList;
    }

    public InternalMedialPlayer build() {
        InternalMedialPlayer mediaPlayer = new InternalMedialPlayer();
        String path = "";
        //TODO 从本地查找

        if (path.length() == 0) {
            path = mPlayList.getSelectedTrack().getAudio();
        }
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.track = mPlayList.getSelectedTrack();
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.prepareAsync();
            mediaPlayer.preparing = true;
            return mediaPlayer;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }


    private class InternalMedialPlayer extends MediaPlayer {
        public AlbumTrack track;
        public boolean preparing = false;
        public boolean playAfterPreparing = false;
    }
}
