package com.jamendo.renhaojie.hear.activities.track;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jamendo.renhaojie.hear.HearApplication;
import com.jamendo.renhaojie.hear.R;
import com.jamendo.renhaojie.hear.activities.AudioActivity;
import com.jamendo.renhaojie.hear.databse.DbOpenHelper;
import com.jamendo.renhaojie.hear.models.Track;
import com.jamendo.renhaojie.hear.providers.DownLoadProvider;
import com.jamendo.renhaojie.hear.providers.FavoriateProvider;
import com.jamendo.renhaojie.hear.services.AudioPlayService;
import com.jamendo.renhaojie.hear.services.download.DownloadService;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Ren Haojie on 2017/2/1.
 */

public class TrackActivity extends AudioActivity implements SeekBar.OnSeekBarChangeListener, TrackContract.ITrackView {
    private static final String TAG = "TrackActivity";

    public static final String KEY_TRACK_ID_LIST = "KEY_TRACK_ID_LIST";
    public static final String KEY_CURRENT_POSITION = "KEY_CURRENT_POSITION";
    private String[] trackIds;
    private int currentPosition;

    @BindView(R.id.trackImage)
    ImageView trackImage;
    @BindView(R.id.trackProgress)
    SeekBar trackProgress;
    @BindView(R.id.lblCurrentTime)
    TextView lblCurrentTime;
    @BindView(R.id.lblTotalTime)
    TextView lblTotalTime;
    @BindView(R.id.trackName)
    TextView trackName;
    @BindView(R.id.lblArtistName)
    TextView artistName;
    @BindView(R.id.btnPlay)
    ImageButton btnPlay;
    @BindView(R.id.btnFavorite)
    ImageButton btnFavorate;
    @BindView(R.id.frameImage)
    FrameLayout frameImage;

    private boolean isMarked = false;

    @OnClick(R.id.btnForward)
    public void onForwardClick(View view) {
        Intent intent = new Intent(this, AudioPlayService.class);
        intent.setAction(AudioPlayService.ACTION_FORWARD);
        startService(intent);
    }

    @OnClick(R.id.btnFavorite)
    public void onFavoriteClick(View view) {
        if (isMarked) {
            deleteMark(trackIds[currentPosition]);
        } else {
            mPresenter.addMark();
        }
    }

    @OnClick(R.id.btnDownload)
    public void onDownloadClick(View view) {
        Cursor cursor = getContentResolver().query(Uri.parse(DownLoadProvider.CONTENT_URI + "/" + trackIds[currentPosition]),
                new String[]{DbOpenHelper.DLD_FILE_PATH},
                null,
                null,
                null
        );
        boolean isExists = mPresenter.isExist();
        if (cursor.getCount() > 0 && isExists) {
            Toast.makeText(this, "该音乐已经下载", Toast.LENGTH_LONG).show();
        } else {
            if (cursor.getCount() == 0) {
                mPresenter.addDownload();
            }
            if (!isExists) {
                Toast.makeText(this, "已添加到下载列表", Toast.LENGTH_LONG).show();
                mPresenter.doDownload();
            }
        }
    }

    private boolean isplaying = false;

    @OnClick(R.id.btnPlay)
    public void onPlayClick(View view) {
        if (isplaying) {
            pause();
        } else {
            mPresenter.play();
        }
    }

    private void pause() {
        Intent intent = new Intent(this, AudioPlayService.class);
        intent.setAction(AudioPlayService.ACTION_PAUSE);
        startService(intent);
        btnPlay.setImageResource(R.drawable.play);
        isplaying = false;
    }

    @OnClick(R.id.btnRewind)
    public void onRewindClick(View view) {
        Intent intent = new Intent(this, AudioPlayService.class);
        intent.setAction(AudioPlayService.ACTION_REWIND);
        startService(intent);
    }

    @Inject
    TrackPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        setContentView(R.layout.activity_track);
        ButterKnife.bind(this);

        DaggerTrackComponent.builder()
                .albumServiceComponent(((HearApplication) getApplication()).getAlbumServiceComponent())
                .trackModule(new TrackModule(this))
                .build()
                .inject(this);

        trackIds = getIntent().getStringArrayExtra(KEY_TRACK_ID_LIST);
        currentPosition = getIntent().getIntExtra(KEY_CURRENT_POSITION, 0);

        mPresenter.loadTrackInfo(trackIds[currentPosition]);
        trackProgress.setOnSeekBarChangeListener(this);
        Intent intent = new Intent(this, AudioPlayService.class);
        bindService(intent, mConnection, BIND_AUTO_CREATE);
        mTimer.schedule(mTask, 3000, 1000);
    }

    @Override
    protected void onDestroy() {
        unbindService(mConnection);
        mTimer.cancel();
        Log.i(TAG, "TrackActivity destoried");
        super.onDestroy();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int progress = seekBar.getProgress();
        Intent intent = new Intent(this, AudioPlayService.class);
        intent.setAction(AudioPlayService.ACTION_SEEKTO);
        intent.putExtra(AudioPlayService.KEY_PROGRESS, progress);
        startService(intent);
        lblCurrentTime.setText(String.format("%d:%02d", seekBar.getProgress() / 1000 / 60, seekBar.getProgress() / 1000 % 60));
        Log.i(TAG, "seekTo " + progress);
    }

    @Override
    public void setTrackInfo(Track track) {

        Glide.with(this)
                .load(track.getImage())
                .placeholder(R.drawable.picture)
                .fitCenter()
                .into(trackImage);

        Uri uri = Uri.parse(FavoriateProvider.CONTENT_URI + "/" + track.getId());
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor.getCount() > 0) {
            isMarked = true;
            btnFavorate.setImageResource(R.drawable.ic_favorite_black_24dp);
        } else {
            isMarked = false;
            btnFavorate.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
        trackName.setText(track.getName());
        lblTotalTime.setText(String.format("%d:%02d", track.getDuration() / 60, track.getDuration() % 60));
        artistName.setText(track.getArtist_name());
        trackProgress.setMax(track.getDuration() * 1000);//million seconds毫秒
    }

    @Override
    public void play(String trackId, String url) {
        Intent intent = new Intent(this, AudioPlayService.class);
        intent.setAction(AudioPlayService.ACTION_PLAY);
        url = getLocalPath(trackId, url);
        intent.putExtra(AudioPlayService.KEY_URL, url);
        startService(intent);
        btnPlay.setImageResource(R.drawable.pause);
        isplaying = true;
    }

    @Override
    public void addMark(Track track) {
        ContentValues values = new ContentValues();
        values.put(DbOpenHelper.TRACK_ID, track.getId());
        values.put(DbOpenHelper.ALBUM_NAME, track.getAlbum_name());
        values.put(DbOpenHelper.TRACK_AUDIO, track.getAudio());
        values.put(DbOpenHelper.TRACK_IMAGE, track.getImage());
        values.put(DbOpenHelper.TRACK_NAME, track.getName());

        getContentResolver().insert(FavoriateProvider.CONTENT_URI, values);
        btnFavorate.setImageResource(R.drawable.ic_favorite_black_24dp);
    }

    @Override
    public void deleteMark(String trackId) {
        getContentResolver().delete(FavoriateProvider.CONTENT_URI, null, null);
        btnFavorate.setImageResource(R.drawable.ic_favorite_border_black_24dp);
    }

    @Override
    public void addDownload(ContentValues downloadInfo) {
        getContentResolver().insert(DownLoadProvider.CONTENT_URI,
                downloadInfo);
    }

    @Override
    public void doDownload(Track track) {
        Intent intent = new Intent(this, DownloadService.class);
        intent.putExtra(DownloadService.KEY_TRACK, track);
        startService(intent);
    }

    private Timer mTimer = new Timer();//TODO 定时器更新进度条

    private TimerTask mTask = new TimerTask() {
        @Override
        public void run() {
            Message msgGetProgress = Message.obtain(null, AudioPlayService.MSG_GET_PROGRESS);
            try {
                msgGetProgress.replyTo = mClientMessenger;
                mServiceMessenger.send(msgGetProgress);
                Log.i(TAG, "send message to Service to get current progress");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    private class MessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AudioPlayService.MSG_REPLAY_ISPLAYING:
                    isplaying = msg.getData().getBoolean(AudioPlayService.KEY_ISPLAYING);
                    Log.i(TAG, "isPlaying : " + isplaying);
                    if (isplaying) {
                        btnPlay.setImageResource(R.drawable.pause);
                    } else {
                        btnPlay.setImageResource(R.drawable.play);
                    }
                    break;
                case AudioPlayService.MSG_GET_PROGRESS_REPLY:
                    int progress = msg.getData().getInt(AudioPlayService.KEY_CURRENT_PROGRESS, 0);
                    boolean isplaying = msg.getData().getBoolean(AudioPlayService.KEY_ISPLAYING);
                    setCurrentProgress(progress);
                    Log.i(TAG, "current progress " + progress + " isplaying:" + isplaying);
                    break;
                case AudioPlayService.MSG_ON_AUDIO_COMPLETION:
                    if (currentPosition + 1 < trackIds.length) {
                        currentPosition++;
                        mPresenter.loadTrackInfo(trackIds[currentPosition]);
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private void setCurrentProgress(int progress) {
        lblCurrentTime.setText(String.format("%d:%02d", progress / 1000 / 60, progress / 1000 % 60));
        trackProgress.setProgress(progress);
    }

    private Messenger mClientMessenger = new Messenger(new MessageHandler());
    private Messenger mServiceMessenger;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mServiceMessenger = new Messenger(service);
            Message msg = Message.obtain(null, AudioPlayService.MSG_FROM_TRACK_CONNECTED);
            msg.replyTo = mClientMessenger;
            try {
                mServiceMessenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}
