package com.example.marko.mojaaplikacija.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by marko on 15.5.18..
 */

public class UserDBContentProvider extends ContentProvider {

    private UserSQLiteHelper1 database;

    private static final int USERS = 1;
    private static final int USERS_ID = 2;

    private static final String AUTHORITY = "com.example.marko.mojaaplikacija";

    private static final String USERS_PATH = "users";

    public static final Uri CONTENT_URI_USERS = Uri.parse("content://" + AUTHORITY + "/" + USERS_PATH);

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, USERS_PATH, USERS);
        sURIMatcher.addURI(AUTHORITY, USERS_PATH + "/#", USERS_ID);
    }

    @Override
    public boolean onCreate() {
        database = new UserSQLiteHelper1(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Uisng SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // check if the caller has requested a column which does not exist
        //checkColumns(projection);
        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case USERS_ID:
                // Adding the ID to the original query
                queryBuilder.appendWhere(UserSQLiteHelper1.COLUMN_ID + "="
                        + uri.getLastPathSegment());
                //$FALL-THROUGH$
            case USERS:
                // Set the table
                queryBuilder.setTables(UserSQLiteHelper1.TABLE_USERS);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        Uri retVal = null;
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        long id = 0;
        switch (uriType) {
            case USERS:
                id = sqlDB.insert(UserSQLiteHelper1.TABLE_USERS, null, values);
                retVal = Uri.parse(USERS_PATH + "/" + id);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return retVal;

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }


}
