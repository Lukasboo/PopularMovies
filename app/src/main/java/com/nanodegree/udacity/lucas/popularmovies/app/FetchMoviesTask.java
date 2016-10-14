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

/**
 * Created by Mobile on 07/10/2016.
 */

class FetchMoviesTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {
        String moviesJsonStr = sendRequest(params[0]);
        return moviesJsonStr;
    }

    private String sendRequest(String params){
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String moviesJson = null;
        try {
            URL url = new URL(uriBuilder(params));
            urlConnection = setUrlConnection(url);
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {
                return null;
            }
            moviesJson = buffer.toString();
        } catch (IOException e) {
            return null;
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

    private String uriBuilder(String params){
        Uri.Builder uri = new Uri.Builder();
        uri.scheme("http")
                .authority(Movie.TAG_URL_API)
                .appendPath("3")
                .appendPath("movie")
                .appendEncodedPath(params)
                .appendQueryParameter("api_key", Movie.TAG_KEY_API)
                .appendQueryParameter("language", "pt-BR");
        String urlBuild = uri.build().toString();
        return urlBuild;
    }

    private HttpURLConnection setUrlConnection(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();
        return urlConnection;
    }

}
