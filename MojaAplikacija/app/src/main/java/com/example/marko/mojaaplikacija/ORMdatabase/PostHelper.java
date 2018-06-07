package com.example.marko.mojaaplikacija.ORMdatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.marko.mojaaplikacija.provider.PostContract;
import com.example.marko.mojaaplikacija.provider.model.Post;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by marko on 21.5.18..
 */

public class PostHelper extends OrmLiteSqliteOpenHelper {

    private Dao<Post, Integer> mPostDao = null;

    public PostHelper(Context context) {
        super(context,
                PostContract.DATABASE_NAME,
                null,
                PostContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Post.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Post.class, true);
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Dao<Post, Integer> getPostDao() throws SQLException {
        if (mPostDao == null) {
            mPostDao = getDao(Post.class);
        }

        return mPostDao;
    }

    @Override
    public void close() {
        mPostDao = null;

        super.close();
    }

}
