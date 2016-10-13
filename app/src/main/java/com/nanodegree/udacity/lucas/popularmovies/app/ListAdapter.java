package com.nanodegree.udacity.lucas.popularmovies.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Mobile on 07/10/2016.
 */

public class ListAdapter extends BaseAdapter {

    private Context context;
    int resource;
    ArrayList<Movie> movieData;

    public ListAdapter(Context context, ArrayList<Movie> movieData, int resource){
        this.context = context;
        this.movieData = movieData;
        this.resource = resource;
    }

    @Override
    public int getCount() {
        return movieData.size();
    }

    @Override
    public Object getItem(int position) { return movieData.get(position).toString(); }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder viewHolder;
        if (convertView==null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.movies_entry, null);
            viewHolder.movie_image = (ImageView) convertView.findViewById(R.id.imgmovie_entry);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Picasso.with(context).load(Movie.TAG_URL_POSTER_PATH + movieData.get(position)
                .getPoster_path().toString())
                .into(viewHolder.movie_image);
        return convertView;
    }

    public class ViewHolder {
        ImageView movie_image;
    }
}
