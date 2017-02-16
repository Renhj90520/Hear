package com.jamendo.renhaojie.hear.media;

/**
 * Created by Ren Haojie on 2017/1/9.
 */

public interface PlayerEngine {

    public void openPlaylist(PlayList playList);

    //开始播放
    public void play();

    //是否播放中
    public boolean isPlaying();

    //暂停
    public void pause();

    //停止
    public void stop();

    //快进
    public void forward();

    //快退
    public void rewind();

    public PlayList getPlaylist();
}
