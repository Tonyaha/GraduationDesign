package com.tm.book_of_exercises.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tm.book_of_exercises.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 作者：T M on 2018/5/6 13:09
 * 邮箱：xxx@163.com
 */
public class SelectContactAdapter extends RecyclerView.Adapter<SelectContactAdapter.ViewHolder> {
    private ArrayList<JSONObject> mData;
    private Context context;
    private String username = "";
    /**
     * 事件回调监听
     */
    private SelectContactAdapter.OnItemClickListener onItemClickListen;

    public SelectContactAdapter(Context context, ArrayList<JSONObject> data) {
        this.mData = data;
        this.context = context;
    }

    public void updateData(ArrayList<JSONObject> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的View
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_contact, parent, false); //item 布局文件xml
        //parent.removeAllViews();//去除重复数据
        // 实例化 ViewHolder
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // 绑定数据
        JSONObject jsonObject = mData.get(position);
        try {
            username = jsonObject.getString("username");
            if ("null".equals(jsonObject.getString("remark"))) {
                holder.mTv.setText(jsonObject.getString("nickname"));
            } else {
                holder.mTv.setText(jsonObject.getString("remark"));
            }
            Glide.with(context)
                    .load(jsonObject.getString("icon"))
                    .into(holder.img);
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        holder.select.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(onItemClickListen != null) {
//                    int pos = holder.getLayoutPosition();
//                    onItemClickListen.onItemClickListener(holder.select, pos);
//                }
//            }
//        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListen != null) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListen.onItemClickListener(v, pos);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTv;
        ImageView img, select;

        public ViewHolder(View itemView) {
            super(itemView);
            mTv = itemView.findViewById(R.id.select_contact_tv); //(R.id.oneContact);
            img = itemView.findViewById(R.id.select_contact_logo);
            select = itemView.findViewById(R.id.select_contact_image);
        }
    }

    /**
     * 设置回调监听
     *
     * @param itemClickListener
     */
    public void setOnItemClickListener(SelectContactAdapter.OnItemClickListener itemClickListener){
        this.onItemClickListen = itemClickListener;
    }
    public interface OnItemClickListener{
        void onItemClickListener(View itemView,int position);
    }
}
