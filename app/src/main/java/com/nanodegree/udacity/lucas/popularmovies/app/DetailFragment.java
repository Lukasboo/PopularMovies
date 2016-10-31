package com.nanodegree.udacity.lucas.popularmovies.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by lucas on 28/10/16.
 */

public class DetailFragment extends Fragment implements Serializable {

    Movie movie;
    ImageView movie_poster_path;
    TextView original_title;
    TextView overview;
    TextView vote_average;
    TextView release_data;
    String originalTitleStr;
    String overviewStr;
    String voteAverageStr;
    String releaseDateStr;
    MovieTrailer movieTrailer;
    ArrayList<MovieTrailer> trailerList;
    ListView list_trailer;

    public DetailFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        findViews(rootView);
        Intent intent = getActivity().getIntent();
        movie = intent.getExtras().getParcelable("movie");
        //movieId = movie.getId();
        settingTexts();
        Picasso.with(getActivity()).load(Movie.TAG_URL_POSTER_PATH + movie.getPoster_path())
                .into(movie_poster_path);

        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask(2);
        //fetchMoviesTask.execute(movie.getId()); //it work

        try {
            trailerList = getMoviesDataFromJson(fetchMoviesTask.execute(movie.getId()).get());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        list_trailer = (ListView) rootView.findViewById(R.id.list_trailer);
        TrailerListAdapter trailerListAdapter = new TrailerListAdapter(getContext(), trailerList, R.layout.trailers_entry);
        list_trailer.setAdapter(trailerListAdapter);
        list_trailer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                //intent.setData(movieTrailer.getKey().toString());
                intent.setPackage("com.google.android.youtube");
                String teste = MovieTrailer.TAG_YOUTUBE_BASE_URL + trailerList.get(position).getKey();
                intent.setData(Uri.parse(MovieTrailer.TAG_YOUTUBE_BASE_URL + trailerList.get(position).getKey()));
                try {
                    //if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(intent);
                    //}
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        return rootView;
    }

    private void findViews(View rootView){
        movie_poster_path = (ImageView)rootView.findViewById(R.id.movie_poster_path);
        original_title = (TextView)rootView.findViewById(R.id.original_title);
        overview = (TextView)rootView.findViewById(R.id.overview);
        vote_average = (TextView)rootView.findViewById(R.id.vote_average);
        release_data = (TextView)rootView.findViewById(R.id.release_date);
    }

    private void settingTexts (){
        concatMovieString();
        original_title.setText(originalTitleStr);
        overview.setText(overviewStr);
        vote_average.setText(voteAverageStr);
        release_data.setText(releaseDateStr);
    }

    private void concatMovieString(){
        originalTitleStr = getString(R.string.original_name) + movie.getOriginal_title();
        overviewStr = getString(R.string.overview) + movie.getOverview();
        voteAverageStr = getString(R.string.vote_average) + movie.getVote_average();
        releaseDateStr = getString(R.string.release_date) + movie.getRelease_date();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private ArrayList<MovieTrailer>  getMoviesDataFromJson(String moviesJsonStr)
            throws JSONException {

        if (moviesJsonStr != null) {
            try {
                JSONObject jsonObject = new JSONObject(moviesJsonStr);
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                trailerList = new ArrayList<>();
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
        return trailerList;
    }

    private void setMovieData(JSONObject jsonObject) throws ParseException {
        try {
            movieTrailer = new MovieTrailer();
            movieTrailer.setId(jsonObject.getString(MovieTrailer.TAG_ID));
            movieTrailer.setIso_639_1(jsonObject.getString(MovieTrailer.TAG_ISO_639_1));
            movieTrailer.setIso_3166_1(jsonObject.getString(MovieTrailer.TAG_ISO_3166_1));
            movieTrailer.setKey(jsonObject.getString(MovieTrailer.TAG_KEY));
            movieTrailer.setName(jsonObject.getString(MovieTrailer.TAG_NAME));
            movieTrailer.setSite(jsonObject.getString(MovieTrailer.TAG_SITE));
            movieTrailer.setSize(jsonObject.getString(MovieTrailer.TAG_SIZE));
            movieTrailer.setType(jsonObject.getString(MovieTrailer.TAG_TYPE));
            trailerList.add(movieTrailer);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}