package com.tm.book_of_exercises.main.http.utils;

import android.content.Context;

import com.tm.book_of_exercises.BuildConfig;
import com.tm.book_of_exercises.main.http.cacahe.CacheProvide;
import com.tm.book_of_exercises.main.http.interceptor.CacheInterceptor;
import com.tm.book_of_exercises.main.http.interceptor.DownLoadInterceptor;
import com.tm.book_of_exercises.main.http.interceptor.HttpLoggingInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by 耿 on 2016/8/27.
 */
public class OkhttpProvidede {
    static OkHttpClient okHttpClient;

    public static OkHttpClient okHttpClient(final Context context, String BASE_URL) {
        if (okHttpClient == null) {
            synchronized (OkhttpProvidede.class) {
                if (okHttpClient == null) {
                    OkHttpClient client = new OkHttpClient.Builder()
                            .addInterceptor(new DownLoadInterceptor(BASE_URL))
                            .addNetworkInterceptor(new CacheInterceptor())
                            .cache(new CacheProvide(context).provideCache())
                            .retryOnConnectionFailure(true)
                            .connectTimeout(5, TimeUnit.SECONDS)
                            .readTimeout(8, TimeUnit.SECONDS)
                            .writeTimeout(8, TimeUnit.SECONDS)
                            .build();
                    if (BuildConfig.DEBUG) {//printf logs while  debug
                        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                        client = client.newBuilder().addInterceptor(logging).build();
                    }
                    okHttpClient = client;
                }

            }

        }
        return okHttpClient;

    }
}
