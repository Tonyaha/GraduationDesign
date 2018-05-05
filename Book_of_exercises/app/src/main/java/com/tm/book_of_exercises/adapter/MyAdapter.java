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
import com.tm.book_of_exercises.constant.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by T M on 2018/3/19.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<JSONObject> mData;
    private Context context;
    /**
     * 事件回调监听
     */
    public MyAdapter.OnItemClickListener onItemClickListener;

    public MyAdapter(Context context,ArrayList<JSONObject> data){
        this.mData = data;
        this.context = context;
    }
    public void updateData(ArrayList<JSONObject> data){
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的View
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact,parent,false); //item 布局文件xml
        //parent.removeAllViews();//去除重复数据
        // 实例化 ViewHolder
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(Constant.showSelectFlag){
            holder.select.setVisibility(View.VISIBLE);
        }else {
            holder.select.setVisibility(View.GONE);
        }
        // 绑定数据
        JSONObject jsonObject = mData.get(position);
        try {
            if("null".equals(jsonObject.getString("remark"))){
                holder.mTv.setText(jsonObject.getString("nickname"));
            }else {
                holder.mTv.setText(jsonObject.getString("remark"));
            }
            Glide.with(context)
                    .load(jsonObject.getString("icon"))
                    .into(holder.img);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //holder.img.setText(mData.get(position));
        holder.select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.select, pos);
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(onItemClickListener != null) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView, pos);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(onItemClickListener != null) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemLongClick(holder.itemView, mData);
                }
                //表示此事件已经消费，不会触发单击事件
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTv;
        ImageView img,select;
        public ViewHolder(View itemView) {
            super(itemView);
            mTv = itemView.findViewById(R.id.oneContact); //(R.id.oneContact);
            img = itemView.findViewById(R.id.oneContactImg);
            select = itemView.findViewById(R.id.select_contact);
        }
    }

//    // 添加item
//    public void addNewItem(){
//        if(mData == null){
//            mData = new ArrayList<>();
//        }
//        mData.add(0,"new item");
//        notifyItemInserted(0);
//    }
//
//    //删除item
//    public void deletItem(){
//        if(mData == null || mData.isEmpty()){
//            return;
//        }
//        mData.remove(0);
//        notifyItemRemoved(0);
//    }

    /**
     * 设置回调监听
     *
     * @param listener
     */
    public void setOnItemClickListener(MyAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, ArrayList<JSONObject> mData);
    }

}
