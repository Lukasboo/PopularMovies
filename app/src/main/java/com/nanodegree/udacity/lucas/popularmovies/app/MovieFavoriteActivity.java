package com.nanodegree.udacity.lucas.popularmovies.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MovieFavoriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_favorite);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.flContent, new MovieFavoriteFragment())
                    .commit();
        }
    }
}
