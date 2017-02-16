package com.jamendo.renhaojie.hear.activities.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jamendo.renhaojie.hear.HearApplication;
import com.jamendo.renhaojie.hear.R;
import com.jamendo.renhaojie.hear.activities.tracks.TracksActivity;
import com.jamendo.renhaojie.hear.adapters.AlbumListAdapter;

import java.io.ByteArrayOutputStream;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MianContract.IMainView {

    @BindView(R.id.album_list)
    RecyclerView album_list;
    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    @Inject
    MianContract.IMainPresenter mPresenter;

    private int lastVisibleIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        album_list.setLayoutManager(new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false));

        DaggerMainComponent.builder()
                .albumServiceComponent(((HearApplication) getApplication()).getAlbumServiceComponent())
                .mainModule(new MainModule(this))
                .build()
                .inject(this);
//        mPresenter = new MainPresenter(this);
        mPresenter.loadAlbums(lastVisibleIndex);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.refreshAlbums();
            }
        });
        album_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                RecyclerView.LayoutManager layoutManager = album_list.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    lastVisibleIndex = ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
                }
                if (layoutManager instanceof GridLayoutManager) {
                    lastVisibleIndex = ((GridLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
                }
                if (layoutManager instanceof StaggeredGridLayoutManager) {
                    int[] lastList = null;
                    StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) layoutManager;
                    lastList = new int[manager.getSpanCount()];
                    int[] lastVisibleItemPositions = manager.findLastCompletelyVisibleItemPositions(lastList);

                    for (int i : lastList) {
                        lastVisibleIndex = i > lastVisibleIndex ? i : lastVisibleIndex;
                    }
                }

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mPresenter.loadAlbums(lastVisibleIndex);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void showProgress() {
        swipeRefresh.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        swipeRefresh.setRefreshing(false);
    }

    @Override
    public void setItems(AlbumListAdapter adapter) {
        album_list.setAdapter(adapter);
    }

    @Override
    public void navigateToTrack(String albumId, String albumName, ImageView sharedImage) {
        Intent intent = new Intent(this, TracksActivity.class);
        intent.putExtra(TracksActivity.EXTRA_ALBUM_ID, albumId);
        intent.putExtra(TracksActivity.EXTRA_ALBUM_NAME, albumName);
        Bitmap bm = ((BitmapDrawable) sharedImage.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
        intent.putExtra(TracksActivity.EXTRA_ALBUM_IMAGE, stream.toByteArray());
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, sharedImage, "albumArt");
        startActivity(intent, options.toBundle());
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

}
