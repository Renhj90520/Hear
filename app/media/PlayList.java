package com.jamendo.renhaojie.hear.media;

import com.jamendo.renhaojie.hear.models.Album;
import com.jamendo.renhaojie.hear.models.AlbumTrack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Ren Haojie on 2017/1/10.
 */

public class PlayList {
    private List<AlbumTrack> mTracks;

    private int mSelectedTrack;

    private PlaylistPlaybackMode mPlaylistPlaybackMode = PlaylistPlaybackMode.NORMAL;

    private List<Integer> mOrder = new ArrayList<>();

    private int selected;

    public PlayList() {
        mTracks = new ArrayList<>();
        calculateOrder(true);
    }

    public void addTrack(AlbumTrack track) {
        mTracks.add(track);
        mOrder.add(mTracks.size() - 1);
    }

    public void addTracks(List<AlbumTrack> tracks) {
        for (AlbumTrack track : tracks) {
            addTrack(track);
        }
    }

    public boolean isEmpty() {
        return mTracks.size() <= 0;
    }

    public void selectNext() {
        if (!isEmpty()) {
            selected++;
            selected %= mTracks.size();
        }
    }

    public void selectPrev() {
        if (!isEmpty()) {
            selected--;
            if (selected < 0) {
                selected = mTracks.size() - 1;
            }
        }
    }

    public void select(int index) {
        if (!isEmpty()) {
            if (index >= 0 && index < mTracks.size()) {
                selected = mOrder.indexOf(index);
            }
        }
    }

    public int getSelectedIndex() {
        if (isEmpty())
            selected = -1;

        if (selected == -1 && !isEmpty()) {
            selected = 0;
        }

        return selected;
    }

    public AlbumTrack getSelectedTrack() {
        int index = getSelectedIndex();
        if (index == -1)
            return null;

        index = mOrder.get(index);
        if (index == -1) {
            return null;
        }

        return mTracks.get(index);
    }

    public int size() {
        return mTracks == null ? 0 : mTracks.size();
    }

    public AlbumTrack getTrack(int index) {
        return mTracks.get(index);
    }

    public boolean isLastTrack() {
        return selected == size() - 1;
    }

    public PlaylistPlaybackMode getPlaylistPlaybackMode() {
        return mPlaylistPlaybackMode;
    }

    public void setPlaylistPlaybackMode(PlaylistPlaybackMode mode) {
        boolean force = false;

        switch (mode) {
            case NORMAL:
            case REPEAT:
                if (mPlaylistPlaybackMode == PlaylistPlaybackMode.SHUFFLE) {
                    force = true;
                }
                break;
            case SHUFFLE:
                if (mPlaylistPlaybackMode == PlaylistPlaybackMode.NORMAL || mPlaylistPlaybackMode == PlaylistPlaybackMode.REPEAT)
                    force = true;
                break;
        }
        mPlaylistPlaybackMode = mode;
        calculateOrder(force);
    }

    private void calculateOrder(boolean force) {
        if (mOrder.isEmpty() || force) {
            int oldSelected = 0;

            if (!mOrder.isEmpty()) {
                oldSelected = mOrder.get(selected);
                mOrder.clear();
            }

            for (int i = 0; i < mTracks.size(); i++) {
                mOrder.add(i, i);
            }

            switch (mPlaylistPlaybackMode) {
                case NORMAL:
                case REPEAT:
                    selected = oldSelected;
                    break;
                case SHUFFLE:
                    Collections.shuffle(mOrder);
                    selected = mOrder.indexOf(selected);
                    break;
            }
        }
    }


    public enum PlaylistPlaybackMode {
        NORMAL, SHUFFLE, REPEAT
    }
}
