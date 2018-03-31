package com.tm.book_of_exercises.constant;

import android.net.Uri;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by T M on 2018/2/9.
 */
public class Constant{
    public final int CODE_SELECT_IMAGE = 2;//相册RequestCode
    public final int TYPE_TAKE_PHOTO = 1;//Uri获取类型判断
    public final int CODE_TAKE_PHOTO = 1;//相机RequestCode
    public final String BaseUrl = "http://192.168.137.1:8000";//"http://140.143.95.232:8000";//"http://192.168.137.1:8081";
    public static String username= "";
    public static Uri uri_admin = Uri.parse("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2619911597,2242133565&fm=27&gp=0.jpg");
}

