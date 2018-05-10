package com.tm.book_of_exercises.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tm.book_of_exercises.R;
import com.tm.book_of_exercises.constant.Constant;
import com.tm.book_of_exercises.main.bean.ImageHandle;
import com.tm.book_of_exercises.main.otherPage.ReceivedTasksActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2018/5/6.
 */

public class ReceivedAdapter extends RecyclerView.Adapter<ReceivedAdapter.ViewHolder> {
    private JSONArray mData;
    private Context context;
    /**
     * 事件回调监听
     */
    public OnItemLongClickListener onItemLongClickListener; //接口
    public OnClickListener onClickListener;

    public ReceivedAdapter(Context context, JSONArray data){
        this.mData = data;
        this.context = context;
    }
    public void updateData(JSONArray data){
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的View
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_received,parent,false); //item 布局文件xml
        //parent.removeAllViews();//去除重复数据
        // 实例化 ViewHolder
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(Constant.showWriteAnswerFlag && ReceivedTasksActivity.pos == position){
            holder.answerImg.setVisibility(View.VISIBLE);
            Bitmap bm = ImageHandle.compressScale(ImageHandle.stringToImage(ReceivedTasksActivity.strAnswer));
            holder.answerImg.setImageBitmap(bm);
        }
        JSONObject jb = null;
        if(mData.length() > 0){
            try {
                jb = mData.getJSONObject(position);
                String answer = jb.getString("answer");
                String myAnswer = jb.getString("myAnswer");
                String occupation = jb.getString("occupation");
                String contextImg = jb.getString("imgUri");
                //Log.e("aaaa",contextImg);
                holder.mTv.setText(String.valueOf(position+1)  + "、" + jb.getString("content"));
                if(Constant.showAnswerFlag && ReceivedTasksActivity.pos == position){
                    holder.answerImg.setVisibility(View.VISIBLE);
                    if ("教师".equals(occupation)){ //如果发送者是老师，展示学生的答案
                        if ("".equals(answer) || "null".equals(answer) || answer == null){
                            Toast.makeText(context,"暂没正确答案",Toast.LENGTH_LONG).show();
                        }else {
                            Glide.with(context)
                                    .load(answer)
                                    .into(holder.answerImg);
                        }
                    }else {
                        if ("".equals(myAnswer) || "null".equals(myAnswer) || myAnswer == null){
                            Toast.makeText(context,"学生暂没书写答案",Toast.LENGTH_LONG).show();
                        }else {
                            Glide.with(context)
                                    .load(myAnswer)
                                    .into(holder.answerImg);
                        }
                    }

                }else {
                    holder.answerImg.setVisibility(View.GONE);
                }
                //holder.answerImg.setText("");//jb.getString("answer")
                //Glide.with(context).load(jb)
                Glide.with(context)
                        .load(contextImg)
                        .into(holder.img);
                if(Constant.showSelectFlag){
                    holder.linearLayout.setVisibility(View.VISIBLE);
                }else {
                    holder.linearLayout.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                if(onItemLongClickListener != null) {
//                    //int pos = holder.getLayoutPosition();
//                    onItemLongClickListener.onItemLongClick(holder.itemView, mData);
//                }
//                //表示此事件已经消费，不会触发单击事件
//                return true;
//            }
//        });

        holder.seeAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickListener != null) {
                    int pos = holder.getLayoutPosition();
                    onClickListener.onClickListener(holder.seeAnswer, pos);
                }
            }
        });
        holder.writeAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickListener != null) {
                    int pos = holder.getLayoutPosition();
                    onClickListener.onClickListener(holder.writeAnswer, pos);
                }
            }
        });
        holder.collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickListener != null) {
                    int pos = holder.getLayoutPosition();
                    onClickListener.onClickListener(holder.collect, pos);
                }
            }
        });
        holder.receivedSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickListener != null) {
                    int pos = holder.getLayoutPosition();
                    onClickListener.onClickListener(holder.receivedSelect, pos);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTv;
        ImageView img,seeAnswer,writeAnswer,collect,receivedSelect,answerImg, writeAnswerImg;
        LinearLayout linearLayout ;
        public ViewHolder(View itemView) {
            super(itemView);
            mTv = itemView.findViewById(R.id.received_task_tv); // 文字
            img = itemView.findViewById(R.id.received_tasks_img); // 图片
            seeAnswer = itemView.findViewById(R.id.see_received_answer);
            writeAnswer = itemView.findViewById(R.id.received_write_answer);
            collect = itemView.findViewById(R.id.received_collect);
            receivedSelect = itemView.findViewById(R.id.received_select);
            linearLayout = itemView.findViewById(R.id.linearLayout_received);
            answerImg = itemView.findViewById(R.id.receivid_answer_img);
        }
    }

    /**
     * 设置回调监听
     *
     * @param listener
     */
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }

    public void setOnClickListener(OnClickListener clickListener){
        this.onClickListener = clickListener;
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, JSONArray mData);
    }

    public interface OnClickListener{
        void onClickListener(View view, int position);
    }
}
