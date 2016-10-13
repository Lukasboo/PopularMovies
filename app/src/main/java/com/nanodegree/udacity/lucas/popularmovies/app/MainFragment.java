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
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
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
    ListAdapter listAdapter;
    ArrayList<Movie> moviesArray;
    public ArrayList<Movie> movieList;

    public MainFragment() {}

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (isOnline()) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
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
        ArrayList<Movie> moviesArraySaved = new ArrayList<>();
        moviesArraySaved = moviesArray;
        outState.putInt("scrollPosition", index);
        outState.putSerializable("savedList", moviesArraySaved);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState!=null) {
            Log.d("INDEX", String.valueOf(savedInstanceState));
            lvMovieList.setSelection(savedInstanceState.getInt("scrollPosition"));
            movieList = (ArrayList<Movie>) savedInstanceState.getSerializable("savedList");
            setListAdapter();
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
        //scrollOverview = (ScrollView) rootView.findViewById(R.id.scrollOverview);
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
            movie.setPoster_path(jsonObject.getString(movie.TAG_POSTER_PATH));
            movie.setAdult(jsonObject.getBoolean(movie.TAG_ADULT));
            movie.setOverview(jsonObject.getString(movie.TAG_OVERVIEW));
            movie.setRelease_date(dateFormat(jsonObject.getString(movie.TAG_RELEASE_DATA)));
            movie.setGenre_ids(jsonObject.getString(movie.TAG_GENRE_IDS));
            movie.setId(jsonObject.getString(movie.TAG_ID));
            movie.setOriginal_title(jsonObject.getString(movie.TAG_ORIGINAL_TITLE));
            movie.setOriginal_language(jsonObject.getString(movie.TAG_ORIGINAL_LANGUAGE));
            movie.setTitle(jsonObject.getString(movie.TAG_TITLE));
            movie.setBackdrop_path(jsonObject.getString(movie.TAG_BACKDROP_PATH));
            movie.setPopularity(jsonObject.getString(movie.TAG_POPULARITY));
            movie.setVote_count(jsonObject.getString(movie.TAG_VOTE_COUNT));
            movie.setVideo(jsonObject.getBoolean(movie.TAG_VIDEO));
            movie.setVote_average(jsonObject.getString(movie.TAG_VOTE_AVERAGE));
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
            }
        });
    }

    private void intentPutExtras(Intent intent, int position){
        intent.putExtra(movie.TAG_POSTER_PATH, movieList.get(position).getPoster_path());
        intent.putExtra(movie.TAG_ORIGINAL_TITLE, movieList.get(position).getOriginal_title());
        intent.putExtra(movie.TAG_OVERVIEW, movieList.get(position).getOverview());
        intent.putExtra(movie.TAG_VOTE_AVERAGE, movieList.get(position).getVote_average());
        intent.putExtra(movie.TAG_RELEASE_DATA, movieList.get(position).getRelease_date());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                callSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void callSettings(){
        Intent intent = new Intent(getActivity(), SettingsActivity.class);
        startActivity(intent);
    }

    private static String dateFormat(String dataToFormat) throws ParseException {
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
        Date date = inputFormat.parse(dataToFormat);
        String dateFormated = outputFormat.format(date);
        return dateFormated;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void updateMovies() throws ExecutionException, InterruptedException, JSONException {
        if (isOnline()) {
            moviesArray = new ArrayList();
            fetchMoviesTask = new FetchMoviesTask();
            prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            moviesArray = getMoviesDataFromJson(fetchMoviesTask.execute(getPreference()).get());
            setListAdapter();
            /*listAdapter = new ListAdapter(getContext(), moviesArray, R.layout.movies_entry);
            lvMovieList.setAdapter(listAdapter);*/
        }
    }

    private void setListAdapter(){
        listAdapter = new ListAdapter(getContext(), moviesArray, R.layout.movies_entry);
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

}
