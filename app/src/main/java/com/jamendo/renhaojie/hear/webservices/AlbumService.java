package com.jamendo.renhaojie.hear.webservices;

import com.jamendo.renhaojie.hear.models.Album;
import com.jamendo.renhaojie.hear.models.Response;
import com.jamendo.renhaojie.hear.models.Track;
import com.jamendo.renhaojie.hear.models.TracksUnderAlbum;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Ren Haojie on 2017/1/6.
 */

//public class AlbumService {
//    private static final String API_URL = "http://api.jamendo.com/";
//    private static final String CLIENT_ID = "56d30c95";

public interface AlbumService {

    @GET("/v3.0/albums/?client_id=" + Constants.CLIENT_ID + "&format=json")
    public Observable<Response<Album>> getAlbumsAsync(@Query("limit") int limit, @Query("offset") int offset);

    @GET("/v3.0/albums/tracks/?client_id=" + Constants.CLIENT_ID)
    public Observable<Response<TracksUnderAlbum>> loadTracksUnderAlbum(@Query("id") String id);

    @GET("/v3.0/tracks/?client_id=" + Constants.CLIENT_ID)
    public Observable<Response<Track>> loadTrackInfo(@Query("id") String id);


}

//    private AlbumService mAlbumService;
//
//    public AlbumService getAlbumService() {
//        if (mAlbumService == null) {
//            Retrofit retrofit = new Retrofit.Builder().baseUrl(API_URL)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                    .build();
//            mAlbumService = retrofit.create(AlbumService.class);
//        }
//        return mAlbumService;
//    }
//}
