package com.ringgit.ringgit.interfaces;

import com.ringgit.ringgit.data.Listing;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Joe DiMaria on 3/9/2017.
 * Service for interacting with the Reddit API
 */

public interface RedditService {
    String TOP_TEN_IN_JSON = "/top/.json?limit=10";
    String TOP_TEN_PAGINATED = TOP_TEN_IN_JSON + "&count=10";

    @GET(TOP_TEN_IN_JSON)
    Call<Listing> getTopTenPosts();

    @GET(TOP_TEN_PAGINATED)
    Call<Listing> getTopTenBefore(@Query("before") String before);

    @GET(TOP_TEN_PAGINATED)
    Call<Listing> getTopTenAfter(@Query("after") String after);
}
