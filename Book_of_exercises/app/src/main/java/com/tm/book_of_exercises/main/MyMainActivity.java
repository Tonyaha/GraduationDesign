package com.tm.book_of_exercises.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tm.book_of_exercises.MainActivity;
import com.tm.book_of_exercises.R;
import com.tm.book_of_exercises.main.mainPage.ContactFragment;
import com.tm.book_of_exercises.main.mainPage.CollectFragment;
import com.tm.book_of_exercises.main.mainPage.InterestFragment;
import com.tm.book_of_exercises.main.mainPage.MeFragment;
import com.tm.book_of_exercises.main.mainPage.MsgFragment;
import com.tm.book_of_exercises.main.otherPage.SearchActivity;
import com.tm.book_of_exercises.main.otherPage.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by T M on 2018/3/14.
 */

public class MyMainActivity extends Fragment {
    private View view;
    private List<Fragment> fragments;
    private RadioGroup radioGroup;
    private ViewPager viewPager;

    private ImageView imageView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_mymain, container, false);
        fragments = new ArrayList<>();

        imageView = view.findViewById(R.id.main_activity_title).findViewById(R.id.user_search);
        imageView.setOnClickListener(view1 -> searchButton());

        MsgFragment msgFragment = new MsgFragment();// 顺序对界面有影响
        ContactFragment contactFragment = new ContactFragment();
        CollectFragment foundFragment = new CollectFragment();
        InterestFragment interestFragment = new InterestFragment();
        MeFragment meFragment = new MeFragment();

        fragments.add(msgFragment);
        fragments.add(contactFragment);
        fragments.add(foundFragment);
        fragments.add(interestFragment);
        fragments.add(meFragment);

        viewPager = view.findViewById(R.id.viewPage);
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });

        initMenuItem();

        /* 将每个page与底部的menu绑定 */
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            /* position是在界面滑动到的页面的 下标 编号 */
            @Override
            public void onPageSelected(int position) {
                RadioButton radioButton = (RadioButton)radioGroup.getChildAt(position);
                radioButton.setChecked(true);
//                if(position == 1){
//                    new MainActivity().getFriendList();
//                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return view;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void searchButton() {
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        startActivity(intent);
    }

    private void initMenuItem() {
        radioGroup =  view.findViewById(R.id.mymain_radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int index = group.getCheckedRadioButtonId(); //通过 group 找对应按钮 id
                RadioButton rb = view.findViewById(index);
                rb.setChecked(true);

                for(int i=0;i<group.getChildCount();i++){
                    RadioButton rb_01 = (RadioButton) group.getChildAt(i);
                    if(rb_01.isChecked()){
                        viewPager.setCurrentItem(i);
                        //Toast.makeText(getActivity(),"第 "+(i+1)+"个界面",Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }
        });
    }

}
