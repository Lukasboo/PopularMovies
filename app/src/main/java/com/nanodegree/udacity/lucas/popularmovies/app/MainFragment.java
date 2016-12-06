package com.nanodegree.udacity.lucas.popularmovies.app;

import android.app.Activity;
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
import android.widget.ImageButton;
import android.widget.TextView;

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
    Movie movieDetail;
    FetchMoviesTask fetchMoviesTask;
    SharedPreferences prefs;
    GridView lvMovieList;
    MovieListAdapter listAdapter;
    ArrayList<Movie> moviesArray;
    public ArrayList<Movie> movieList;
    DrawerLayout mDrawer;
    Toolbar toolbar;
    NavigationView nvDrawer;
    View rootView;
    private ActionBarDrawerToggle mDrawerToggle;
    private int menuItemId;
    ImageButton ibtfavorite;
    TextView txtmark;
    MovieDataHelper movieDataHelper;
    String movieId;
    private boolean mTwoPane;
    private OnClickListener listener;

    public MainFragment() {}

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        try {

            if (isOnline()) {
                rootView = inflater.inflate(R.layout.fragment_main, container, false);
                mTwoPane = ((MainActivity)getActivity()).mTwoPane;
                toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
                ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
                mDrawer = (DrawerLayout) rootView.findViewById(R.id.drawer_layout);
                nvDrawer = (NavigationView) rootView.findViewById(R.id.nvView);
                ibtfavorite = (ImageButton) rootView.findViewById(R.id.ibtfavorite);
                ibtfavorite.setVisibility(View.INVISIBLE);
                txtmark = (TextView) rootView.findViewById(R.id.txtmark);
                txtmark.setVisibility(View.INVISIBLE);
                setupDrawerContent(nvDrawer);
                findViews(rootView);
                movieDataHelper = new MovieDataHelper(getActivity());

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
                //return rootView;
            } else {
                //createDialog();
                //return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rootView;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);

        if(!(activity instanceof OnClickListener)){
            throw new RuntimeException("A Activity deve implementar a interface MainFragment.OnClickListener");
        }
        listener = (OnClickListener) activity;
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
                mTwoPane = ((MainActivity)getActivity()).mTwoPane;
                if (mTwoPane) {

                    //movieId = movieList.get(position).getId();
                    movieDetail = movieList.get(position);
                    listener.onClick(movieDetail);

                } else {
                    Intent intent = new Intent(getActivity(), MovieDetail.class);
                    intentPutExtras(intent, position);
                    startActivity(intent);
                    getActivity().getFragmentManager().popBackStack();
                }
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
                    .setTitle(R.string.internet_message)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
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
        internetDialogFragment.show(getFragmentManager(), getString(R.string.conection_error));
    }


    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        try {
                            menuItemId = menuItem.getItemId();
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
                menuItemId = menuItem.getItemId();
                break;
            case R.id.nav_second_fragment:
                callSettings("top_rated");
                break;
            case R.id.nav_third_fragment:
                Intent intent = new Intent(getActivity(), MovieFavoriteActivity.class);
                startActivity(intent);
            default:
                break;
        }
        //toolbar.setTitle(getPreference());
        mDrawer.closeDrawers();
    }

    public interface OnClickListener{
        void onClick(Movie movieDetail);
    }

}
