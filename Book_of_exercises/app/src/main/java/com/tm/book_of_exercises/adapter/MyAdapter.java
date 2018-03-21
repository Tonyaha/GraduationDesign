package com.tm.book_of_exercises.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tm.book_of_exercises.R;

import java.util.ArrayList;

/**
 * Created by T M on 2018/3/19.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<String> mData;
    private int xmlItem; //item文件 .xml
    private int textViewId; //item中显示文字的 text
    private int imgViewId; //item 中显示图片的imageView
    /**
     * 事件回调监听
     */
    public MyAdapter.OnItemClickListener onItemClickListener;

    public MyAdapter(ArrayList<String> data,int xmlItemId,int textId,int imgId){
        this.mData = data;
        this.xmlItem = xmlItemId;
        this.imgViewId = imgId;
        this.textViewId = textId;
    }
    public void updateData(ArrayList<String> data){
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的View
        View view = LayoutInflater.from(parent.getContext()).inflate(xmlItem,parent,false); //item 布局文件xml
        //parent.removeAllViews();//去除重复数据
        // 实例化 ViewHolder
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // 绑定数据
        holder.mTv.setText(mData.get(position));
        //holder.img.setText(mData.get(position));
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
                    onItemClickListener.onItemLongClick(holder.itemView, pos);
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
        ImageView img;
        public ViewHolder(View itemView) {
            super(itemView);
            mTv = itemView.findViewById(textViewId); //(R.id.oneContact);
            img = itemView.findViewById(imgViewId);
        }
    }

    // 添加item
    public void addNewItem(){
        if(mData == null){
            mData = new ArrayList<>();
        }
        mData.add(0,"new item");
        notifyItemInserted(0);
    }

    //删除item
    public void deletItem(){
        if(mData == null || mData.isEmpty()){
            return;
        }
        mData.remove(0);
        notifyItemRemoved(0);
    }

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
        void onItemLongClick(View view, int position);
    }

}
