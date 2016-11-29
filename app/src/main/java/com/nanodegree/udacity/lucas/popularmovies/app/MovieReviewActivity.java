package com.nanodegree.udacity.lucas.popularmovies.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by lucas on 27/11/16.
 */

public class MovieReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_review);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.flContent, new MovieReviewFragment())
                    .commit();
        }
    }

}
