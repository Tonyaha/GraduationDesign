package com.tm.book_of_exercises.main.mainPage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tm.book_of_exercises.R;

/**
 * Created by T M on 2018/3/14.
 */

public class AddFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add,container,false);
        return view;//super.onCreateView(inflater, container, savedInstanceState);
    }
}