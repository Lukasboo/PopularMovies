package com.nanodegree.udacity.lucas.popularmovies.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.Serializable;

public class MovieDetail extends AppCompatActivity implements Serializable {

    public MovieDetail(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }
    }
}
