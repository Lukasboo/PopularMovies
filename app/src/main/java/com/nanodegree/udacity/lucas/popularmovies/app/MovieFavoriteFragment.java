package com.nanodegree.udacity.lucas.popularmovies.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;

/**
 * Created by lucas on 15/11/16.
 */

public class MovieFavoriteFragment extends Fragment {

    View rootView;
    DrawerLayout mDrawer;
    Toolbar toolbar;
    NavigationView nvDrawer;
    GridView favoriteMoviesGrid;
    MovieDataHelper movieDataHelper;

    public MovieFavoriteFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_movie_favorite, container, false);

        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

        // Find our drawer view
        mDrawer = (DrawerLayout) rootView.findViewById(R.id.drawer_layout);

        // Find our drawer view
        nvDrawer = (NavigationView) rootView.findViewById(R.id.nvView);
        // Setup drawer view

        favoriteMoviesGrid = (GridView) rootView.findViewById(R.id.favoriteMoviesGrid);
        movieDataHelper = new MovieDataHelper(getActivity());
        try {
            ArrayList<Movie> favoriteMoviesList = movieDataHelper.getFavoriteMovies();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("TESTE", "ERROo" + e.toString());
        }

        return rootView;
    }
}
