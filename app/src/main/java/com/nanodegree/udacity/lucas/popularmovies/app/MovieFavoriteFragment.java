package com.nanodegree.udacity.lucas.popularmovies.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

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
    ArrayList<Movie> favoriteMoviesList;
    MovieListAdapter listAdapter;
    TextView txtmark;
    ImageButton ibtfavorite;
    SharedPreferences prefs;
    Intent mainIntent;

    public MovieFavoriteFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_movie_favorite, container, false);

        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        mDrawer = (DrawerLayout) rootView.findViewById(R.id.drawer_layout);
        nvDrawer = (NavigationView) rootView.findViewById(R.id.nvView);
        favoriteMoviesGrid = (GridView) rootView.findViewById(R.id.favoriteMoviesGrid);
        ibtfavorite = (ImageButton) rootView.findViewById(R.id.ibtfavorite);
        ibtfavorite.setVisibility(View.INVISIBLE);
        txtmark = (TextView) rootView.findViewById(R.id.txtmark);
        txtmark.setVisibility(View.INVISIBLE);
        setupDrawerContent(nvDrawer);
        movieDataHelper = new MovieDataHelper(getActivity());
        try {
            favoriteMoviesList = movieDataHelper.getFavoriteMovies();
        } catch (Exception e) {
            e.printStackTrace();
            //Log.d("TESTE", "ERROo" + e.toString());
        }
        setListAdapter();
        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        try {
                            selectDrawerItem(menuItem);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return true;
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void selectDrawerItem(MenuItem menuItem) throws InterruptedException, ExecutionException, JSONException {

        switch(menuItem.getItemId()) {
            case R.id.nav_first_fragment:
                callSettings("popular");
                break;
            case R.id.nav_second_fragment:
                callSettings("top_rated");
                break;
            case R.id.nav_third_fragment:

            default:
                break;
        }

        //toolbar.setTitle(getPreference());
        mDrawer.closeDrawers();
    }

    private void callSettings(String preference){
        Intent intent = new Intent(getActivity(), SettingsActivity.class);
        intent.putExtra("pref", preference);
        startActivity(intent);
    }

    private void setListAdapter(){
        listAdapter = new MovieListAdapter(getContext(), favoriteMoviesList, R.layout.movies_entry);
        favoriteMoviesGrid.setAdapter(listAdapter);
    }

}
