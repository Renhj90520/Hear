package com.jamendo.renhaojie.hear.models;

public class Musicinfo {
    private String gender;
    private String acousticelectric;
    private String lang;
    private String vocalinstrumental;
    private String speed;
    private MusicinfoTags tags;

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAcousticelectric() {
        return this.acousticelectric;
    }

    public void setAcousticelectric(String acousticelectric) {
        this.acousticelectric = acousticelectric;
    }

    public String getLang() {
        return this.lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getVocalinstrumental() {
        return this.vocalinstrumental;
    }

    public void setVocalinstrumental(String vocalinstrumental) {
        this.vocalinstrumental = vocalinstrumental;
    }

    public String getSpeed() {
        return this.speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public MusicinfoTags getTags() {
        return this.tags;
    }

    public void setTags(MusicinfoTags tags) {
        this.tags = tags;
    }
}
