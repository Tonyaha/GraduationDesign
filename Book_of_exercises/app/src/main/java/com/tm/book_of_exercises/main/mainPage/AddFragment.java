package com.tm.book_of_exercises.main.mainPage;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.tm.book_of_exercises.main.bean.ImageHandle.bitmapToString;

/**
 * Created by T M on 2018/3/14.
 */

public class AddFragment extends Fragment implements View.OnClickListener,RadioGroup.OnCheckedChangeListener{
    private TextView tv_notice;
    private ImageView selectImg;
    private EditText et_content;
    private FloatingActionButton floatButton;
    private RadioButton btn_next, btn_save, btn_send, btn_extract, btn_addImg;
    private RadioGroup radioGroup;

    private Bitmap prodImg;
    private String strBitmap = "null";
    private static final int REQUEST_CODE_ACCURATE_BASIC = 107;
    private String TAG = "AddFragment";
    private boolean floatButton_Opened = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        tv_notice = view.findViewById(R.id.tv_notice);
        tv_notice.setSelected(true);

        selectImg = view.findViewById(R.id.selectImage);
        et_content = view.findViewById(R.id.selectTv);
        floatButton = view.findViewById(R.id.btnSuspension);


        floatButton.setOnClickListener(this) ;

//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                int viewId = radioGroup.getCheckedRadioButtonId(); //通过 group 找对应按钮 id
//                switch (viewId) {
//                    case R.id.extract:
//                        Intent intent = new Intent(getActivity(), CameraActivity.class);
//                        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, FileUtil.getSaveFile(getActivity()).getAbsolutePath());
//                        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_GENERAL);
//                        startActivityForResult(intent, REQUEST_CODE_ACCURATE_BASIC);
//                        break;
//                    case R.id.send:
//                        break;
//                    case R.id.save:
//                        if (!("").equals(et_content.getText().toString().trim()) | !("null").equals(strBitmap)) {
//                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//                            alertDialog.setTitle("显示用户")
//                                    .setIcon(R.mipmap.icon)
//                                    .setMessage("是否显示您的账户信息？")
//                                    .setCancelable(true);
//
//                            alertDialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    saveTasks("true");
//                                    dialogInterface.cancel();
//                                }
//                            });
//                            alertDialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    saveTasks("false");
//                                    dialogInterface.cancel();
//                                }
//                            });
//                            alertDialog.show();
//                        } else {
//                            Toast.makeText(getActivity(), "你并没有录入习题", Toast.LENGTH_LONG).show();
//                        }
//                        break;
//                    case R.id.next:
//                        et_content.setText("");
//                        selectImg.setImageResource(R.mipmap.icon);
//                        break;
//                    case R.id.addImg:
//                        //ImageHandle.pickAlbum(getActivity(), Constant.CODE_SELECT_IMAGE);
//                        Intent intent1 = new Intent(Intent.ACTION_PICK);
//                        intent1.setType("image/*");
//                        startActivityForResult(intent1, Constant.CODE_SELECT_IMAGE);
//                        break;
//                }
//            }
//        });

        return view;//super.onCreateView(inflater, container, savedInstanceState);
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

    @Override
    public void onClick(View view) {
        assert floatButton !=null;
        switch (view.getId()){
            case R.id.btnSuspension:
                createPopupWindow(view);
                if(!floatButton_Opened){
                    openMenu(view);
                }else {
                    closeMenu(view);
                }
                break;
        }
    }

    private void createPopupWindow(View view) {
        // 构建一个popupwindow的布局
        LayoutInflater mLayoutInflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup popupView = (ViewGroup) mLayoutInflater.inflate(R.layout.popupwindow_add, null, true);

        radioGroup = popupView.findViewById(R.id.add_menu_group);
        radioGroup.setOnCheckedChangeListener(this);
        //  创建PopupWindow对象，指定宽度和高度
        PopupWindow window = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // 设置动画
        window.setAnimationStyle(R.style.PopupDialog);
        // 设置背景颜色
        window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));//#00000000
        // 设置可以获取焦点
        window.setFocusable(true);
        // 设置可以触摸弹出框以外的区域
        window.setOutsideTouchable(true);
        //更新popupwindow的状态
        window.update();
        // 以下拉的方式显示，并且可以设置显示的位置
        //window.showAsDropDown(floatButton, 0, 20);
        //parent:传你当前Layout的id;  gravity:Gravity.BOTTOM（以屏幕左下角为参照）... 偏移量会以它为基准点 当x y为0,0是出现在底部居中
        int screenWidth = getActivity().getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getActivity().getResources().getDisplayMetrics().heightPixels; //负的向上移
        //设置Gravity.NO_GRAVITY的话，就相对屏幕左上角作为参照(即原点[0,0]是屏幕左上角) 其中view设置哪个都一样(意思是，不论设置哪个，popwindow的相对参照物都是整个屏幕的根布局)
        window.showAtLocation(floatButton, Gravity.NO_GRAVITY,screenWidth, screenHeight/2);
        backgroundAlpha(getActivity(), 0.8f);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                closeMenu(view);
                backgroundAlpha(getActivity(), 1.0f);
            }
        });
    }

    private void closeMenu(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view,"rotation",-135,20,0);
        animator.setDuration(500);
        animator.start();
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.7f,0);
        alphaAnimation.setDuration(500);
        //tv_xxx.startAnimation(alphaAnimation);
        //tv_xxx.setVisibility(View.INVISIBLE);
        floatButton_Opened = false;
    }

    private void openMenu(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view,"rotation",0,-155,-135);
        animator.setDuration(500);
        animator.start();
        //tv_xxx.setVisibility(View.VISIBLE);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0,0.7f);
        alphaAnimation.setDuration(500);
        alphaAnimation.setFillAfter(true);
        //tv_xxx.startAnimation(alphaAnimation);
        floatButton_Opened = true;
    }

    //设置添加屏幕的背景透明度
    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        int buttonId = radioGroup.getCheckedRadioButtonId(); //通过 group 找对应按钮 id
        switch (buttonId){
            case R.id.extract:
                Intent intent = new Intent(getActivity(), CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, FileUtil.getSaveFile(getActivity()).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_GENERAL);
                startActivityForResult(intent, REQUEST_CODE_ACCURATE_BASIC);
                break;
            case R.id.send:
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
                break;
            case R.id.addImg:
                //ImageHandle.pickAlbum(getActivity(), Constant.CODE_SELECT_IMAGE);
                Intent intent1 = new Intent(Intent.ACTION_PICK);
                intent1.setType("image/*");
                startActivityForResult(intent1, Constant.CODE_SELECT_IMAGE);
                break;
        }
    }
}