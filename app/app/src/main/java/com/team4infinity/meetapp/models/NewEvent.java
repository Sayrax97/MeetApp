package com.team4infinity.meetapp.models;

public class NewEvent {
    String key;
    String title;
    Double lat;
    Double lon;
    String creator_id;

    public NewEvent(String key, String title, Double lat, Double lon, String creator_id) {
        this.key = key;
        this.title = title;
        this.lat = lat;
        this.lon = lon;
        this.creator_id = creator_id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(String creator_id) {
        this.creator_id = creator_id;
    }
}
