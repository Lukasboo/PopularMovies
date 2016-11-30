package com.nanodegree.udacity.lucas.popularmovies.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import static com.nanodegree.udacity.lucas.popularmovies.app.R.id.txttrailer;

/**
 * Created by lucas on 30/10/16.
 */

public class TrailerListAdapter extends BaseAdapter {

    private Context context;
    int resource;
    ArrayList<MovieTrailer> trailerData;

    public TrailerListAdapter(Context context, ArrayList<MovieTrailer> trailerData, int resource){
        this.context = context;
        this.trailerData = trailerData;
        this.resource = resource;
    }

    @Override
    public int getCount() {
        return trailerData.size();
    }

    @Override
    public Object getItem(int position) { return trailerData.get(position).toString(); }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TrailerListAdapter.ViewHolder viewHolder;
        if (convertView==null){
            viewHolder = new TrailerListAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.trailers_entry, null);
            viewHolder.txttrailer = (TextView) convertView.findViewById(txttrailer);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (TrailerListAdapter.ViewHolder) convertView.getTag();
        }
        viewHolder.txttrailer.setText(trailerData.get(position).getName());
        return convertView;
    }

    public class ViewHolder {
        TextView txttrailer;
    }
}
