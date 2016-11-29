package com.nanodegree.udacity.lucas.popularmovies.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.Serializable;

/**
 * Created by lucas on 27/11/16.
 */

public class MovieReviewFragment  extends Fragment implements Serializable {

    MovieReview movieReview;
    TextView txtauthor;
    TextView txtcontent;
    ImageButton ibtfavorite;
    TextView txtmark;

    public MovieReviewFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movie_review, container, false);

        ibtfavorite = (ImageButton) rootView.findViewById(R.id.ibtfavorite);
        ibtfavorite.setVisibility(View.INVISIBLE);

        txtmark = (TextView) rootView.findViewById(R.id.txtmark);
        txtmark.setVisibility(View.INVISIBLE);

        txtauthor = (TextView) rootView.findViewById(R.id.txtauthor);
        txtcontent = (TextView) rootView.findViewById(R.id.txtcontent);
        movieReview = new MovieReview();
        Intent intent = getActivity().getIntent();
        movieReview = intent.getExtras().getParcelable("moviereview");

        txtauthor.setText(movieReview.getAuthor().toString());
        txtcontent.setText(movieReview.getContent().toString());

        return rootView;
    }
}
