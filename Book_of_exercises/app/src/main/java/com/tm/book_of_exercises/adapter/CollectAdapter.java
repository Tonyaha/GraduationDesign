package com.tm.book_of_exercises.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tm.book_of_exercises.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by T M on 2018/3/28.
 */

public class CollectAdapter extends RecyclerView.Adapter<CollectAdapter.ViewHolder> {

    private ArrayList<JSONObject> mData;
    private int xmlItem; //item文件 .xml
    private int tvContextId; //item中显示文字的 text
    private int imgContextId; //item 中显示图片的imageView
    private int tvMsgId; //item中显示留言的text
    private int tvLabelId;
    private int img_collectId;
    private int tvFollowId;

    private String flag;
    /**
     * 事件回调监听
     */
    public MyOnClickListener myOnClickListener;

    private String collectFlag;


    public CollectAdapter(ArrayList<JSONObject> data, int xmlItemId, int textId, int imgId, int collect,int follow,int tvMsgId,int label,String f) {
        this.mData = data;
        this.xmlItem = xmlItemId;
        this.imgContextId = imgId;
        this.tvContextId = textId;
        this.tvMsgId = tvMsgId;
        this.tvLabelId = label;
        this.img_collectId = collect;
        this.flag = f;
        this.tvFollowId = follow;
    }

    public CollectAdapter(ArrayList<JSONObject> data, int xmlItemId, int textId, int imgId,int follow, int label,String f) {
        this.mData = data;
        this.xmlItem = xmlItemId;
        this.imgContextId = imgId;
        this.tvContextId = textId;
        this.tvLabelId = label;
        this.tvFollowId = follow;
        this.flag = f;
    }
    public void updateData(ArrayList<JSONObject> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public CollectAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的View
        View view = LayoutInflater.from(parent.getContext()).inflate(xmlItem, parent, false); //item 布局文件xml
        //parent.removeAllViews();//去除重复数据
        // 实例化 ViewHolder
        CollectAdapter.ViewHolder viewHolder = new CollectAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CollectAdapter.ViewHolder holder, int position) {
        // 绑定数据
        JSONObject jsonObject = mData.get(position);
        if("all".equals(flag)){
            try {
                collectFlag = jsonObject.getString("collectFlag");
                if("true".equals(collectFlag)){
                    holder.mTvLabel.setText("#收藏习题#");
                    holder.mTvFollow.setTextColor(Color.parseColor("#FC2828"));
                    holder.mTvFollow.setText("已关注");
                }else {
                    holder.mTvLabel.setText("#未收藏习题#");
                    holder.mTvFollow.setText("关  注");
                }
                holder.mTv.setText( (position+1) + "、" +jsonObject.getString("context"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            holder.mTvFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(myOnClickListener != null){
                        int pos = holder.getLayoutPosition();
                        myOnClickListener.onViewClickListener(holder.mTvFollow,pos);
                        if("true".equals(collectFlag)){
                            holder.mTvFollow.setTextColor(Color.parseColor("#fc1e2327"));
                            holder.mTvFollow.setText("关  注");
                            collectFlag = "false";
                        }else {
                            holder.mTvFollow.setTextColor(Color.parseColor("#FC2828"));
                            holder.mTvFollow.setText("已关注");
                            collectFlag = "true";
                        }
                    }
                }
            });
        }else if("collect".equals(flag)){
            try {
                collectFlag = jsonObject.getString("collectFlag");
                if("true".equals(collectFlag)){
                    holder.imgCollect.setImageResource(R.mipmap.task_cllect_true);
                    holder.mTvLabel.setText("#收藏习题#");
                    holder.mTvFollow.setTextColor(Color.parseColor("#FC2828"));
                    holder.mTvFollow.setText("已关注");
                }else {
                    holder.imgCollect.setImageResource(R.mipmap.task_cllect);
                    holder.mTvLabel.setText("#未收藏习题#");
                    holder.mTvFollow.setText("关  注");
                }
                holder.mTv.setText( (position+1) + "、" +jsonObject.getString("context"));
                holder.mTvMsg.setText(jsonObject.getString("msg"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            holder.imgCollect.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if(myOnClickListener != null){
//                        int pos = holder.getLayoutPosition();
//                        myOnClickListener.onViewClickListener(holder.imgCollect,pos);
//                        if("true".equals(collectFlag)){
//                            holder.imgCollect.setImageResource(R.mipmap.task_cllect);
//                            collectFlag = "false";
//                        }else {
//                            holder.imgCollect.setImageResource(R.mipmap.task_cllect_true);
//                            collectFlag = "true";
//                        }
//
//                    }
//                }
//            });
            holder.imgMsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(myOnClickListener != null){
                        int pos = holder.getLayoutPosition();
                        myOnClickListener.onViewClickListener(holder.imgMsg,pos);
                    }
                }
            });
            holder.mTvFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(myOnClickListener != null){
                        if("true".equals(collectFlag)){
                            holder.mTvFollow.setTextColor(Color.parseColor("#fc1e2327"));
                            holder.mTvFollow.setText("关  注");
                            holder.imgCollect.setImageResource(R.mipmap.task_cllect);
                            collectFlag = "false";
                        }else {
                            holder.mTvFollow.setTextColor(Color.parseColor("#FC2828"));
                            holder.mTvFollow.setText("已关注");
                            holder.imgCollect.setImageResource(R.mipmap.task_cllect_true);
                            collectFlag = "true";
                        }
                        int pos = holder.getLayoutPosition();
                        myOnClickListener.onViewClickListener(holder.mTvFollow,pos);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTv;
        ImageView mImg;
        TextView mTvMsg;
        TextView mTvFollow;
        ImageView imgCollect;
        ImageView imgMsg;
        TextView mTvLabel;

        public ViewHolder(View itemView) {
            super(itemView);
            mTv = itemView.findViewById(tvContextId); //(R.id.oneContact);
            mImg = itemView.findViewById(imgContextId);
            mTvMsg = itemView.findViewById(tvMsgId);
            mTvFollow = itemView.findViewById(tvFollowId) ;
            imgCollect = itemView.findViewById(img_collectId); //收藏按钮
            imgMsg = itemView.findViewById(R.id.img_msg);  //留言按钮
            mTvLabel = itemView.findViewById(tvLabelId);
        }
    }

    /**
     * 设置回调监听
     *
     * @param listener
     */

    public void mySetOnClickListener(MyOnClickListener listener){
        this.myOnClickListener = listener;
    }
    public interface MyOnClickListener {
        void onViewClickListener(View view,int position);
    }



//    // 添加item
//    public void addNewItem() {
//        if (mData == null) {
//            mData = new ArrayList<>();
//        }
//        mData.add(0, "");
//        notifyItemInserted(0);
//    }
//
//    //删除item
//    public void deletItem() {
//        if (mData == null || mData.isEmpty()) {
//            return;
//        }
//        mData.remove(0);
//        notifyItemRemoved(0);
//    }

}
