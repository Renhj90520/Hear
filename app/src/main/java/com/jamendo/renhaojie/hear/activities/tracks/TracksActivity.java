package com.jamendo.renhaojie.hear.activities.tracks;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jamendo.renhaojie.hear.HearApplication;
import com.jamendo.renhaojie.hear.R;
import com.jamendo.renhaojie.hear.activities.AudioActivity;
import com.jamendo.renhaojie.hear.activities.track.TrackActivity;
import com.jamendo.renhaojie.hear.adapters.TrackListAdapter;
import com.jamendo.renhaojie.hear.databse.DbOpenHelper;
import com.jamendo.renhaojie.hear.models.AlbumTrack;
import com.jamendo.renhaojie.hear.providers.DownLoadProvider;
import com.jamendo.renhaojie.hear.services.AudioPlayService;
import com.jamendo.renhaojie.hear.utils.FileUtil;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Ren Haojie on 2017/1/7.
 */

public class TracksActivity extends AudioActivity implements TracksContract.ITracksView {
    public static final String EXTRA_ALBUM_ID = "EXTRA_ALBUM_ID";
    public static final String EXTRA_ALBUM_NAME = "EXTRA_ALBUM_NAME";
    public static final String EXTRA_ALBUM_IMAGE = "EXTRA_ALBUM_IMAGE";
    private static final String TAG = "TracksActivity";
    @BindView(R.id.image_album)
    ImageView image_album;
    @BindView(R.id.album_name)
    TextView album_name;
    @BindView(R.id.tracks_list)
    RecyclerView tracks_list;
    @BindView(R.id.track_progress)
    ProgressBar track_progress;
    @BindView(R.id.tracks_fab)
    FloatingActionButton tracks_fab;
    @BindView(R.id.action_container)
    RelativeLayout action_container;
    @BindView(R.id.currentName)
    TextView lblCurrentName;
    @BindView(R.id.btnPrev)
    ImageButton btnPrev;
    @BindView(R.id.btnPlayOrPause)
    ImageButton btnPlayOrPause;
    @BindView(R.id.btnNext)
    ImageButton btnNext;

    @Inject
    TracksPresenter mPresenter;

    @OnClick(R.id.tracks_fab)
    public void onPlayFabClick(View view) {
        doPlayOrPause();
    }

    @OnClick(R.id.btnPlayOrPause)
    public void onPlayOrPauseClick(View view) {
        doPlayOrPause();
    }

    @OnClick(R.id.btnPrev)
    public void onPrev(View view) {
        mPresenter.prev();
    }

    @OnClick(R.id.btnNext)
    public void onNext(View view) {
        mPresenter.next();
    }

    private void doPlayOrPause() {
        if (isPlaying) {
            mPresenter.pause();
        } else {
            mPresenter.play();
        }
    }

    public class MessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AudioPlayService.MSG_ON_AUDIO_COMPLETION:
                    Log.i(TAG, "Message from service for completion");
                    mPresenter.next();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private Messenger mGetReplayMessenger = new Messenger(new MessageHandler());

    private int currentIndex;
    private boolean isPlaying = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        setContentView(R.layout.activity_tracks);
        ButterKnife.bind(this);
        Intent intent = getIntent();

//        DaggerTracksComponent.builder()
//                .
        DaggerTracksComponent.builder()
                .albumServiceComponent(((HearApplication) getApplication()).getAlbumServiceComponent())
                .tracksModule(new TracksModule(this))
                .build().inject(this);
        if (intent != null) {
            String albumid = intent.getStringExtra(EXTRA_ALBUM_ID);
            String albumname = intent.getStringExtra(EXTRA_ALBUM_NAME);
            byte[] imageBytes = intent.getByteArrayExtra(EXTRA_ALBUM_IMAGE);
            if (imageBytes != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                image_album.setImageBitmap(bitmap);
                Palette.Builder builder = new Palette.Builder(bitmap);
                album_name.setBackgroundColor(builder.generate().getDarkVibrantColor(Color.BLACK));
                album_name.setText(albumname);
                tracks_list.setLayoutManager(new LinearLayoutManager(TracksActivity.this, LinearLayoutManager.VERTICAL, false));
                mPresenter.loatTracks(albumid);
            }
        }
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "TracksActivity stoped");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "TracksActivity destoried");
        if (isBound) {
            unbindService(mConnection);
        }
        super.onDestroy();
    }

    @Override
    public void showProgress() {
        track_progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        track_progress.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setItems(AlbumTrack[] tracks) {
        TrackListAdapter adapter = new TrackListAdapter(this, tracks);
        adapter.setOnTrackClickListener(mPresenter);
        tracks_list.setAdapter(adapter);
    }

    @Override
    public void play(String trackId, String url) {
        isPlaying = true;
        if (action_container.getVisibility() == View.GONE) {
            action_container.setVisibility(View.VISIBLE);
        }

        Intent intent = new Intent(this, AudioPlayService.class);
        intent.setAction(AudioPlayService.ACTION_PLAY);
        url = getLocalPath(trackId, url);
        intent.putExtra(AudioPlayService.KEY_URL, url);

        startService(intent);
        bindService(intent, mConnection, BIND_AUTO_CREATE);
        tracks_fab.setImageResource(R.drawable.ic_pause_black_24dp);
        btnPlayOrPause.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);
    }


    @Override
    public void pause() {
        isPlaying = false;

        Intent intent = new Intent(this, AudioPlayService.class);
        intent.setAction(AudioPlayService.ACTION_PAUSE);
        startService(intent);
        tracks_fab.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        btnPlayOrPause.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
    }

    @Override
    public void setCurrentPlayingName(String currentName) {
        lblCurrentName.setText(currentName);
    }

    @Override
    public void setCurrentPlayingIndex(int position) {
        TrackListAdapter adapter = (TrackListAdapter) tracks_list.getAdapter();
        tracks_list.scrollToPosition(position);
        adapter.setCurrentPlayIndex(position);
        adapter.notifyItemChanged(position);
    }

    @Override
    public void navigateToTrack(String[] trackIds, int position) {
        mPresenter.play(position);

        Intent intent = new Intent(this, TrackActivity.class);
        intent.putExtra(TrackActivity.KEY_TRACK_ID_LIST, trackIds);
        intent.putExtra(TrackActivity.KEY_CURRENT_POSITION, position);
        startActivity(intent);
        Log.i(TAG, "Navigate to TrackActivity");
    }

    private boolean isBound = false;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            isBound = true;
            Messenger toService = new Messenger(service);
            Message msg = Message.obtain(null, AudioPlayService.MSG_FROM_TRACKS_CONNECTED);
            msg.replyTo = mGetReplayMessenger;
            try {
                toService.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };
}
