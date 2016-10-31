package com.nanodegree.udacity.lucas.popularmovies.app;

/**
 * Created by lucas on 29/10/16.
 */

public class MovieTrailer {

    public String id;
    public String iso_639_1;
    public String iso_3166_1;
    public String key;
    public String name;
    public String site;
    public String size;
    public String type;

    public static final String TAG_ID = "id";
    public static final String TAG_ISO_639_1 = "iso_639_1";
    public static final String TAG_ISO_3166_1 = "iso_3166_1";
    public static final String TAG_KEY = "key";
    public static final String TAG_NAME = "name";
    public static final String TAG_SITE = "site";
    public static final String TAG_SIZE = "size";
    public static final String TAG_TYPE = "type";
    public static final String TAG_YOUTUBE_BASE_URL = "http://www.youtube.com/watch?v=";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIso_639_1() {
        return iso_639_1;
    }

    public void setIso_639_1(String iso_639_1) {
        this.iso_639_1 = iso_639_1;
    }

    public String getIso_3166_1() {
        return iso_3166_1;
    }

    public void setIso_3166_1(String iso_3166_1) {
        this.iso_3166_1 = iso_3166_1;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }







}
