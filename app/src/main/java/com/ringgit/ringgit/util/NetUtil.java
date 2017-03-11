package com.ringgit.ringgit.util;

import android.content.Context;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ringgit.ringgit.BuildConfig;
import com.ringgit.ringgit.interfaces.RedditService;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Joe DiMaria on 3/9/2017.
 * Util for handling common network methods
 */

public class NetUtil {
    // TODO Most of these components are more manageable via Dependency Injection (like Dagger) so they're reusable
    // TODO across multiple activities. But since we're dealing with a demo SPA I figured that's a bit
    // TODO over engineering for now.
    private static Gson provideGson() {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }

    private static Cache provideOkHttpCache(Context context) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        return new Cache(context.getCacheDir(), cacheSize);
    }

    private static OkHttpClient provideOkHttpClient(Cache cache) {
        return new OkHttpClient.Builder().cache(cache).build();
    }

    private static Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BuildConfig.BASE_URL)
                .client(okHttpClient)
                .build();
    }

    public static RedditService getRedditService(Context context) {
        return provideRetrofit(
                provideGson(),
                provideOkHttpClient(provideOkHttpCache(context))
        ).create(RedditService.class);
    }
}
