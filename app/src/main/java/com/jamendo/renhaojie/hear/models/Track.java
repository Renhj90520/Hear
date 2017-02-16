package com.jamendo.renhaojie.hear.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Track implements Parcelable {
    private String license_ccurl;
    private String image;
    private String artist_name;
    private String shorturl;
    private String releasedate;
    private String audiodownload;
    private String artist_id;
    private int duration;
    private String artist_idstr;
    private String album_name;
    private String album_image;
    private String name;
    private String album_id;
    private String id;
    private int position;
    private String audio;
    private String prourl;
    private String shareurl;


    protected Track(Parcel in) {
        license_ccurl = in.readString();
        image = in.readString();
        artist_name = in.readString();
        shorturl = in.readString();
        releasedate = in.readString();
        audiodownload = in.readString();
        artist_id = in.readString();
        duration = in.readInt();
        artist_idstr = in.readString();
        album_name = in.readString();
        album_image = in.readString();
        name = in.readString();
        album_id = in.readString();
        id = in.readString();
        position = in.readInt();
        audio = in.readString();
        prourl = in.readString();
        shareurl = in.readString();
    }

    public static final Creator<Track> CREATOR = new Creator<Track>() {
        @Override
        public Track createFromParcel(Parcel in) {
            return new Track(in);
        }

        @Override
        public Track[] newArray(int size) {
            return new Track[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(license_ccurl);
        dest.writeString(image);
        dest.writeString(artist_name);
        dest.writeString(shorturl);
        dest.writeString(releasedate);
        dest.writeString(audiodownload);
        dest.writeString(artist_id);
        dest.writeInt(duration);
        dest.writeString(artist_idstr);
        dest.writeString(album_name);
        dest.writeString(album_image);
        dest.writeString(name);
        dest.writeString(album_id);
        dest.writeString(id);
        dest.writeInt(position);
        dest.writeString(audio);
        dest.writeString(prourl);
        dest.writeString(shareurl);
    }

    public String getFileName() {
        return String.format("%s-%s.mp3", album_name, name);
    }

    public String getLicense_ccurl() {
        return this.license_ccurl;
    }

    public void setLicense_ccurl(String license_ccurl) {
        this.license_ccurl = license_ccurl;
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

    public String getReleasedate() {
        return this.releasedate;
    }

    public void setReleasedate(String releasedate) {
        this.releasedate = releasedate;
    }

    public String getAudiodownload() {
        return this.audiodownload;
    }

    public void setAudiodownload(String audiodownload) {
        this.audiodownload = audiodownload;
    }

    public String getArtist_id() {
        return this.artist_id;
    }

    public void setArtist_id(String artist_id) {
        this.artist_id = artist_id;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getArtist_idstr() {
        return this.artist_idstr;
    }

    public void setArtist_idstr(String artist_idstr) {
        this.artist_idstr = artist_idstr;
    }

    public String getAlbum_name() {
        return this.album_name;
    }

    public void setAlbum_name(String album_name) {
        this.album_name = album_name;
    }

    public String getAlbum_image() {
        return this.album_image;
    }

    public void setAlbum_image(String album_image) {
        this.album_image = album_image;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlbum_id() {
        return this.album_id;
    }

    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getAudio() {
        return this.audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getProurl() {
        return this.prourl;
    }

    public void setProurl(String prourl) {
        this.prourl = prourl;
    }

    public String getShareurl() {
        return this.shareurl;
    }

    public void setShareurl(String shareurl) {
        this.shareurl = shareurl;
    }

    @Override
    public int describeContents() {
        return 0;
    }


}
