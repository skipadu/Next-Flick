package com.pihrit.nextflick.model;

import com.google.gson.annotations.SerializedName;

public class TmdbJsonReviewsResponse {
    private long id;
    private long page;
    private Review[] results;
    @SerializedName("total_pages")
    private long totalPages;
    @SerializedName("total_results")
    private long totalResults;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    public Review[] getResults() {
        return results;
    }

    public void setResults(Review[] results) {
        this.results = results;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(long totalResults) {
        this.totalResults = totalResults;
    }
}
