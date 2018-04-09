package com.tm.book_of_exercises.main.mainPage;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.ocr.ui.camera.CameraActivity;
import com.tm.book_of_exercises.R;
import com.tm.book_of_exercises.constant.Constant;
import com.tm.book_of_exercises.http.RetrofitBuilder;
import com.tm.book_of_exercises.main.bean.ImageHandle;
import com.tm.book_of_exercises.orc.FileUtil;
import com.tm.book_of_exercises.orc.RecognizeService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tm.book_of_exercises.main.bean.ImageHandle.bitmapToString;

/**
 * Created by T M on 2018/3/14.
 */

public class AddFragment extends Fragment implements View.OnClickListener {
    private TextView tv_notice;
    private ImageView selectImg;
    private EditText et_content;
    private Button btn_next, btn_save, btn_send, btn_extract, btn_addImg;

    private Bitmap prodImg;
    private String strBitmap = "null";
    private static final int REQUEST_CODE_ACCURATE_BASIC = 107;
    private String TAG = "AddFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        tv_notice = view.findViewById(R.id.tv_notice);
        tv_notice.setSelected(true);

        btn_addImg = view.findViewById(R.id.addImg);
        btn_extract = view.findViewById(R.id.extract);
        selectImg = view.findViewById(R.id.selectImage);
        et_content = view.findViewById(R.id.selectTv);
        btn_next = view.findViewById(R.id.next);
        btn_save = view.findViewById(R.id.save);
        btn_send = view.findViewById(R.id.send);

        btn_extract.setOnClickListener(this);
        selectImg.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        btn_send.setOnClickListener(this);
        btn_addImg.setOnClickListener(this);
        return view;//super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.extract:
                Intent intent = new Intent(getActivity(), CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, FileUtil.getSaveFile(getActivity()).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_GENERAL);
                startActivityForResult(intent, REQUEST_CODE_ACCURATE_BASIC);
                break;
            case R.id.selectImage:
                break;
            case R.id.save:
                if (!("").equals(et_content.getText().toString().trim()) | !("null").equals(strBitmap)) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                    alertDialog.setTitle("显示用户")
                            .setIcon(R.mipmap.icon)
                            .setMessage("是否显示您的账户信息？")
                            .setCancelable(true);

                    alertDialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            saveTasks("true");
                            dialogInterface.cancel();
                        }
                    });
                    alertDialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            saveTasks("false");
                            dialogInterface.cancel();
                        }
                    });
                    alertDialog.show();
                } else {
                    Toast.makeText(getActivity(), "你并没有录入习题", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.next:
                et_content.setText("");
                selectImg.setImageResource(R.mipmap.icon);
                break;
            case R.id.send:
                break;
            case R.id.addImg:
                //ImageHandle.pickAlbum(getActivity(), Constant.CODE_SELECT_IMAGE);
                Intent intent1 = new Intent(Intent.ACTION_PICK);
                intent1.setType("image/*");
                startActivityForResult(intent1, Constant.CODE_SELECT_IMAGE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_ACCURATE_BASIC:
                if (resultCode == Activity.RESULT_OK) {
                    RecognizeService.recAccurateBasic(FileUtil.getSaveFile(getActivity()).getAbsolutePath(),
                            new RecognizeService.ServiceListener() {
                                @Override
                                public void onResult(String result) {
                                    try {
                                        String resultText = "";
                                        if (!et_content.getText().toString().isEmpty()) {
                                            resultText = et_content.getText().toString();
                                        }
                                        JSONObject jsonObject = new JSONObject(result);
                                        JSONArray jsonArray = jsonObject.getJSONArray("words_result");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jb = jsonArray.getJSONObject(i);
                                            resultText += jb.getString("words");
                                        }
                                        et_content.setText(resultText);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                }
                break;
            case Constant.CODE_SELECT_IMAGE:
                if (data != null) {
                    String imagePath = null;//String.valueOf(getActivity().getResources().getDrawable(R.mipmap.icon));
                    try {
                        Uri uri = data.getData();
                        String[] filePathColumns = {MediaStore.Images.Media.DATA};
                        Cursor c = getActivity().getContentResolver().query(uri , filePathColumns,null,null,null);
                        Objects.requireNonNull(c).moveToFirst();
                        int columnIndex = c.getColumnIndex(filePathColumns[0]);
                        imagePath = c.getString(columnIndex);
                        c.close();
                    }catch (NullPointerException e){
                        Log.e("选择图片出现异常",e.getMessage());
                    }
                    if (prodImg != null)//如果不释放的话，不断取图片，将会内存不够
                        prodImg.recycle();
                    prodImg = ImageHandle.getBitmap(imagePath);
                    if (prodImg != null) {
                        selectImg.setImageBitmap(prodImg);
                        strBitmap = bitmapToString(ImageHandle.compressScale(prodImg));//ImageHandle.compressScale(prodImg)
                    }
                }
                break;
        }
    }

    private void saveTasks(String tag) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("username", Constant.username);
        map.put("action", "save");
        map.put("content", et_content.getText().toString());
        map.put("image", strBitmap);
        map.put("tag", tag);
        RetrofitBuilder builder = new RetrofitBuilder(new Constant().BaseUrl + "/api/save/");
        builder.isConnected(getActivity());
        builder.params(map);
        builder.post();
        Call<ResponseBody> call = builder.getCall();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    Toast.makeText(getActivity(),jsonObject.getString("msg"),Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //请求失败时回调
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Toast.makeText(getActivity(), getResources().getText(R.string.connect_to_server), Toast.LENGTH_LONG).show();
                System.out.println(throwable.getMessage());
            }
        });
    }
}