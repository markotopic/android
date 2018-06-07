package com.example.marko.mojaaplikacija.tools;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.example.marko.mojaaplikacija.database.AplikacijaSQLiteHelper;
import com.example.marko.mojaaplikacija.database.DBContentProvider;
import com.example.marko.mojaaplikacija.database.UserDBContentProvider;
import com.example.marko.mojaaplikacija.database.UserSQLiteHelper;
import com.example.marko.mojaaplikacija.database.UserSQLiteHelper1;

/**
 * Created by marko on 13.5.18..
 */

public class Util {

    public static void initDB(Activity activity) {
//        AplikacijaSQLiteHelper dbHelper = new AplikacijaSQLiteHelper(activity);

        UserSQLiteHelper1 dbUser = new UserSQLiteHelper1(activity);

//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        {
//            ContentValues entry = new ContentValues();
//            entry.put(AplikacijaSQLiteHelper.COLUMN_TITLE, "Super naslov 1000");
//            entry.put(AplikacijaSQLiteHelper.COLUMN_DESCRIPTION, "Opis posta");
//
//            activity.getContentResolver().insert(DBContentProvider.CONTENT_URI_POSTS, entry);
//
//            entry = new ContentValues();
//            entry.put(AplikacijaSQLiteHelper.COLUMN_TITLE, "Super naslov 2000");
//            entry.put(AplikacijaSQLiteHelper.COLUMN_DESCRIPTION, "Opis posta");
//
//            activity.getContentResolver().insert(DBContentProvider.CONTENT_URI_POSTS, entry);
//
//        }
//        db.close();

        SQLiteDatabase dbu = dbUser.getWritableDatabase();
        {

            ContentValues entry = new ContentValues();
            entry.put(UserSQLiteHelper1.COLUMN_NAME, "Marko");
            entry.put(UserSQLiteHelper1.COLUMN_USERNAME, "mare");
            entry.put(UserSQLiteHelper1.COLUMN_PASSWORD, "123");

            activity.getContentResolver().insert(UserDBContentProvider.CONTENT_URI_USERS, entry);

        }
        dbu.close();

    }

}
