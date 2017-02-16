package com.jamendo.renhaojie.hear.activities.track;

import com.jamendo.renhaojie.hear.models.Headers;
import com.jamendo.renhaojie.hear.models.Response;
import com.jamendo.renhaojie.hear.models.Track;
import com.jamendo.renhaojie.hear.webservices.AlbumService;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Ren Haojie on 2017/2/4.
 */

public class TrackInteractor implements ITrackInteractor {

    private AlbumService mAlbumService;

    @Inject
    public TrackInteractor(AlbumService albumService) {
        mAlbumService = albumService;
    }

    @Override
    public void loadTrackInfo(String trackId, final OnTrackInfoLoadedListener mListener) {
        Observable<Response<Track>> call = mAlbumService.loadTrackInfo(trackId);
        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<Track>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Response<Track> trackResponse) {
                        Headers headers = trackResponse.getHeaders();
                        if (headers != null && headers.getCode() == 0) {
                            if (trackResponse.getResults().size() > 0) {
                                Track track = trackResponse.getResults().get(0);
                                mListener.onTrackInfoLoaded(track);
                            }
                        }
                    }
                });
    }
}
