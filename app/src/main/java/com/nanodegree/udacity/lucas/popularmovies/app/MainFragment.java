package com.nanodegree.udacity.lucas.popularmovies.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by lucas on 06/10/16.
 */

public class MainFragment extends android.support.v4.app.Fragment {

    Movie movie;
    ListView lvMovieList;
    public ArrayList<Movie> movieList;
    SharedPreferences prefs;
    ArrayList<Movie> moviesArray;
    FetchMoviesTask fetchMoviesTask;

    public MainFragment() {
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        this.setHasOptionsMenu(true);
        findViews(rootView);
        moviesArray = new ArrayList();
        fetchMoviesTask = new FetchMoviesTask();
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        try {
            String teste = prefs.getString("sort_by", "testando");
            moviesArray = getMoviesDataFromJson(fetchMoviesTask.execute(prefs.getString("sort_by", "popularity.desc")).get());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        ListAdapter listAdapter = new ListAdapter(getContext(), moviesArray, R.layout.movies_entry, new String[] {"name_movie"}, new int[] {R.id.movie_name_entry});
        lvMovieList.setAdapter(listAdapter);
        setItemClickListener();
        return rootView;
    }

    private void findViews(View rootView) {
        lvMovieList = (ListView) rootView.findViewById(R.id.movieList);
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

    private void setMovieData(JSONObject jsonObject) {
        try {
            movie = new Movie();
            movie.setPoster_path(jsonObject.getString(movie.TAG_POSTER_PATH));
            movie.setAdult(jsonObject.getBoolean(movie.TAG_ADULT));
            movie.setOverview(jsonObject.getString(movie.TAG_OVERVIEW));
            movie.setRelease_date(jsonObject.getString(movie.TAG_RELEASE_DATA));
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

    private String getPreference() {
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return prefs.getString("sort_by", "popularity.desc");
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void Refresh(){

        try {
            moviesArray = getMoviesDataFromJson(fetchMoviesTask.execute(getPreference()).get());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}
