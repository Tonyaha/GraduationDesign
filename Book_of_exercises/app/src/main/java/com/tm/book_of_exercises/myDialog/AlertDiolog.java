package com.tm.book_of_exercises.myDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.tm.book_of_exercises.R;

public class AlertDiolog {
    private Context context;
    private String title, message;
    private AlertDialog.Builder alertDialog;
    public AlertDiolog(Context context,String title,String msg){
        this.context = context;
        this.title = title;
        this.message = msg;
    }
    public void alertText() {
        alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(title)
                .setIcon(R.mipmap.icon)
                .setMessage(message)
                .setCancelable(true);
        alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        alertDialog.show();
    }
}
