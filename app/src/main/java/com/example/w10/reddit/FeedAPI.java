package com.example.w10.reddit;

import com.example.w10.reddit.Account.CheckLogin;
import com.example.w10.reddit.model.Feed;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static android.os.Build.VERSION_CODES.M;

/**
 * Created by W10 on 9/25/2017.
 */

public interface FeedAPI {

//    // static url (static feed name)
//    @GET("earthporn/.rss")
//    Call<Feed> getFeed();

    //non-static feed name
    @GET("{feedName}/.rss")
    Call<Feed> getFeed(@Path("feedName") String feedName);

    @POST("{user}")
    Call<CheckLogin> signIn(
            @HeaderMap Map<String, String> headers,
            @Path("user") String username,
            @Query("user") String user,
            @Query("passwd") String password,
            @Query("api_type") String type
    );

    @POST("{comment}")
    Call<CheckComment> submitComment(
            @HeaderMap Map<String, String> headers,
            @Path("comment") String username,
            @Query("parent") String user,
            @Query("amp;text") String password
    );

}
