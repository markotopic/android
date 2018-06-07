package com.example.marko.mojaaplikacija.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by marko on 13.5.18..
 */

public class AplikacijaSQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_POSTS = "posts";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";

    private static final String DATABASE_NAME = "posts.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DB_CREATE = "create table "
            + TABLE_POSTS + "("
            + COLUMN_ID  + " integer primary key autoincrement , "
            + COLUMN_TITLE + " text, "
            + COLUMN_DESCRIPTION + " text"
            + ")";

    public AplikacijaSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POSTS);
        onCreate(db);
    }
}
