package com.nanodegree.udacity.lucas.popularmovies.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
        ibtfavorite = (ImageButton) rootView.findViewById(R.id.ibtfavorite);
        ibtfavorite.setVisibility(View.INVISIBLE);

        txtmark = (TextView) rootView.findViewById(R.id.txtmark);
        txtmark.setVisibility(View.INVISIBLE);

        setupDrawerContent(nvDrawer);

        movieDataHelper = new MovieDataHelper(getActivity());
        try {
            favoriteMoviesList = movieDataHelper.getFavoriteMovies();
            String teste = "";
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("TESTE", "ERROo" + e.toString());
        }
        setListAdapter();
        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
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
                            //nvDrawer.getMenu().findItem(menuItemId).setChecked(true);
                            //menuItemId = menuItem.getItemId();
                            //nvDrawer.setCheckedItem(menuItem.getItemId());
                            //nvDrawer.getMenu().findItem(menuItem.getItemId()).setChecked(true);
                            //menuItem.setChecked(true);
                            //menuItem.setCheckable(true);
                            //menuItem.setChecked(true);
                            //mNavItemId = menuItem.getItemId();
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
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass = null;
        switch(menuItem.getItemId()) {
            case R.id.nav_first_fragment:
                callSettings("popular");
                /*mainIntent = new Intent(getActivity(), MainActivity.class);
                startActivity(mainIntent);*/
                //menuItemId = menuItem.getItemId();
                //menuItem = nvDrawer.getMenu().findItem(R.id.nav_first_fragment);
                //menuItem.setCheckable(true);
                //menuItem.setChecked(true);
                //nvDrawer.setCheckedItem(menuItem.getItemId());
                //updateMovies();
                //fragmentClass = MainFragment.this;
                break;
            case R.id.nav_second_fragment:
                callSettings("top_rated");
                /*mainIntent = new Intent(getActivity(), MainActivity.class);
                startActivity(mainIntent);*/
                //menuItem = nvDrawer.getMenu().findItem(R.id.nav_second_fragment);
                //menuItem.setCheckable(true);
                //menuItem.setChecked(true);
                //nvDrawer.setCheckedItem(menuItem.getItemId());
                //updateMovies();
                //fragmentClass = DetailFragment.class;
                break;
            case R.id.nav_third_fragment:
                //Toast.makeText(getActivity(), "teste", Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(getActivity(), MovieFavoriteActivity.class);
                //startActivity(intent);
                //fragmentClass = ThirdFragment.class;
                //Intent intent = new Intent(this, )
                //break;
            default:
                //fragmentClass = FirstFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        //FragmentManager fragmentManager = getSupportFragmentManager();
        //.beginTransaction().replace(R.id.flContent, fragment).commit();


        /*// Lookup navigation view
        NavigationView navigationView = (NavigationView) rootView.findViewById(R.id.nav_draw);
        // Inflate the header view at runtime
        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header);
        // We can now look up items within the header if needed
        ImageView ivHeaderPhoto = headerLayout.findViewById(R.id.imageView);*/

        // Highlight the selected item has been done by NavigationView
        //menuItem.setChecked(true);
        // Set action bar title
        //toolbar.setTitle(menuItem.getTitle());
        toolbar.setTitle(getPreference());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }

    private String getPreference() {
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return prefs.getString("sort", "popular");
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
