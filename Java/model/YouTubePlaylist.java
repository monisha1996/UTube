package com.smedic.tubtub.model;

import java.io.Serializable;


public class YouTubePlaylist implements Serializable {

    private String title;
    private String thumbnailURL;
    private String id;
    private long numberOfVideos;
    private String status;

    public YouTubePlaylist() {
        this.title = "";
        this.thumbnailURL = "";
        this.id = "";
        this.numberOfVideos = 0;
        this.status = "";
    }

    public YouTubePlaylist(String title, String thumbnailURL, String id, long numberOfVideos, String status) {
        this.title = title;
        this.thumbnailURL = thumbnailURL;
        this.id = id;
        this.numberOfVideos = numberOfVideos;
        this.status = status;
    }

    public long getNumberOfVideos() {
        return numberOfVideos;
    }

    public void setNumberOfVideos(long numberOfVideos) {
        this.numberOfVideos = numberOfVideos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnail) {
        this.thumbnailURL = thumbnail;
    }

    public String getStatus() {
        return status;
    }

    public void setPrivacy(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "YouTubePlaylist {" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' + ", number of videos=" + numberOfVideos +
                ", " + status +
                '}';
    }
}
