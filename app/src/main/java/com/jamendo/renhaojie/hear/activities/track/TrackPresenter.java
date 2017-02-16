package com.jamendo.renhaojie.hear.activities.track;

import android.content.ContentValues;
import android.content.Intent;

import com.jamendo.renhaojie.hear.adapters.TrackListAdapter;
import com.jamendo.renhaojie.hear.databse.DbOpenHelper;
import com.jamendo.renhaojie.hear.models.Track;
import com.jamendo.renhaojie.hear.services.download.DownloadService;
import com.jamendo.renhaojie.hear.utils.FileUtil;
import com.jamendo.renhaojie.hear.webservices.AlbumService;

import java.io.InputStream;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Ren Haojie on 2017/2/3.
 */

public class TrackPresenter implements TrackContract.ITrackPresenter, ITrackInteractor.OnTrackInfoLoadedListener {

    private TrackContract.ITrackView mView;
    private ITrackInteractor mInteractor;
    private Track mTrack;

    @Inject
    public TrackPresenter(ITrackInteractor interactor, TrackContract.ITrackView view) {
        mInteractor = interactor;
        mView = view;
    }

    @Override
    public void loadTrackInfo(String trackId) {
        mInteractor.loadTrackInfo(trackId, this);
    }

    @Override
    public void play() {
        mView.play(mTrack.getId(), mTrack.getAudio());
    }

    @Override
    public void addMark() {
        mView.addMark(mTrack);
    }

    @Override
    public void doDownload() {
        mView.doDownload(mTrack);
    }

    @Inject
    AlbumService mService;

    @Override
    public boolean isExist() {
        return FileUtil.isExists((TrackActivity) mView, mTrack.getFileName());
    }

    @Override
    public void addDownload() {
        ContentValues values = new ContentValues();
        values.put(DbOpenHelper.TRACK_ID, mTrack.getId());
        values.put(DbOpenHelper.TRACK_AUDIO, mTrack.getAudio());
        values.put(DbOpenHelper.TRACK_IMAGE, mTrack.getImage());
        values.put(DbOpenHelper.TRACK_NAME, mTrack.getName());
        values.put(DbOpenHelper.ALBUM_NAME, mTrack.getAlbum_name());
        values.put(DbOpenHelper.DLD_FILE_PATH, FileUtil.getFilePath((TrackActivity) mView, mTrack.getFileName()));
        mView.addDownload(values);
    }

    @Override
    public void onTrackInfoLoaded(Track track) {
        mTrack = track;
        mView.setTrackInfo(track);
    }


}
