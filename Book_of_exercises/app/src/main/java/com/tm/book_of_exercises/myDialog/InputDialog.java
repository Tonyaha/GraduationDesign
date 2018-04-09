package com.tm.book_of_exercises.myDialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.tm.book_of_exercises.R;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxDialogFragment;  //才能用.show()

import java.util.concurrent.TimeUnit;

/**
 * Created by T M on 2018/3/31.
 * DialogFragment是在Android3.0的时候被引入的，从其名字可以很直观的看出它是一种基于Fragment的Dialog，可以用来创建对话框，它是用来替代Dialog的。
 */

public class InputDialog extends RxDialogFragment {
    private Dialog dialog;
    private InputDialogInterface observer;
    private String title;
    private String hint;
    private String msg;

    private TextInputEditText editTextInput;

    /**
     * 初始化.必须调用一次
     * @param title: 标题
     * @param hint: 提示
     * @param observer: 观察者对象
     */
    public void init(String title, String hint,String msg, InputDialogInterface observer) {
        this.title = title;
        this.hint = hint;
        this.observer = observer;
        this.msg = msg;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new Dialog(getActivity(), R.style.PopupDialog);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_input, null);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);

        TextView textViewTitle = view.findViewById(R.id.title);
        Button buttonCancel = view.findViewById(R.id.cancel);
        Button buttonOK = view.findViewById(R.id.ok);
        editTextInput =  view.findViewById(R.id.input);
        editTextInput.setText(msg);
        textViewTitle.setText(title);
        editTextInput.setHint(hint);

        RxView.clicks(buttonCancel)
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(v -> dialog.cancel());

        RxView.clicks(buttonOK)
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(v -> observer.onClick());
//                .subscribe(v -> {
//                    InputDialogInterface.FilterResult filterResult = observer.filter(editTextInput.getText().toString());
//                    if (filterResult == null || filterResult.result) {
//                        dialog.cancel();
//                        observer.onClick();
//                    } else {
//                        editTextInput.setError(filterResult.errorHint);
//                        editTextInput.requestFocus();
//                    }
//                });

        return dialog;
    }

    /**
     * 得到输入内容
     * @return 输入内容
     */
    public String getInput() {
        return editTextInput.getText().toString();
    }

}
