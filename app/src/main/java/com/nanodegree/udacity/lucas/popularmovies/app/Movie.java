package com.nanodegree.udacity.lucas.popularmovies.app;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mobile on 07/10/2016.
 */

public class Movie implements Parcelable {

    public String poster_path;
    public boolean adult;
    public String overview;
    public String release_date;
    public String genre_ids;
    public String id;
    public String original_title;
    public String original_language;
    public String title;
    public String backdrop_path;
    public String popularity;
    public String vote_count;
    public boolean video;
    public String vote_average;

    public static final String TAG_POSTER_PATH = "poster_path";
    public static final String TAG_ADULT = "adult";
    public static final String TAG_OVERVIEW = "overview";
    public static final String TAG_RELEASE_DATA = "release_date";
    public static final String TAG_GENRE_IDS = "genre_ids";
    public static final String TAG_ID = "id";
    public static final String TAG_ORIGINAL_TITLE = "original_title";
    public static final String TAG_ORIGINAL_LANGUAGE = "original_language";
    public static final String TAG_TITLE = "title";
    public static final String TAG_BACKDROP_PATH = "backdrop_path";
    public static final String TAG_POPULARITY = "popularity";
    public static final String TAG_VOTE_COUNT = "vote_count";
    public static final String TAG_VIDEO = "video";
    public static final String TAG_VOTE_AVERAGE = "vote_average";
    //public static final String TAG_KEY_API = "API KEY AQUI";
    public static final String TAG_KEY_API = BuildConfig.MOVIE_DB_API_KEY;
    public static final String TAG_URL_POSTER_PATH = "https://image.tmdb.org/t/p/w500";
    public static final String TAG_URL_API = "api.themoviedb.org";
    public static final int TAG_URI_TYPE_MAIN = 1;
    public static final int TAG_URI_TYPE_DETAIL = 2;
    public static final String TAG_YOUTUBE_BASE_URL = "www.youtube.com";
    public static final String TAG_KEY = "key";

    public Movie(){}

    protected Movie(Parcel in) {
        poster_path = in.readString();
        adult = in.readByte() != 0;
        overview = in.readString();
        release_date = in.readString();
        genre_ids = in.readString();
        id = in.readString();
        original_title = in.readString();
        original_language = in.readString();
        title = in.readString();
        backdrop_path = in.readString();
        popularity = in.readString();
        vote_count = in.readString();
        video = in.readByte() != 0;
        vote_average = in.readString();
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

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getGenre_ids() {
        return genre_ids;
    }

    public void setGenre_ids(String genre_ids) {
        this.genre_ids = genre_ids;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getVote_count() {
        return vote_count;
    }

    public void setVote_count(String vote_count) {
        this.vote_count = vote_count;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(poster_path);
        parcel.writeByte((byte) (adult ? 1 : 0));
        parcel.writeString(overview);
        parcel.writeString(release_date);
        parcel.writeString(genre_ids);
        parcel.writeString(id);
        parcel.writeString(original_title);
        parcel.writeString(original_language);
        parcel.writeString(title);
        parcel.writeString(backdrop_path);
        parcel.writeString(popularity);
        parcel.writeString(vote_count);
        parcel.writeByte((byte) (video ? 1 : 0));
        parcel.writeString(vote_average);
    }
}
