package com.pihrit.nextflick.model;

public class TmdbJsonResponse {
    private long page;
    private long totalResults;
    private Movie[] results;

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    public long getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(long totalResults) {
        this.totalResults = totalResults;
    }

    public Movie[] getResults() {
        return results;
    }

    public void setResults(Movie[] results) {
        this.results = results;
    }
}
