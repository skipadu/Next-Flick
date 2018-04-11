package com.pihrit.nextflick.model;

public class TmdbJsonVideosResponse {
    private long id;
    private TrailerVideo[] results;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public TrailerVideo[] getResults() {
        return results;
    }

    public void setResults(TrailerVideo[] results) {
        this.results = results;
    }
}
