package com.nanodegree.udacity.lucas.popularmovies.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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
 * Created by lucas on 28/10/16.
 */

public class DetailFragment extends Fragment implements Serializable {

    static final String DETAIL_URI = "URI";
    Movie movie;
    MovieDataHelper movieDataHelper;
    MovieReview movieReview;
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
    ArrayList<Movie> favoriteIdsList;
    ArrayList<MovieReview> reviewList;
    ArrayList<String> reviewAuthorList;
    ListView list_trailer;
    ListView list_review;
    ImageButton ibtfavorite;
    Toolbar toolbar;
    int movieId;
    boolean mTwoPane;
    public MainFragment mainFragment;
    ArrayList<Movie> moviesArray;
    private FetchMoviesTask mainMoviesTask;
    private SharedPreferences prefs;
    private ArrayList<Movie> movieList;

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
        mTwoPane = ((MainActivity)getActivity()).mTwoPane;

        movieDataHelper = new MovieDataHelper(getActivity());

        if (mTwoPane==false) {

            Intent intent = getActivity().getIntent();
            movie = intent.getExtras().getParcelable("movie");

        } else {
            moviesArray = new ArrayList();
            mainMoviesTask = new FetchMoviesTask(Movie.TAG_URI_TYPE_MAIN);
            prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            try {
                String teste = mainMoviesTask.execute(getPreference()).get();
                moviesArray = getMoviesDataFromJson(teste);
                movie = moviesArray.get(0);
                /*moviesArray = getMoviesDataFromJson(mainMoviesTask.execute(getPreference()).get());
                movie = moviesArray.get(0);*/
                //String teste = "";
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        movieId = Integer.parseInt(movie.getId());
        settingTexts();
        Picasso.with(getActivity()).load(Movie.TAG_URL_POSTER_PATH + movie.getPoster_path())
                .into(movie_poster_path);
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ibtfavorite.setVisibility(View.VISIBLE);
        if (isFavorite()) {
            ibtfavorite.setImageResource(android.R.drawable.btn_star_big_on);
        } else {
            ibtfavorite.setImageResource(android.R.drawable.btn_star_big_off);
        }
        ibtfavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFavorite()) {
                    movieDataHelper.deleteMovie(movieId);
                    ibtfavorite.setImageResource(android.R.drawable.btn_star_big_off);
                    Toast.makeText(getActivity(), R.string.delete_message, Toast.LENGTH_LONG).show();
                } else {
                    movieDataHelper.insertMovie(movie);
                    ibtfavorite.setImageResource(android.R.drawable.btn_star_big_on);
                    Toast.makeText(getActivity(), R.string.insert_message, Toast.LENGTH_LONG).show();
                }
            }
        });

        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask(2);
        FetchMoviesTask fetchReviewMoviesTask = new FetchMoviesTask(3);

        try {
            trailerList = getMoviesTrailerDataFromJson(fetchMoviesTask.execute(movie.getId()).get());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        try {
            //reviewList = getMovieReviewDataFromJson(fetchReviewMoviesTask.execute(movie.getId()).get());
            reviewList = getMovieReviewDataFromJson(fetchReviewMoviesTask.execute(String.valueOf(movieId)).get());
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
                intent.setPackage(getString(R.string.youtube_package_name));
                intent.setData(Uri.parse(MovieTrailer.TAG_YOUTUBE_BASE_URL + trailerList.get(position).getKey()));
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        reviewAuthorList = new ArrayList<>();
        for (int i = 0; i < reviewList.size(); i++) {
            reviewAuthorList.add(reviewList.get(i).getAuthor());
        }

        list_review = (ListView) rootView.findViewById(R.id.list_review);
        ArrayAdapter reviewArrayAdapter = new ArrayAdapter(getActivity(), R.layout.review_entry, R.id.txtauthor, reviewAuthorList);
        list_review.setAdapter(reviewArrayAdapter);
        setItemClickListener();

        return rootView;
    }

    private void findViews(View rootView){
        movie_poster_path = (ImageView)rootView.findViewById(R.id.movie_poster_path);
        original_title = (TextView)rootView.findViewById(R.id.original_title);
        overview = (TextView)rootView.findViewById(R.id.overview);
        vote_average = (TextView)rootView.findViewById(R.id.vote_average);
        release_data = (TextView)rootView.findViewById(R.id.release_date);
        ibtfavorite = (ImageButton) rootView.findViewById(R.id.ibtfavorite);
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
    private ArrayList<MovieTrailer> getMoviesTrailerDataFromJson(String moviesJsonStr)
            throws JSONException {

        if (moviesJsonStr != null) {
            try {
                JSONObject jsonObject = new JSONObject(moviesJsonStr);
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                trailerList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject moviesJson= jsonArray.getJSONObject(i);
                    setMainMovieData(moviesJson);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            //Log.d("GETMOVIESJSON", "Empty Data");
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private ArrayList<MovieReview> getMovieReviewDataFromJson(String moviesJsonStr)
            throws JSONException {

        if (moviesJsonStr != null) {
            try {
                JSONObject jsonObject = new JSONObject(moviesJsonStr);
                JSONArray jsonArray = jsonObject.getJSONArray(getString(R.string.results_str));
                reviewList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject moviesJson= jsonArray.getJSONObject(i);
                    setMovieReviewData(moviesJson);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            //Log.d("GETMOVIESJSON", "Empty Data");
        }
        return reviewList;
    }

    private void setMovieReviewData(JSONObject jsonObject) throws ParseException {
        try {
            movieReview = new MovieReview();
            movieReview.setId(jsonObject.getString(MovieReview.TAG_ID));
            movieReview.setAuthor(jsonObject.getString(MovieReview.TAG_AUTHOR));
            movieReview.setContent(jsonObject.getString(MovieReview.TAG_CONTENT));
            movieReview.setUrl(jsonObject.getString(MovieReview.TAG_URL));
            reviewList.add(movieReview);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean isFavorite() {
        favoriteIdsList = movieDataHelper.getFavoriteMoviesIds();
        for (int i=0;i<favoriteIdsList.size();i++) {
            if (movieId==Integer.parseInt(favoriteIdsList.get(i).getId())) {
                return true;
            }
        }
        return false;
    }

    private void setItemClickListener(){
        list_review.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),  MovieReviewActivity.class);
                intentPutExtras(intent, position);
                startActivity(intent);
                getActivity().getFragmentManager().popBackStack();
            }
        });
    }

    private void intentPutExtras(Intent intent, int position){
        intent.putExtra("moviereview", reviewList.get(position));
    }

    private String getPreference() {
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return prefs.getString("sort", "popular");
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
                    setMainMovieData(moviesJson);
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

    private void setMainMovieData(JSONObject jsonObject) throws ParseException {
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

    private static String dateFormat(String dateToFormat) throws ParseException {
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
        Date date = inputFormat.parse(dateToFormat);
        return outputFormat.format(date);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void clickcolor(Movie movieDetail){
        //movieId = Integer.parseInt(movie.getId());
        movieId = Integer.parseInt(movieDetail.getId());
        settingTexts();
        Picasso.with(getActivity()).load(Movie.TAG_URL_POSTER_PATH + movie.getPoster_path())
                .into(movie_poster_path);

        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask(2);
        FetchMoviesTask fetchReviewMoviesTask = new FetchMoviesTask(3);

        try {
            trailerList = getMoviesTrailerDataFromJson(fetchMoviesTask.execute(movie.getId()).get());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        try {
            //reviewList = getMovieReviewDataFromJson(fetchReviewMoviesTask.execute(movie.getId()).get());
            reviewList = getMovieReviewDataFromJson(fetchReviewMoviesTask.execute(String.valueOf(movieId)).get());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        TrailerListAdapter trailerListAdapter = new TrailerListAdapter(getContext(), trailerList, R.layout.trailers_entry);
        list_trailer.setAdapter(trailerListAdapter);
        list_trailer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setPackage(getString(R.string.youtube_package_name));
                intent.setData(Uri.parse(MovieTrailer.TAG_YOUTUBE_BASE_URL + trailerList.get(position).getKey()));
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        reviewAuthorList = new ArrayList<>();
        for (int i = 0; i < reviewList.size(); i++) {
            reviewAuthorList.add(reviewList.get(i).getAuthor());
        }

        ArrayAdapter reviewArrayAdapter = new ArrayAdapter(getActivity(), R.layout.review_entry, R.id.txtauthor, reviewAuthorList);
        list_review.setAdapter(reviewArrayAdapter);
    }

}