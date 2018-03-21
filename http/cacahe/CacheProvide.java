package com.tm.book_of_exercises.main.http.cacahe;

import android.content.Context;

import okhttp3.Cache;

/**
 * Created by 耿 on 2016/8/12.
 */
public class CacheProvide {
    Context mContext;

    public CacheProvide(Context context) {
        mContext = context;
    }

    public Cache provideCache() {
        return new Cache(mContext.getCacheDir(), 50*1024 * 1024);
    }
}
