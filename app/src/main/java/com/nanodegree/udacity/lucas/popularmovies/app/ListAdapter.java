package com.nanodegree.udacity.lucas.popularmovies.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Mobile on 07/10/2016.
 */

public class ListAdapter extends BaseAdapter {

    private Context context;
    int resource;
    int[] to;
    ArrayList<Movie> movieData;
    String[] from;
    public static final String URL_API = "https://image.tmdb.org/t/p/w500";

    public ListAdapter(Context context, ArrayList<Movie> movieData, int resource, String[] from, int[] to){
        this.context = context;
        this.movieData = movieData;
        this.resource = resource;
        this.from = from;
        this.to = to;
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
            viewHolder.movie_name = (TextView) convertView.findViewById(R.id.movie_name_entry);
            viewHolder.movie_release_date = (TextView) convertView.findViewById(R.id.release_date);
            viewHolder.movie_image = (ImageView) convertView.findViewById(R.id.imgmovie_entry);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //String teste = URL_API + movieData.get(position).getPoster_path().toString().toString();
        viewHolder.movie_name.setText("Título:" + movieData.get(position).getTitle().toString());
        viewHolder.movie_release_date.setText("Data Estréia:" + movieData.get(position)
                .getRelease_date().toString());
        Picasso.with(context).load(URL_API + movieData.get(position)
                .getPoster_path().toString())
                .into(viewHolder.movie_image);
        return convertView;
    }

    public class ViewHolder {
        //ListView movieList;
        TextView movie_name;
        TextView movie_release_date;
        ImageView movie_image;
    }
}
