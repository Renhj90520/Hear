package com.jamendo.renhaojie.hear.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.jamendo.renhaojie.hear.utils.AudioPlayer;

/**
 * Created by Ren Haojie on 2017/1/30.
 */

public class AudioPlayService extends Service implements MediaPlayer.OnCompletionListener {

    public static final String ACTION_PLAY = "play";
    public static final String ACTION_STOP = "stop";
    public static final String ACTION_PAUSE = "pause";
    public static final String ACTION_FORWARD = "forward";
    public static final String ACTION_REWIND = "rewind";
    public static final String ACTION_SEEKTO = "seekto";

    public static final String KEY_URL = "Current Url";
    public static final int MSG_FROM_TRACKS_CONNECTED = 100;
    public static final int MSG_ON_AUDIO_COMPLETION = 101;
    public static final int MSG_FROM_TRACK_CONNECTED = 102;
    public static final int MSG_REPLAY_ISPLAYING = 103;
    public static final int MSG_GET_PROGRESS = 104;
    public static final int MSG_GET_PROGRESS_REPLY = 105;

    public static final String KEY_ISPLAYING = "KEY_ISPLAYING";
    public static final String KEY_PROGRESS = "KEY_PROGRESS";
    public static final String KEY_CURRENT_PROGRESS = "KEY_CURRENT_PROGRESS";

    private static final String TAG = "AudioPlayService";

    private String currentState = "stop";

    AudioPlayer mPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        mPlayer = new AudioPlayer();
        Log.i(TAG, ">>>>>>>>>>AudioPlayService created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();

            switch (action) {
                case ACTION_PLAY:
                    String url = intent.getStringExtra(KEY_URL);
                    mPlayer.play(url);
                    mPlayer.setOnAudioCompletionListener(this);
                    currentState = ACTION_PLAY;
                    Log.i(TAG, action);
                    break;
                case ACTION_PAUSE:
                    mPlayer.pause();
                    currentState = ACTION_PAUSE;
                    Log.i(TAG, action);
                    break;
                case ACTION_STOP:
                    mPlayer.stop();
                    currentState = ACTION_STOP;
                    break;
                case ACTION_FORWARD:
                    mPlayer.forward();
                    break;
                case ACTION_REWIND:
                    mPlayer.rewind();
                    break;
                case ACTION_SEEKTO:
                    int progress = intent.getIntExtra(KEY_PROGRESS, 0);
                    mPlayer.seekTo(progress);
                    break;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, ">>>>>>>>>>>AudioPlayService destoried");
        super.onDestroy();
    }

    private Messenger mReplyTracksMessenger;
    private Messenger mReplyTrackMessenger;

    private class MessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_FROM_TRACKS_CONNECTED:
                    mReplyTracksMessenger = msg.replyTo;
                    break;
                case MSG_FROM_TRACK_CONNECTED:
                    mReplyTrackMessenger = msg.replyTo;

                    Message isPlayingMsg = Message.obtain(null, MSG_REPLAY_ISPLAYING);
                    Bundle isplayingBundle = new Bundle();
                    isplayingBundle.putBoolean(KEY_ISPLAYING, mPlayer.isPlaying());
                    isPlayingMsg.setData(isplayingBundle);
                    try {
                        mReplyTrackMessenger.send(isPlayingMsg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case MSG_GET_PROGRESS:
                    Message msgProgress = Message.obtain(null, MSG_GET_PROGRESS_REPLY);
                    Bundle bundleProgress = new Bundle();
                    bundleProgress.putInt(KEY_CURRENT_PROGRESS, mPlayer.getCurrentProgress());
                    bundleProgress.putBoolean(KEY_ISPLAYING, mPlayer.isPlaying());
                    msgProgress.setData(bundleProgress);
                    Messenger reply = msg.replyTo;
                    try {
                        reply.send(msgProgress);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private final Messenger mMessenger = new Messenger(new MessageHandler());

    @Override
    public void onCompletion(MediaPlayer mp) {
//        Log.i(TAG, "current progress-------->" + mPlayer.getCurrentProgress() + "\r\n ------------>duration" + mp.getDuration());
//        int currentProgress = mPlayer.getCurrentProgress();
//        int totalDuration = mp.getDuration() * 1000;
//        if (currentProgress == totalDuration) {
        Message message = Message.obtain(null, MSG_ON_AUDIO_COMPLETION);

        try {
            mPlayer.setPlaying(false);
            mReplyTracksMessenger.send(message);
            if (mReplyTrackMessenger != null) {
                Message msg = Message.obtain(null, MSG_ON_AUDIO_COMPLETION);
                mReplyTrackMessenger.send(msg);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
//        } else {
//            mPlayer.seekTo(Math.max(currentProgress, totalDuration));
//        }
    }
}
