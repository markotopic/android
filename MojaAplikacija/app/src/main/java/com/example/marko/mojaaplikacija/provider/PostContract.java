package com.example.marko.mojaaplikacija.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by marko on 21.5.18..
 */

public class PostContract {

    public static final String DATABASE_NAME = "MyDatabase";
    public static final int DATABASE_VERSION = 1;

    public static final String AUTHORITY = "com.example.marko.mojaaplikacija";

    public static class Post implements BaseColumns
    {
        public static final String TABLE_NAME = "posts";

        public static final String CONTENT_URI_PATH = TABLE_NAME;

        public static final String MIMETYPE_TYPE = TABLE_NAME;
        public static final String MIMETYPE_NAME = AUTHORITY + ".provider";

        public static final String FIELD_NAME_TITLE   = "title";
        public static final String FIELD_NAME_DESCRIPTION   = "description";
        public static final String FIELD_NAME_PHOTO  = "photo";
        public static final String FIELD_NAME_AUTHOR   = "author";
        public static final String FIELD_NAME_DATE   = "date";
        public static final String FIELD_NAME_LOCATION   = "location";
        public static final String FIELD_NAME_TAGS   = "tags";
        public static final String FIELD_NAME_COMMENTS   = "comments";
        public static final String FIELD_NAME_LIKES   = "likes";
        public static final String FIELD_NAME_DISLIKES   = "dislikes";

        public static final int CONTENT_URI_PATTERN_MANY = 1;
        public static final int CONTENT_URI_PATTERN_ONE = 2;

        public static final Uri contentUri = new Uri.Builder()
                .scheme(ContentResolver.SCHEME_CONTENT)
                .authority(AUTHORITY)
                .appendPath(CONTENT_URI_PATH).build();
    }


}
