package com.nanodegree.udacity.lucas.popularmovies.app;

/**
 * Created by Mobile on 07/10/2016.
 */

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

    int typeUri;

    public FetchMoviesTask(int typeUri) {
        this.typeUri = typeUri;
    }

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
            URL url = null;
            if (typeUri==Movie.TAG_URI_TYPE_MAIN) {
                url = new URL(uriBuilder(params));
            } else if (typeUri==Movie.TAG_URI_TYPE_DETAIL) {
                url = new URL(uriBuilder(params, "videos"));
            } else if (typeUri==Movie.TAG_URI_TYPE_DETAIL) {
                url = new URL(uriBuilder(params, "videos"));
            } else {
                Log.d("TaskError", "Erro");
                return null;
            }
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
    //scheme  authority          pathpath param  api_key
    //https://api.themoviedb.org/3/movie/popular?api_key=9c533054f3463670421676bacdb1966b&language=pt-br
    //scheme  authority          pathpath param  api_key
    //https://api.themoviedb.org/3/movie/188927?api_key=9c533054f3463670421676bacdb1966b&language=pt-br
    //scheme  authority          pathpath param  param  api_key
    //https://api.themoviedb.org/3/movie/188927/videos?api_key=9c533054f3463670421676bacdb1966b&language=pt-br
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
    //https://api.themoviedb.org/3/movie/278/videos?api_key=9c533054f3463670421676bacdb1966b
    private String uriBuilder(String params, String paramstwo){
        Uri.Builder uri = new Uri.Builder();
        uri.scheme("http")
                .authority(Movie.TAG_URL_API)
                .appendPath("3")
                .appendPath("movie")
                .appendEncodedPath(params)
                .appendPath(paramstwo)
                .appendQueryParameter("api_key", Movie.TAG_KEY_API);
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
