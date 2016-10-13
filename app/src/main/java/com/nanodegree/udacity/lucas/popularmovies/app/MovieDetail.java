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

import java.io.Serializable;

public class MovieDetail extends AppCompatActivity implements Serializable {

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

    public static class PlaceholderFragment extends Fragment implements Serializable {

        Movie movie;
        ImageView movie_poster_path;
        TextView original_title;
        TextView overview;
        TextView vote_average;
        TextView release_data;


        public PlaceholderFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            movie = new Movie();
            findViews(rootView);
            Intent intent = getActivity().getIntent();
            settingTexts(intent);
            Picasso.with(getActivity()).load(Movie.TAG_URL_POSTER_PATH + intent.getStringExtra(Movie.TAG_POSTER_PATH))
                    .into(movie_poster_path);

            return rootView;

        }

        private void findViews(View rootView){
            movie_poster_path = (ImageView)rootView.findViewById(R.id.movie_poster_path);
            original_title = (TextView)rootView.findViewById(R.id.original_title);
            overview = (TextView)rootView.findViewById(R.id.overview);
            vote_average = (TextView)rootView.findViewById(R.id.vote_average);
            release_data = (TextView)rootView.findViewById(R.id.release_date);
        }

        private void settingTexts (Intent intent){
            original_title.setText("Nome original: " + intent.getStringExtra(Movie.TAG_ORIGINAL_TITLE));
            overview.setText("Sinopse\n" + intent.getStringExtra(Movie.TAG_OVERVIEW));
            vote_average.setText("Nota: " + intent.getStringExtra(movie.TAG_VOTE_AVERAGE));
            release_data.setText("Data de Lançamento\n" + intent.getStringExtra(movie.TAG_RELEASE_DATA));
        }
    }
}
