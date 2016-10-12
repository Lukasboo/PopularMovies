package com.nanodegree.udacity.lucas.popularmovies.app;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;

/**
 * Created by Mobile on 07/10/2016.
 */

public class FetchMoviesTask extends AsyncTask<String, Void, String> {


    //String API_KEY = "{INSERT API_KEY}";
    String API_KEY = "";
    @Override
    protected String doInBackground(String... params) {

        String teste = sendRequest(params[0]);

        return teste;

    }

    private String sendRequest(String params){

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String moviesJson = null;
        try {

            Uri.Builder uri = new Uri.Builder();
            uri.scheme("http")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("movie")
                    .appendEncodedPath("upcoming")
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter("language", "pt-BR")
                    .appendQueryParameter("sort_by", params);
                    //.appendQueryParameter("sort_by", "popularity.desc");
                    //.appendQueryParameter("units", "pt-BR");
            //.appendQueryParameter("page", "1");

            String urlBuild = uri.build().toString();
            URL url = new URL(urlBuild);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                //return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                //return null;
            }
            moviesJson = buffer.toString();
        } catch (IOException e) {
            //return null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("ForecastFragment", "Error closing stream", e);
                }
            }
        }
        return moviesJson;
    }


    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

}
