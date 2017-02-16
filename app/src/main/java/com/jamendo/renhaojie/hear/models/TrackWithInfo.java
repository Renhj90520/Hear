package com.jamendo.renhaojie.hear.models;

public class TrackWithInfo {
    private String zip;
    private String image;
    private String artist_name;
    private String shorturl;
    private String name;
    private TrackWithInfoMusicinfo musicinfo;
    private String id;
    private String releasedate;
    private String shareurl;
    private String artist_id;

    public String getZip() {
        return this.zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getArtist_name() {
        return this.artist_name;
    }

    public void setArtist_name(String artist_name) {
        this.artist_name = artist_name;
    }

    public String getShorturl() {
        return this.shorturl;
    }

    public void setShorturl(String shorturl) {
        this.shorturl = shorturl;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TrackWithInfoMusicinfo getMusicinfo() {
        return this.musicinfo;
    }

    public void setMusicinfo(TrackWithInfoMusicinfo musicinfo) {
        this.musicinfo = musicinfo;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReleasedate() {
        return this.releasedate;
    }

    public void setReleasedate(String releasedate) {
        this.releasedate = releasedate;
    }

    public String getShareurl() {
        return this.shareurl;
    }

    public void setShareurl(String shareurl) {
        this.shareurl = shareurl;
    }

    public String getArtist_id() {
        return this.artist_id;
    }

    public void setArtist_id(String artist_id) {
        this.artist_id = artist_id;
    }
}
