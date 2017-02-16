package com.jamendo.renhaojie.hear.activities.main;

import com.jamendo.renhaojie.hear.models.Album;
import com.jamendo.renhaojie.hear.models.Response;
import com.jamendo.renhaojie.hear.webservices.AlbumService;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Ren Haojie on 2017/1/12.
 */

public class AlbumInteractor implements IAlbumInteractor {

    @Inject
    public AlbumInteractor(AlbumService service) {
        mService = service;
    }

    AlbumService mService;

    @Override
    public void loadAlbums(int offset, final OnLoadFinishedListener mListener) {
//        AlbumService service = new AlbumService();
        Observable<Response<Album>> albumResponse = mService.getAlbumsAsync(20, offset);
        albumResponse.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<Album>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mListener.onLoadError();
                    }

                    @Override
                    public void onNext(Response<Album> result) {
                        if (result.getHeaders() != null && result.getHeaders().getCode() == 0) {
                            List<Album> albumList = result.getResults();
                            mListener.onLoadFinished(albumList);
                        } else {
//                Toast.makeText(MainActivity.this, "header's code is " + result.getHeaders().getCode(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

//    private Callback<Response<Album>> mAlbumCallback = new Callback<Response<Album>>() {
//        @Override
//        public void onResponse(Call<Response<Album>> call, retrofit2.Response<Response<Album>> response) {
//            Response<Album> result = response.body();
//            if (result.getHeaders() != null && result.getHeaders().getCode() == 0) {
//                List<Album> albumList = result.getResults();
//                mListener.onLoadFinished(albumList);
//            } else {
////                Toast.makeText(MainActivity.this, "header's code is " + result.getHeaders().getCode(), Toast.LENGTH_LONG).show();
//            }
//        }
//
//        @Override
//        public void onFailure(Call<Response<Album>> call, Throwable t) {
//            t.printStackTrace();
//        }
//    };
}
