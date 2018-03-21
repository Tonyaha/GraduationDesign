package com.tm.book_of_exercises.main.bean;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;

/**
 * Created by T M on 2018/2/9.
 */

public class Image_Handle{

    public static String bitmapToString(Bitmap bitmap) {
        // 保存
        // BitmapDrawable drawble = (BitmapDrawable) cv_head.getDrawable();

        // 第一步 将bitmap 转换成字节数组输出流
        ByteArrayOutputStream byStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byStream);

        // 利用base64将字节数组转换成字符串
        byte[] byteArray = byStream.toByteArray();
        String imgString = new String(Base64.encodeToString(byteArray, Base64.DEFAULT));
        Log.d("aaaaaaaaa",imgString);
        return imgString;

    }
    public static Bitmap stringToImage(String imgStr) {
        //Log.d("aaaaaaaaa",imgStr);
        byte[] b = Base64.decode(imgStr, Base64.DEFAULT);
        for (int i = 0; i < b.length; ++i) {
            if (b[i] < 0) {// 调整异常数据
                b[i] += 256;
            }
        }
        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        // 生成jpeg图片
        return bitmap;

    }
}