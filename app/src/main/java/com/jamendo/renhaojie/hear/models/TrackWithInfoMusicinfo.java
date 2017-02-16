package com.jamendo.renhaojie.hear.models;

public class TrackWithInfoMusicinfo {
    private TrackWithInfoMusicinfoDescription description;
    private String[] tags;

    public TrackWithInfoMusicinfoDescription getDescription() {
        return this.description;
    }

    public void setDescription(TrackWithInfoMusicinfoDescription description) {
        this.description = description;
    }

    public String[] getTags() {
        return this.tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }
}
