package com.example.marko.mojaaplikacija.services;

import java.util.List;

import model.Comment;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by marko on 1.6.18..
 */

public interface CommentService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })

    @GET(ServiceUtils.COMMENTSBYPOST)
    Call<List<Comment>> getCommentsByPost(@Path("id") int id);

    @POST("comments")
    Call<ResponseBody> addComment(@Body Comment comment);

    @DELETE("comments/{id}")
    Call<Comment> deleteComment(@Path("id") int id);
}
