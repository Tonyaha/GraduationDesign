package com.tm.book_of_exercises.constant;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.tm.book_of_exercises.MainActivity;
import com.tm.book_of_exercises.chat.customMsg.CxImgMessage;
import com.tm.book_of_exercises.http.RetrofitBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by T M on 2018/2/9.
 */
public class Constant{
    public static String occupation = "学生";
    public static String myAnswer = "";
    public static String answer = "";
    public static String sendData = "";
    public static boolean showSelectFlag = false;
    public static boolean showAnswerFlag =false;
    public static boolean showWriteAnswerFlag =false;
    public static final int CODE_CUT_IMAGE = 3;//裁剪图片
    public static final int CODE_SELECT_IMAGE = 2;//相册RequestCode
    public static final int CODE_SELECT_IMAGE_Add = 4;//相册RequestCode
    public static final int TYPE_TAKE_PHOTO = 1;//Uri获取类型判断
    public static final int CODE_TAKE_PHOTO = 1;//相机RequestCode
    public static final String FILE_PATH = Environment.getExternalStoragePublicDirectory("").getPath();//Environment.getExternalStorageDirectory().toString()+"/";
    public final String BaseUrl = "http://193.112.122.190:8000";//"http://192.168.137.1:8000";//"http://192.168.43.165:8000";//;
    public static String username= "";

    private ArrayList<JSONObject> data = new ArrayList<>();

    public static ArrayList<JSONObject> kongData = new ArrayList<>();
    private HashMap<String, Object> map = new HashMap<>();
    private Context context;
    public Constant(){}

    public Constant(Context context){
        this.context = context;
    }
    // 关注按钮
    public void follow(int id,String follow,String msg,String answer) {
        Constant constant = new Constant();
        map.put("username", Constant.username);
        map.put("action", "modifyCollect");
        map.put("isFollow",follow);
        map.put("taskId",id);
        map.put("msg",msg);
        map.put("answer",answer);
        RetrofitBuilder retrofitBuilder = new RetrofitBuilder(constant.BaseUrl + "/api/userCollect/");
        retrofitBuilder.isConnected(context);
        retrofitBuilder.params(map);
        retrofitBuilder.post();
        Call<ResponseBody> call = retrofitBuilder.getCall();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    //System.out.println("//////"+response.body().string());
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    if("200".equals(jsonObject.getString("code"))){
                        myAnswer = jsonObject.getString("myAnswer");
                        Toast.makeText(context,"change success ",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(context,"change fail ",Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void addFriend(Context context ,String userId,String targetId){
        map.put("username",userId);
        map.put("targetname",targetId);
        RetrofitBuilder builder = new RetrofitBuilder(BaseUrl + "/api/newFriend/");
        builder.isConnected(context);
        builder.params(map);
        builder.post();
        Call<ResponseBody> call = builder.getCall();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    //发送自定义消息
    public void sendMsg(String targetId,String data) {
        CxImgMessage cxImgMessage = CxImgMessage.obtain(data);
        Message myMessage = Message.obtain(targetId, Conversation.ConversationType.PRIVATE,cxImgMessage);
        RongIM.getInstance().sendMessage(myMessage,null,null, new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(Message message) {

            }

            @Override
            public void onSuccess(Message message) {
                Toast.makeText(context,"发送成功",Toast.LENGTH_LONG).show();
                sendData = null;
                MainActivity.selectContactFlag = 0;
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                Toast.makeText(context,"发送失败",Toast.LENGTH_LONG).show();
            }
        });
    }

    //全部习题
    public void tasksRequest() {
        ArrayList<JSONObject> data_1 = new ArrayList<>();
        HashMap<String, Object> map = new HashMap<>();
        Constant constant = new Constant();
        RetrofitBuilder builder = new RetrofitBuilder(constant.BaseUrl + "/api/userCollect/");
        builder.isConnected(context);
        map.put("username", Constant.username);
        map.put("action", "allTasks");
        builder.params(map);
        builder.get();
        Call<ResponseBody> call = builder.getCall();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    String code = jsonObject.getString("code");
                    if ("200".equals(code)) {
                        try {
                            JSONArray jsonArray = jsonObject.getJSONArray("list");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jb = jsonArray.getJSONObject(i);
                                data_1.add(jb);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        setData(data_1);
                        //Log.e("调试", String.valueOf(data_1));
                        Toast.makeText(context,"数据请求成功",Toast.LENGTH_LONG).show();
                    } else if ("404".equals(code)) {
                        Log.e("CollectFragment", jsonObject.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void setData(ArrayList<JSONObject> data) {
        this.data = data;
        Log.e("调试", "//////////11111" + this.data);
    }

    public ArrayList<JSONObject> getData() {
        Log.e("调试", "//////////========222" + this.data);
        return data;
    }

    // CameraUtil的裁剪方法
    public static Intent cropPhoto(Uri uri, Uri cropUri, int outputX, int outputY) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");//可裁剪
        intent.putExtra("aspectX", 1); //裁剪的宽比例
        intent.putExtra("aspectY", 1);  //裁剪的高比例
        intent.putExtra("outputX", outputX); //裁剪的宽度
        intent.putExtra("outputY", outputY);  //裁剪的高度
        intent.putExtra("scale", true); //支持缩放
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri);  //将裁剪的结果输出到指定的Uri
        intent.putExtra("return-data", true); //若为true则表示返回数据
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());//裁剪成的图片的格式
        intent.putExtra("noFaceDetection", true);  //启用人脸识别
        return intent;
    }

    //自定义拍照保存的临时路径
    public static Uri getTempUri() {
        String fileName = System.currentTimeMillis() + ".jpeg";
//        File out = new File(FILE_PATH+fileName);
//        if (!out.exists()) {
//            out.mkdirs();
//        }
       // out = new File(FILE_PATH, fileName);
        return Uri.fromFile(new File(FILE_PATH + fileName));
    }

    public static Bitmap getBitmapByUri(Context context, Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver()
                    .openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }
}

