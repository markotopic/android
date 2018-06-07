package com.example.marko.mojaaplikacija.provider;

import com.example.marko.mojaaplikacija.ORMdatabase.PostHelper;
import com.example.marko.mojaaplikacija.provider.model.Post;
import com.tojc.ormlite.android.OrmLiteSimpleContentProvider;
import com.tojc.ormlite.android.framework.MatcherController;
import com.tojc.ormlite.android.framework.MimeTypeVnd;

/**
 * Created by marko on 21.5.18..
 */

public class PostProvider extends OrmLiteSimpleContentProvider<PostHelper> {

    @Override
    protected Class<PostHelper> getHelperClass() {
        return PostHelper.class;
    }

    @Override
    public boolean onCreate() {
        setMatcherController(new MatcherController()//
                .add(Post.class, MimeTypeVnd.SubType.DIRECTORY, "", PostContract.Post.CONTENT_URI_PATTERN_MANY)//
                .add(Post.class, MimeTypeVnd.SubType.ITEM, "#", PostContract.Post.CONTENT_URI_PATTERN_ONE));
        return true;
    }
}
