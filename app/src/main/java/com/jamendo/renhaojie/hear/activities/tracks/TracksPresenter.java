package com.jamendo.renhaojie.hear.activities.tracks;

import com.jamendo.renhaojie.hear.adapters.TrackListAdapter;
import com.jamendo.renhaojie.hear.models.AlbumTrack;
import com.jamendo.renhaojie.hear.utils.FileUtil;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Ren Haojie on 2017/1/12.
 */

public class TracksPresenter implements TracksContract.ITracksPresenter, ITracksInteractor.OnTracksLoadFinishListener, TrackListAdapter.OnTrackClickListener {
    private AlbumTrack[] mTracks;
    private TracksContract.ITracksView mTrackView;
    private ITracksInteractor mTrackInteractor;
    private int currentPlayIndex = 0;

    private ArrayList<String> trackIds = new ArrayList<>();

    @Inject
    public TracksPresenter(TracksContract.ITracksView trackView, ITracksInteractor trackInteractor) {
        mTrackView = trackView;
        mTrackInteractor = trackInteractor;
    }

    @Override
    public void loatTracks(String albumId) {
        mTrackView.showProgress();
        mTrackInteractor.loadTracks(albumId, this);
    }

    @Override
    public void play() {
        playCurrent();
    }

    @Override
    public void play(int position) {
        if (position < mTracks.length) {
            currentPlayIndex = position;

            playCurrent();
        }
    }

    private void playCurrent() {
        AlbumTrack track = mTracks[currentPlayIndex];
        mTrackView.play(track.getId(), track.getAudio());
        mTrackView.setCurrentPlayingName(track.getName());
        mTrackView.setCurrentPlayingIndex(currentPlayIndex);
    }

    @Override
    public void pause() {
        mTrackView.pause();
    }

    @Override
    public void stop() {

    }

    @Override
    public void next() {
        if (currentPlayIndex < mTracks.length) {
            currentPlayIndex++;
            play(currentPlayIndex);
        }
    }

    @Override
    public void prev() {
        if (currentPlayIndex > 0) {
            currentPlayIndex--;
            play(currentPlayIndex);
        }
    }

    @Override
    public void onTrackLoadFinish(AlbumTrack[] tracks) {
        mTracks = tracks;
        for (AlbumTrack track : mTracks) {
            trackIds.add(track.getId());
        }
        mTrackView.setItems(tracks);
        mTrackView.hideProgress();
    }

    @Override
    public void onTrackClick(int position) {
        String[] ids = new String[trackIds.size()];
        ids = trackIds.toArray(ids);
        mTrackView.navigateToTrack(ids, position);
    }
}
