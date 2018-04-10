package com.tm.book_of_exercises.tools;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.tm.book_of_exercises.R;

public class PopWindowUtil {
    private static PopWindowUtil instance;
    private PopupWindow mPopupWindow;
    private OnDissmissListener mListener;

    // 私有化构造方法，变成单例模式
    private PopWindowUtil() {
    }

    // 对外提供一个该类的实例，考虑多线程问题，进行同步操作
    public static PopWindowUtil getInstance() {
        if (instance == null) {
            synchronized (PopWindowUtil.class) {
                if (instance == null) {
                    instance = new PopWindowUtil();
                }
            }
        }
        return instance;
    }

    /**
     * @param cx    activity
     * @param view  传入需要显示在什么控件下
     * @param view1 传入内容的view
     * @return
     */
    public PopWindowUtil makePopupWindow(Context cx, View view, View view1, int color) {
        LayoutInflater inflater = LayoutInflater.from(cx);
        View view2 = inflater.inflate(R.layout.popupwindow_add, null);

        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wmManager=(WindowManager) cx.getSystemService(Context.WINDOW_SERVICE);
        wmManager.getDefaultDisplay().getMetrics(dm);
        int hight = dm.heightPixels;
        mPopupWindow = new PopupWindow(cx);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(color));
        //view1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));

        // 设置PopupWindow的大小（宽度和高度）
        mPopupWindow.setWidth(view.getWidth());
        mPopupWindow.setHeight((hight - view.getBottom()) *2 / 3);
        mPopupWindow.setContentView(view1);  // 设置PopupWindow的内容view
        mPopupWindow.setFocusable(true);  // 设置PopupWindow可获得焦点
        mPopupWindow.setTouchable(true);  // 设置PopupWindow可触摸
        mPopupWindow.setOutsideTouchable(true);  // 设置非PopupWindow区域可触摸
        return instance;
    }

    /**
     * @param cx 此处必须为Activity的实例
     * @param view 显示在该控件之下
     * @param xOff 距离view的x轴偏移量
     * @param yOff 距离view的y轴偏移量
     * @param anim 弹出及消失动画
     * @return
    **/
    public PopupWindow showLocationWithAnimation(final Context cx,View view,int xOff,int yOff,int anim){
        //弹出动画
        mPopupWindow.setAnimationStyle(anim);
        //弹出PopupWindow时让后面的界面变暗
        WindowManager.LayoutParams params = ((Activity) cx).getWindow().getAttributes();
        params.alpha = 0.5f;

        int[] position = new int[2];
        view.getLocationOnScreen(position);

        //弹窗出现的位置，在指定view之下
        mPopupWindow.showAsDropDown(view,position[0] + xOff,position[1] + yOff);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // PopupWindow 消失后让界面变亮
                WindowManager.LayoutParams params1 = ((Activity) cx).getWindow().getAttributes();
                params1.alpha = 1.0f;
                ((Activity) cx).getWindow().setAttributes(params1);

                if (mListener != null) {
                    mListener.dismiss();
                }
            }
        });
        return mPopupWindow;
    }

    public interface OnDissmissListener{
        void dismiss();
    }
    public void setOnDissmissListener(OnDissmissListener listener){
        mListener = listener;
    }
}
