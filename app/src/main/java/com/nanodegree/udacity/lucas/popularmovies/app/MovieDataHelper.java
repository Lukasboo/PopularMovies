package com.nanodegree.udacity.lucas.popularmovies.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mobile on 04/11/2016.
 */

public class MovieDataHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    static final String DATABASE_NAME = "movie.db";

    private static final String TABLE_FAVORITE_MOVIES = "favoritemovies";

    private static final String TABLE_CREATE_FAVORITE_MOVIES =
            "create table product (id integer primary key not null, " +
                    " movie_id integer not null, original_title text not null, overview text not null, " +
                    "imagem text not null, nome_imagem text not null, valor_final real not null, " +
                    " info_nutri_imagem text not null, data_cadastro text not null, " +
                    "id_produto integer not null, id_maquina integer not null, " +
                    " quantidade text not null, posicao text not null, repor integer not null);";

    public MovieDataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
