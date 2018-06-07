package com.example.marko.mojaaplikacija.services;

import java.util.List;

import model.Post;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by marko on 26.5.18..
 */

public interface PostService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })

    @GET(ServiceUtils.POSTS)
    Call<List<Post>> getPosts();

    @GET(ServiceUtils.POST)
    Call<Post> getPost(@Path("id") int id);

    @POST(ServiceUtils.POSTS)
    Call<Post> createPost(@Body Post post);

    @PUT("posts/{id}")
    Call<Post> addLikeDislike(@Body Post post,@Path("id") int id);

    @PUT("posts/setTags/{postId}/{tagId}")
    Call<Post> setTagsInPost(@Path("postId") int postId,@Path("tagId") int tagId);

    @DELETE("posts/{id}")
    Call<Post> deletePost(@Path("id") int id);


}
