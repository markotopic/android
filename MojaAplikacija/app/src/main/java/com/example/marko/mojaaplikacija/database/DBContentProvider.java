package com.example.marko.mojaaplikacija.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * Created by marko on 13.5.18..
 */

public class DBContentProvider extends ContentProvider {

    private AplikacijaSQLiteHelper database;

    private static final int POSTS = 1;
    private static final int POSTS_ID = 2;

    private static final String AUTHORITY = "com.example.marko.mojaaplikacija";

    private static final String POSTS_PATH = "posts";

    public static final Uri CONTENT_URI_POSTS = Uri.parse("content://" + AUTHORITY + "/" + POSTS_PATH);

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, POSTS_PATH, POSTS);
        sURIMatcher.addURI(AUTHORITY, POSTS_PATH + "/#", POSTS_ID);
    }

    @Override
    public boolean onCreate() {
        database = new AplikacijaSQLiteHelper(getContext());
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
            case POSTS_ID:
                // Adding the ID to the original query
                queryBuilder.appendWhere(AplikacijaSQLiteHelper.COLUMN_ID + "="
                        + uri.getLastPathSegment());
                //$FALL-THROUGH$
            case POSTS:
                // Set the table
                queryBuilder.setTables(AplikacijaSQLiteHelper.TABLE_POSTS);
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
    public String getType(Uri uri) {
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
            case POSTS:
                id = sqlDB.insert(AplikacijaSQLiteHelper.TABLE_POSTS, null, values);
                retVal = Uri.parse(POSTS_PATH + "/" + id);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return retVal;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        long id = 0;
        int rowsDeleted = 0;
        switch (uriType) {
            case POSTS:
                rowsDeleted = sqlDB.delete(AplikacijaSQLiteHelper.TABLE_POSTS,
                        selection,
                        selectionArgs);
                break;
            case POSTS_ID:
                String idPost = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(AplikacijaSQLiteHelper.TABLE_POSTS,
                            AplikacijaSQLiteHelper.COLUMN_ID + "=" + idPost,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(AplikacijaSQLiteHelper.TABLE_POSTS,
                            AplikacijaSQLiteHelper.COLUMN_ID + "=" + idPost
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        long id = 0;
        int rowsUpdated = 0;
        switch (uriType) {
            case POSTS:
                rowsUpdated = sqlDB.update(AplikacijaSQLiteHelper.TABLE_POSTS,
                        values,
                        selection,
                        selectionArgs);
                break;
            case POSTS_ID:
                String idCinema = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(AplikacijaSQLiteHelper.TABLE_POSTS,
                            values,
                            AplikacijaSQLiteHelper.COLUMN_ID + "=" + idCinema,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(AplikacijaSQLiteHelper.TABLE_POSTS,
                            values,
                            AplikacijaSQLiteHelper.COLUMN_ID + "=" + idCinema
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

}
