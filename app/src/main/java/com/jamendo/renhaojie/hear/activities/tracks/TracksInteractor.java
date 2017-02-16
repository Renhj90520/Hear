package com.jamendo.renhaojie.hear.activities.tracks;

import com.jamendo.renhaojie.hear.models.Headers;
import com.jamendo.renhaojie.hear.models.Response;
import com.jamendo.renhaojie.hear.models.TracksUnderAlbum;
import com.jamendo.renhaojie.hear.webservices.AlbumService;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Ren Haojie on 2017/1/12.
 */

public class TracksInteractor implements ITracksInteractor {
//    private OnTracksLoadFinishListener mListener;

//    public TracksInteractor(OnTracksLoadFinishListener listener) {
//        mListener = listener;
//    }

    AlbumService service;

    @Inject
    public TracksInteractor(AlbumService service) {
        this.service = service;
    }

    @Override
    public void loadTracks(String albumId, final OnTracksLoadFinishListener mListener) {
        Observable<Response<TracksUnderAlbum>> call = service.loadTracksUnderAlbum(albumId);
        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<TracksUnderAlbum>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Response<TracksUnderAlbum> tracksResponse) {
                        Headers headers = tracksResponse.getHeaders();
                        if (headers != null && headers.getCode() == 0) {
                            if (tracksResponse.getResults().size() > 0) {
                                TracksUnderAlbum album = tracksResponse.getResults().get(0);
                                mListener.onTrackLoadFinish(album.getTracks());
                                //TODO
//                    PlayList playList = new PlayList();
//                    for (AlbumTrack track : mTracks) {
//                        playList.addTrack(track);
//                    }
//                    getPlayerEngine().openPlaylist(playList);
//                    getPlayerEngine().play();
                            }
                        }
                    }
                });
    }

//    private Callback<Response<TracksUnderAlbum>> mTracksCallback = new Callback<Response<TracksUnderAlbum>>() {
//        @Override
//        public void onResponse(Call<Response<TracksUnderAlbum>> call, retrofit2.Response<Response<TracksUnderAlbum>> response) {
//            Response<TracksUnderAlbum> tracksResponse = response.body();
//            Headers headers = tracksResponse.getHeaders();
//            if (headers != null && headers.getCode() == 0) {
//                if (tracksResponse.getResults().size() > 0) {
//                    TracksUnderAlbum album = tracksResponse.getResults().get(0);
//                    mListener.onTrackLoadFinish(album.getTracks());
//                    //TODO
////                    PlayList playList = new PlayList();
////                    for (AlbumTrack track : mTracks) {
////                        playList.addTrack(track);
////                    }
////                    getPlayerEngine().openPlaylist(playList);
////                    getPlayerEngine().play();
//                }
//            }
//
//        }
//
//        @Override
//        public void onFailure(Call<Response<TracksUnderAlbum>> call, Throwable t) {
//
//        }
//    };
}
