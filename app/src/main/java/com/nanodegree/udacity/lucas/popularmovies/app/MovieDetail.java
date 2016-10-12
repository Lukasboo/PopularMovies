package com.nanodegree.udacity.lucas.popularmovies.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MovieDetail extends AppCompatActivity {

    public MovieDetail(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    public static class PlaceholderFragment extends Fragment {

        Movie movie;
        ImageView movie_poster_path;
        TextView original_title;
        TextView overview;
        TextView vote_average;
        TextView release_data;
        public static final String URL_API = "https://image.tmdb.org/t/p/w500";

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            movie = new Movie();
            findViews(rootView);
            Intent intent = getActivity().getIntent();
            Picasso.with(getActivity()).load(URL_API + intent.getStringExtra(Movie.TAG_POSTER_PATH)).into(movie_poster_path);
            original_title.setText("Nome original: " + intent.getStringExtra(Movie.TAG_ORIGINAL_TITLE));
            overview.setText("Sinopse\n" + intent.getStringExtra(Movie.TAG_OVERVIEW));
            vote_average.setText("Nota: " + intent.getStringExtra(movie.TAG_VOTE_AVERAGE));
            try {
                release_data.setText("Data de Lançamento\n" + dateFormat(intent.getStringExtra(movie.TAG_RELEASE_DATA)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //String teste = dateFormat(intent.getStringExtra(movie.TAG_RELEASE_DATA));
            //release_data.setText("Data de Lançamento\n" + intent.getStringExtra(movie.TAG_RELEASE_DATA));
            return rootView;
        }

        private void findViews(View rootView){
            movie_poster_path = (ImageView)rootView.findViewById(R.id.movie_poster_path);
            original_title = (TextView)rootView.findViewById(R.id.original_title);
            overview = (TextView)rootView.findViewById(R.id.overview);
            vote_average = (TextView)rootView.findViewById(R.id.vote_average);
            release_data = (TextView)rootView.findViewById(R.id.release_date);
        }
    }

    private static String dateFormat(String dataToFormat) throws ParseException {
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
        Date date = inputFormat.parse(dataToFormat);
        String dateFormated = outputFormat.format(date);
        return dateFormated;
    }

}
