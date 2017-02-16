package com.jamendo.renhaojie.hear.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jamendo.renhaojie.hear.R;
import com.jamendo.renhaojie.hear.models.AlbumTrack;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ren Haojie on 2017/1/8.
 */

public class TrackListAdapter extends RecyclerView.Adapter<TrackListAdapter.TrackViewHolder> {

    private static int VIEW_TYPE_NORMAL = 0;
    private static int VIEW_TYPE_PLAYING = 1;

    public void setCurrentPlayIndex(int currentPlayIndex) {
        this.currentPlayIndex = currentPlayIndex;
    }

    private int currentPlayIndex = 0;

    private Context mContext;
    private AlbumTrack[] mTracks;
    private OnTrackClickListener mListener;

    public void setOnTrackClickListener(OnTrackClickListener listener) {
        mListener = listener;
    }

    public TrackListAdapter(Context context, AlbumTrack[] tracks) {
        mContext = context;
        mTracks = tracks;
    }

    @Override
    public TrackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == VIEW_TYPE_NORMAL) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_track, parent, false);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_track_playing, parent, false);
        }
        return new TrackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrackViewHolder holder, int position) {
        AlbumTrack track = mTracks[position];
        holder.track_name.setText(track.getName());
        if (holder.track_index != null) {
            holder.track_index.setText(track.getPosition());
        }
        holder.setTrackId(track.getId(), position);
    }

    @Override
    public int getItemCount() {
        return mTracks.length;
    }

    @Override
    public int getItemViewType(int position) {

        if (currentPlayIndex == position) {
            return VIEW_TYPE_PLAYING;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    public interface OnTrackClickListener {
        void onTrackClick(int position);
    }

    public class TrackViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Nullable
        @BindView(R.id.track_index)
        TextView track_index;
        @BindView(R.id.track_name)
        TextView track_name;
        @BindView(R.id.track_check)
        CheckBox track_track;

        private String mTrackId;
        private int mPosition;

        public void setTrackId(String trackId, int position) {
            mTrackId = trackId;
            mPosition = position;
        }

        public TrackViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onTrackClick(mPosition);
            }
        }
    }
}
