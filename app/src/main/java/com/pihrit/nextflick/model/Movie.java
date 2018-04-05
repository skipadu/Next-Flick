package com.pihrit.nextflick.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Movie implements Parcelable {
    public static final String PARCELABLE_ID = "movie";
    public static final String IMAGE_PATH_START = "http://image.tmdb.org/t/p/w185/";

    @SerializedName("id")
    private long id;

    @SerializedName("title")
    private String title;

    @SerializedName("original_title")
    private String originalTitle;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("original_language")
    private String originalLanguage;

    @SerializedName("vote_count")
    private long voteCount;

    @SerializedName("video")
    private boolean hasVideo;

    @SerializedName("popularity")
    private double popularity;

    @SerializedName("genre_ids")
    private int[] genreIds;

    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("adult")
    private boolean adultMaterial;

    @SerializedName("overview")
    private String synopsis;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("vote_average")
    private double voteAverage;

    public Movie(){
        //
    }

    protected Movie(Parcel in) {
        id = in.readLong();
        title = in.readString();
        originalTitle = in.readString();
        posterPath = in.readString();
        originalLanguage = in.readString();
        voteCount = in.readLong();
        hasVideo = in.readByte() != 0;
        popularity = in.readDouble();
        genreIds = in.createIntArray();
        backdropPath = in.readString();
        adultMaterial = in.readByte() != 0;
        synopsis = in.readString();
        releaseDate = in.readString();
        voteAverage = in.readDouble();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getFullPosterPath() {
        return Movie.IMAGE_PATH_START + getPosterPath();
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public long getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(long voteCount) {
        this.voteCount = voteCount;
    }

    public boolean isHasVideo() {
        return hasVideo;
    }

    public void setHasVideo(boolean hasVideo) {
        this.hasVideo = hasVideo;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public int[] getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(int[] genreIds) {
        this.genreIds = genreIds;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public boolean isAdultMaterial() {
        return adultMaterial;
    }

    public void setAdultMaterial(boolean adultMaterial) {
        this.adultMaterial = adultMaterial;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(title);
        parcel.writeString(originalTitle);
        parcel.writeString(posterPath);
        parcel.writeString(originalLanguage);
        parcel.writeLong(voteCount);
        parcel.writeByte((byte) (hasVideo ? 1 : 0));
        parcel.writeDouble(popularity);
        parcel.writeIntArray(genreIds);
        parcel.writeString(backdropPath);
        parcel.writeByte((byte) (adultMaterial ? 1 : 0));
        parcel.writeString(synopsis);
        parcel.writeString(releaseDate);
        parcel.writeDouble(voteAverage);
    }
}
