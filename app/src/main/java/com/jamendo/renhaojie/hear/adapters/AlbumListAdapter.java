package com.jamendo.renhaojie.hear.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jamendo.renhaojie.hear.R;
import com.jamendo.renhaojie.hear.models.Album;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ren Haojie on 2017/1/7.
 */

public class AlbumListAdapter extends RecyclerView.Adapter<AlbumListAdapter.AlbumViewHolder> {

    private List<Album> mAlbumList;
    private Context mContext;
    private OnAlbumItemClickListener mListener;

    public void setOnAlbumClickListener(OnAlbumItemClickListener listerner) {
        this.mListener = listerner;
    }

    public AlbumListAdapter(Context context, List<Album> albumList) {
        mAlbumList = albumList;
        mContext = context;
    }

    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album, parent, false);
        return new AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AlbumViewHolder holder, int position) {
        Album album = mAlbumList.get(position);
        if (album != null) {
            holder.album_name.setText(album.getName());

            Glide.with(mContext)
                    .load(album.getImage())
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.image_album);
        }
    }

    @Override
    public int getItemCount() {
        return mAlbumList.size();
    }

    public interface OnAlbumItemClickListener {
        void onAlbumClick(int position, ImageView image_album);
    }

    public class AlbumViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.image_album)
        ImageView image_album;
        @BindView(R.id.album_name)
        TextView album_name;

        public AlbumViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onAlbumClick(getAdapterPosition(), image_album);
        }
    }
}
