package com.example.marko.mojaaplikacija.provider.model;

import com.example.marko.mojaaplikacija.provider.PostContract;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation;

/**
 * Created by marko on 21.5.18..
 */

@DatabaseTable(tableName = PostContract.Post.TABLE_NAME)
@AdditionalAnnotation.DefaultContentUri(authority = PostContract.AUTHORITY, path = PostContract.Post.CONTENT_URI_PATH)
@AdditionalAnnotation.DefaultContentMimeTypeVnd(name = PostContract.Post.MIMETYPE_NAME, type = PostContract.Post.MIMETYPE_TYPE)
public class Post {

    @DatabaseField(columnName = PostContract.Post._ID, generatedId = true)
    @AdditionalAnnotation.DefaultSortOrder
    private int mId;

    @DatabaseField(columnName = PostContract.Post.FIELD_NAME_TITLE)
    private String mTitle;

    @DatabaseField(columnName = PostContract.Post.FIELD_NAME_DESCRIPTION)
    private String description;

    @DatabaseField(columnName = PostContract.Post.FIELD_NAME_PHOTO)
    private String photo;

    @DatabaseField(columnName = PostContract.Post.FIELD_NAME_AUTHOR)
    private String author;

    @DatabaseField(columnName = PostContract.Post.FIELD_NAME_DATE)
    private String date;

    @DatabaseField(columnName = PostContract.Post.FIELD_NAME_LOCATION)
    private String location;

    @DatabaseField(columnName = PostContract.Post.FIELD_NAME_TAGS)
    private String tags;

    @DatabaseField(columnName = PostContract.Post.FIELD_NAME_COMMENTS)
    private String comments;

    @DatabaseField(columnName = PostContract.Post.FIELD_NAME_LIKES)
    private String likes;

    @DatabaseField(columnName = PostContract.Post.FIELD_NAME_DISLIKES)
    private String dislikes;

    public Post() {
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getDislikes() {
        return dislikes;
    }

    public void setDislikes(String dislikes) {
        this.dislikes = dislikes;
    }

    @Override
    public String toString() {
        return  mTitle;
    }
}
