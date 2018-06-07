package com.example.marko.mojaaplikacija.services;

import java.util.List;

import model.Tag;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by marko on 1.6.18..
 */

public interface TagService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })

    @GET("tags/post/{id}")
    Call<List<Tag>> getTagsByPost(@Path("id") int id);

    @GET("tags/getName/{name}")
    Call<Tag> getTagByName(@Path("name") String name);

    @POST("tags")
    Call<Tag> addTag(@Body Tag tag);

}
