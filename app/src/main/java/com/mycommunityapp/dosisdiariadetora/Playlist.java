package com.mycommunityapp.dosisdiariadetora;


import java.util.List;

public class Playlist {





    private String kind;
    private int id;
    private String created_at;
    private int track_count;
    private String title;
    private String artwork_url;

    private List<TracksBean> tracks;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getTrack_count() {
        return track_count;
    }

    public void setTrack_count(int track_count) {
        this.track_count = track_count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtwork_url() {
        return artwork_url;
    }

    public void setArtwork_url(String artwork_url) {
        this.artwork_url = artwork_url;
    }

    public List<TracksBean> getTracks() {
        return tracks;
    }

    public void setTracks(List<TracksBean> tracks) {
        this.tracks = tracks;
    }


    public static class TracksBean {


        private String kind;
        private int id;
        private String created_at;
        private String title;
        private String artwork_url;
        private String stream_url;

        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }


        public String getArtwork_url() {
            return artwork_url;
        }

        public void setArtwork_url(String artwork_url) {
            this.artwork_url = artwork_url;
        }

        public String getStream_url() {
            return stream_url;
        }

        public void setStream_url(String stream_url) {
            this.stream_url = stream_url;
        }

    }
}
