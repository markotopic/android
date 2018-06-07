package com.example.marko.mojaaplikacija.services;

import model.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * Created by marko on 29.5.18..
 */

public interface UserService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })

    @GET(ServiceUtils.LOGIN)
    Call<User> login(@Path("username") String username, @Path("password") String password);

    @GET("users/user/{username}")
    Call<User> getUserByUsername(@Path("username") String username);

}
