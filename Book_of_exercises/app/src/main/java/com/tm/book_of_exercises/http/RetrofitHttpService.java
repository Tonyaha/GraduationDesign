package com.tm.book_of_exercises.http;


import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by T M on 2018/3/16.
 */

public interface RetrofitHttpService {
    //URLEncoder.encode("张德帅","UTF-8");
    @GET(".") // 没有参数就加 "."
    Call<ResponseBody> get(@QueryMap Map<String, Object> params);

    @FormUrlEncoded
    @POST(".") // 没有参数就加 "."
    Call<ResponseBody> post(@FieldMap Map<String, Object> params);

    @GET()
    Observable<String> Obget(@Url String url, @QueryMap Map<String, String> params, @HeaderMap Map<String, String> headers);

    @FormUrlEncoded
    @POST()
    Observable<String> Obpost(@Url String url, @FieldMap Map<String, String> params, @HeaderMap Map<String, String> headers);

    @FormUrlEncoded
    @PUT
    Observable<String> Obput(@Url String url, @FieldMap Map<String, String> params, @HeaderMap Map<String, String> headers);

    @Streaming
    @GET()
    Observable<ResponseBody> Obdownload(@HeaderMap Map<String, String> headers, @Url String url, @QueryMap Map<String, String> params);

    @Streaming
    @GET()
    Call<ResponseBody> download(@HeaderMap Map<String, String> headers, @Url String url, @QueryMap Map<String, String> params);

}
