package com.nanodegree.udacity.lucas.popularmovies.app;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 * Created by lucas on 06/10/16.
 */

public class MainFragment extends android.support.v4.app.Fragment implements Serializable {

    Movie movie;
    FetchMoviesTask fetchMoviesTask;
    SharedPreferences prefs;
    GridView lvMovieList;
    MovieListAdapter listAdapter;
    ArrayList<Movie> moviesArray;
    public ArrayList<Movie> movieList;
    DrawerLayout mDrawer;
    Toolbar toolbar;
    NavigationView nvDrawer;
    ActionBarDrawerToggle drawerToggle;
    View rootView;

    public MainFragment() {}

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (isOnline()) {
            rootView = inflater.inflate(R.layout.fragment_main, container, false);

            // Set a Toolbar to replace the ActionBar.
            toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
            //setSupportActionBar(toolbar);
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

            // Find our drawer view
            mDrawer = (DrawerLayout) rootView.findViewById(R.id.drawer_layout);

            // Find our drawer view
            nvDrawer = (NavigationView) rootView.findViewById(R.id.nvView);
            // Setup drawer view
            setupDrawerContent(nvDrawer);

            this.setHasOptionsMenu(true);
            findViews(rootView);
            try {
                updateMovies();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            setItemClickListener();
            return rootView;
        } else {
            createDialog();
            return null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int index = lvMovieList.getFirstVisiblePosition();
        outState.putInt("scrollPosition", index);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState!=null) {
            Log.d("INDEX", String.valueOf(savedInstanceState));
            lvMovieList.setSelection(savedInstanceState.getInt("scrollPosition"));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onStart() {
        super.onStart();
        if (isOnline()) {
            try {
                updateMovies();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            createDialog();
        }
    }

    private void findViews(View rootView) {
        lvMovieList = (GridView) rootView.findViewById(R.id.movieList);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private ArrayList<Movie> getMoviesDataFromJson(String moviesJsonStr)
            throws JSONException {

        if (moviesJsonStr != null) {
            try {
                JSONObject jsonObject = new JSONObject(moviesJsonStr);
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                movieList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject moviesJson= jsonArray.getJSONObject(i);
                    setMovieData(moviesJson);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            Log.d("GETMOVIESJSON", "Empty Data");
        }
        return movieList;
    }

    private void setMovieData(JSONObject jsonObject) throws ParseException {
        try {
            movie = new Movie();
            movie.setPoster_path(jsonObject.getString(Movie.TAG_POSTER_PATH));
            movie.setAdult(jsonObject.getBoolean(Movie.TAG_ADULT));
            movie.setOverview(jsonObject.getString(Movie.TAG_OVERVIEW));
            movie.setRelease_date(dateFormat(jsonObject.getString(Movie.TAG_RELEASE_DATA)));
            movie.setGenre_ids(jsonObject.getString(Movie.TAG_GENRE_IDS));
            movie.setId(jsonObject.getString(Movie.TAG_ID));
            movie.setOriginal_title(jsonObject.getString(Movie.TAG_ORIGINAL_TITLE));
            movie.setOriginal_language(jsonObject.getString(Movie.TAG_ORIGINAL_LANGUAGE));
            movie.setTitle(jsonObject.getString(Movie.TAG_TITLE));
            movie.setBackdrop_path(jsonObject.getString(Movie.TAG_BACKDROP_PATH));
            movie.setPopularity(jsonObject.getString(Movie.TAG_POPULARITY));
            movie.setVote_count(jsonObject.getString(Movie.TAG_VOTE_COUNT));
            movie.setVideo(jsonObject.getBoolean(Movie.TAG_VIDEO));
            movie.setVote_average(jsonObject.getString(Movie.TAG_VOTE_AVERAGE));
            movieList.add(movie);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setItemClickListener(){
        lvMovieList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),  MovieDetail.class);
                intentPutExtras(intent, position);
                startActivity(intent);
                getActivity().getFragmentManager().popBackStack();
            }
        });
    }

    private void intentPutExtras(Intent intent, int position){
        intent.putExtra("movie", movieList.get(position));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                callSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

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

    private void callSettings(String preference){
        Intent intent = new Intent(getActivity(), SettingsActivity.class);
        intent.putExtra("pref", preference);
        startActivity(intent);
    }

    private static String dateFormat(String dateToFormat) throws ParseException {
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
        Date date = inputFormat.parse(dateToFormat);
        return outputFormat.format(date);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void updateMovies() throws ExecutionException, InterruptedException, JSONException {
        if (isOnline()) {
            moviesArray = new ArrayList();
            fetchMoviesTask = new FetchMoviesTask(Movie.TAG_URI_TYPE_MAIN);
            prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String teste = getPreference().toString();
            moviesArray = getMoviesDataFromJson(fetchMoviesTask.execute(getPreference()).get());
            setListAdapter();
        }
    }

    private void setListAdapter(){
        listAdapter = new MovieListAdapter(getContext(), moviesArray, R.layout.movies_entry);
        lvMovieList.setAdapter(listAdapter);
    }

    private String getPreference() {
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return prefs.getString("sort", "popular");
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static class InternetDialogFragment extends DialogFragment {

        public InternetDialogFragment(){}

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState)
        {
            return new AlertDialog.Builder(getActivity())
                    .setTitle("Sem conexão com a internet!\nVerifique suas configurações...")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getActivity().finish();
                        }
                    })
                    .create();
        }
    }

    private void createDialog(){
        InternetDialogFragment internetDialogFragment = new InternetDialogFragment();
        internetDialogFragment.show(getFragmentManager(), "Erro Conexão");
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
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass = null;
        switch(menuItem.getItemId()) {
            case R.id.nav_first_fragment:
                callSettings("popular");
                //updateMovies();
                //fragmentClass = MainFragment.this;
                break;
            case R.id.nav_second_fragment:
                callSettings("top_rated");
                //updateMovies();
                //fragmentClass = DetailFragment.class;
                break;
            //case R.id.nav_third_fragment:
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
        menuItem.setChecked(true);
        // Set action bar title
        toolbar.setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }




}
