package com.tm.book_of_exercises.main.mainPage;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tm.book_of_exercises.MainActivity;
import com.tm.book_of_exercises.R;
import com.tm.book_of_exercises.adapter.CollectAdapter;
import com.tm.book_of_exercises.constant.Constant;
import com.tm.book_of_exercises.http.RetrofitBuilder;
import com.tm.book_of_exercises.main.bean.ImageHandle;
import com.tm.book_of_exercises.main.otherPage.SelectContactActivity;
import com.tm.book_of_exercises.myDialog.InputDialog;
import com.tm.book_of_exercises.myDialog.InputDialogInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by T M on 2018/3/14.
 */

public class CollectFragment extends Fragment {
    private RecyclerView mRecyclerView;
    public CollectAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button btn_send;
    private LinearLayout linearLayout_collect;
    private boolean flag = false;
    private int taskID = 0;
    ArrayList<HashMap> list = new ArrayList<>();
    public static int pos = 999999999;
    private Bitmap prodImg;
    public static String strBitmap = "";
    private ImageView imgTest;
    private Uri imgUriSelect;
    private String mFilePath;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collect, container, false);
        initData();  // 初始化在前面，不然提示 E/RecyclerView: No adapter attached; skipping layout
        if (MainActivity.updateCollectFlag){
            collectRequest();
        }
        //AnnotateUtils.injectViews(getActivity());

        btn_send = view.findViewById(R.id.btnCollectSend);
        linearLayout_collect = view.findViewById(R.id.linearLayoutCollect);
        imgTest = view.findViewById(R.id.testImg);

        mRecyclerView = view.findViewById(R.id.collectRecyclerView);
        mRecyclerView.setLayoutManager(mLayoutManager); // 设置布局管理器
        mRecyclerView.setAdapter(mAdapter); // 设置adapter
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());  // 设置Item添加和移除的动画
        //mRecyclerView.addItemDecoration(mDividerItemDecoration); // 设置Item之间间隔样式

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(getActivity(), SelectContactActivity.class);
                sendIntent.putExtra("data", new Gson().toJson(list));
                startActivity(sendIntent);
            }
        });
        return view;//super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initData() {
        mFilePath = Environment.getExternalStorageDirectory().getPath();// 获取SD卡路径
        mFilePath = mFilePath + "/" + "temp.png";// 指定路径
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mAdapter = new CollectAdapter(getActivity(), MainActivity.collectData);
        //mAdapter.updateData(MainActivity.collectData);
//        if(MainActivity.collectData != null || !"".equals(MainActivity.collectData) || !"null".equals(MainActivity.collectData)){
//            mAdapter.updateData(Constant.kongData);
//        }else {
//            mAdapter = new CollectAdapter(getActivity(), MainActivity.collectData);
//        }
        mAdapter.mySetOnClickListener(new CollectAdapter.MyOnClickListener() {
            @Override
            public void onViewClickListener(View view, int position) {
                switch (view.getId()) {
                    case R.id.img_msg:
                        String msg = null;
                        JSONObject jsonObject = MainActivity.collectData.get(position);
                        try {
                            msg = jsonObject.getString("msg");
                            taskID = jsonObject.getInt("taskId");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        InputDialog addFriendDialog = new InputDialog();
                        addFriendDialog.init("设置留言", "输入留言", msg, new InputDialogInterface() {
                            @Override
                            public void onClick() {
                                //Log.e("click!!!" , addFriendDialog.getInput());
                                Constant constant = new Constant(getActivity());
                                constant.follow(taskID, "已关注", addFriendDialog.getInput(), "");
                            }
                        });
                        addFriendDialog.show(getFragmentManager(), "");
                        break;
                    case R.id.tasks_follow:
                        //Toast.makeText(getActivity(),"收藏按钮  +  "+position,Toast.LENGTH_SHORT).show();
                        JSONObject jb = MainActivity.collectData.get(position);
                        TextView tv = view.findViewById(R.id.tasks_follow);
                        try {
                            Constant constant = new Constant(getActivity());
                            constant.follow(jb.getInt("taskId"), tv.getText().toString(), "null", "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        MainActivity.updateFlag = true;
                        break;
                    case R.id.collect_select_tastk:
                        JSONObject jb_select = MainActivity.collectData.get(position); //垃圾程序 不能传JSONObjact;
                        ImageView imageView = view.findViewById(R.id.collect_select_tastk);
                        String context = "";
                        String imgUri = "";
                        int tasksId = 0;
                        String answer = "";
                        if (list.size() > 1) { //加一层保障，防止数据重复
                            for (int i = 0; i < list.size(); i++) {
                                for (int j = 1; j < list.size(); j++) {
                                    if (list.get(i).equals(list.get(j))) {
                                        list.remove(j);
                                    }
                                }
                            }
                        }
                        try {
                            context = jb_select.getString("context");
                            tasksId = jb_select.getInt("taskId");
                            imgUri = jb_select.getString("contextImg");
                            answer = jb_select.getString("answer");
                            //Log.e("iii", "////位置：" + position + "数据：" + fromId+context+tasksId+imgUri);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (!flag) {
                            imageView.setImageResource(R.mipmap.select_true);
                            list.add(getData(String.valueOf(tasksId), context, imgUri, answer));
                            flag = true;
                        } else {
                            imageView.setImageResource(R.mipmap.select_false);
                            list.remove(getData(String.valueOf(tasksId), context, imgUri, answer));
                            flag = false;
                        }
                        //Log.e("iii", "位置：" + position + " ,长度：" +  list.size() + " ,数据：" + String.valueOf(list));
                        break;
                    case R.id.tasks_see_answer:
                        pos = position;
                        Constant.showWriteAnswerFlag = false;
                        if (!flag) {
                            Constant.showAnswerFlag = true;
                            mAdapter.updateData(MainActivity.collectData);
                            flag = true;
                        } else {
                            Constant.showAnswerFlag = false;
                            mAdapter.updateData(MainActivity.collectData);
                            flag = false;
                        }
                        break;
                    case R.id.collect_write_answer:
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                        alertDialog.setTitle("选择图片")
                                .setIcon(R.mipmap.icon)
                                .setMessage("选择一种获取答案的方式")
                                .setCancelable(true);

                        alertDialog.setPositiveButton("照相", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //启动相机程序
                                Intent takePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                Uri photoUri = Uri.fromFile(new File(mFilePath)); // 传递路径
                                takePhoto.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);  //设置自定义拍照保存的临时路径 ,//指定图片存放位置，指定后，在onActivityResult里得到的Data将为null
                                startActivityForResult(takePhoto, Constant.CODE_TAKE_PHOTO);
                                dialogInterface.cancel();
                            }
                        });
                        alertDialog.setNegativeButton("相册", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent selectFromPhotos = new Intent(Intent.ACTION_PICK);
                                //takePhoto.putExtra(MediaStore.EXTRA_OUTPUT, imgUriSelect);  //设置自定义拍照保存的临时路径 ,//指定图片存放位置，指定后，在onActivityResult里得到的Data将为null
                                selectFromPhotos.setType("image/*");
                                startActivityForResult(selectFromPhotos, Constant.CODE_SELECT_IMAGE);
                                dialogInterface.cancel();
                            }
                        });
                        alertDialog.show();
                        JSONObject jb_add_answer = MainActivity.collectData.get(position); //垃圾程序 不能传JSONObjact;
                        try {
                            taskID = jb_add_answer.getInt("taskId");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pos = position;
                        Constant.showWriteAnswerFlag = true;
                        mAdapter.updateData(MainActivity.collectData);
                        break;
                }
            }
        });
        mAdapter.setOnItemLongClickListener(new CollectAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClickListener(View itemView, int position) {
                linearLayout_collect.setVisibility(View.VISIBLE);
                Constant.showSelectFlag = true;
                mAdapter.updateData(MainActivity.collectData);
            }
        });

    }

    private HashMap getData(String taskId, String context, String uri, String answer) {
        HashMap<String, String> map = new HashMap<>();
        map.put("from", "notFromAddFragment");
        map.put("tasksId", taskId);
        map.put("content", context);
        map.put("imgUri", uri);//strBitmap);
        map.put("answer", answer);//strBitmap);
        map.put("myAnswer", Constant.myAnswer);
        map.put("occupation",Constant.occupation);
        //Log.e("aaa",taskId + " /// " +answer);
        return map;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constant.CODE_TAKE_PHOTO:
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(mFilePath); // 根据路径获取数据
                    Bitmap bitmap = ImageHandle.compressScale(BitmapFactory.decodeStream(fis)); //已经按质量压缩
                    strBitmap = ImageHandle.bitmapToString(bitmap);//ImageHandle.compressScale(prodImg)
                    //imgTest.setImageBitmap(bitmap);// 显示图片
                    Constant constant = new Constant(getActivity());
                    constant.follow(taskID, "已关注", "", strBitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fis.close();// 关闭流
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case Constant.CODE_SELECT_IMAGE:
                if(data != null){
                    try {
                        Uri uri = data.getData();
                        String[] filePathColumns = {MediaStore.Images.Media.DATA};
                        Cursor c = getActivity().getContentResolver().query(uri , filePathColumns,null,null,null);
                        Objects.requireNonNull(c).moveToFirst();
                        int columnIndex = c.getColumnIndex(filePathColumns[0]);
                        strBitmap = c.getString(columnIndex);
                        c.close();
                    }catch (NullPointerException e){
                        Log.e("选择图片出现异常",e.getMessage());
                    }
                    if (prodImg != null)//如果不释放的话，不断取图片，将会内存不够
                        prodImg.recycle();
                    prodImg = ImageHandle.compressScale(ImageHandle.getBitmap(strBitmap));
                    if (prodImg != null) {
                        //imgTest.setImageBitmap(ImageHandle.compressScale(prodImg));
                        strBitmap = ImageHandle.bitmapToString(ImageHandle.compressScale(prodImg));//ImageHandle.compressScale(prodImg)
                        //Log.e("大小",String.valueOf(ImageHandle.getBitmapSize(ImageHandle.compressScale(prodImg))));
                        Constant constant = new Constant(getActivity());
                        constant.follow(taskID, "已关注", "", strBitmap);
                    }
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //收藏列表
    private void collectRequest() {
        ArrayList<JSONObject> data_ = new ArrayList<>();
        HashMap<String, Object> map = new HashMap<>();
        Constant constant = new Constant();
        RetrofitBuilder builder = new RetrofitBuilder(constant.BaseUrl + "/api/userCollect/");
        //builder.isConnected(MainActivity.this);
        map.put("username", Constant.username);
        map.put("action", "collectTasks");
        builder.params(map);
        builder.get();
        Call<ResponseBody> call = builder.getCall();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    //System.out.println("////////////" + response.body().string());
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    String code = jsonObject.getString("code");
                    if ("200".equals(code)) {
                        Constant.occupation = jsonObject.getString("occupation");
                        try {
                            JSONArray jsonArray = jsonObject.getJSONArray("list");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jb = jsonArray.getJSONObject(i);
                                data_.add(jb);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if ("404".equals(code)) {
                        Log.e("CollectFragment", jsonObject.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mAdapter.updateData(data_);
            }

            //请求失败时回调
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                //Toast.makeText(MainActivity.this, getBaseContext().getResources().getText(R.string.connect_to_server), Toast.LENGTH_LONG).show();
                System.out.println(throwable.getMessage());
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        linearLayout_collect.setVisibility(View.GONE);
        Constant.showSelectFlag = false;
        mAdapter.updateData(MainActivity.collectData);
    }
}
