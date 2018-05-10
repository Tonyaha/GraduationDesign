package com.tm.book_of_exercises.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import com.tm.book_of_exercises.main.mainPage.CollectFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by T M on 2018/3/28.
 */

public class CollectAdapter extends RecyclerView.Adapter<CollectAdapter.ViewHolder> {
    private Context context;
    private ArrayList<JSONObject> mData;
    /**
     * 事件回调监听
     */
    private MyOnClickListener myOnClickListener;
    private OnItemLongClickListener onItemLongClickListen;
    private String collectFlag = "false";

    public CollectAdapter(Context context, ArrayList<JSONObject> data) {
        this.mData = data;
        this.context = context;
    }

    public void updateData(ArrayList<JSONObject> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        // 实例化展示的View
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_collect_tasks, parent, false); //item 布局文件xml
        //parent.removeAllViews();//去除重复数据
        // 实例化 ViewHolder
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        // 绑定数据
        JSONObject jsonObject = mData.get(position);
        if(Constant.showSelectFlag){
            holder.linearLayout.setVisibility(View.VISIBLE);

        }else {
            holder.linearLayout.setVisibility(View.GONE);
        }
        if(Constant.showWriteAnswerFlag && CollectFragment.pos == position){
            //Toast.makeText(context,"写答案",Toast.LENGTH_LONG).show();
            holder.showAnswer.setVisibility(View.VISIBLE);
//            Glide.with(context)
//                    .load("http://www.ghost64.com/qqtupian/qqTxImg/2/1531534221-6.jpg")
//                    .into(holder.showAnswer);
            holder.showAnswer.setImageBitmap(ImageHandle.stringToImage(CollectFragment.strBitmap));
        }
        try {
            collectFlag = jsonObject.getString("collectFlag");
            String urlpath = jsonObject.getString("contextImg");
            String content = "  " + (position + 1) + "、" + jsonObject.getString("context").replace("\\n", "\n");
            String answer = jsonObject.getString("answer");
            holder.mTv.setText(content);
            if (!"null".equals(jsonObject.getString("contextImg"))) {
                Glide.with(context)
                        .load(urlpath)
                        .into(holder.mImg);
            }
            if (!"null".equals(jsonObject.getString("msg")) || !"".equals(jsonObject.getString("msg"))) {
                holder.mTvMsg.setText(jsonObject.getString("msg"));
            }

            if(Constant.showAnswerFlag && CollectFragment.pos == position){
                if(!"".equals(answer)){
                    holder.showAnswer.setVisibility(View.VISIBLE);
                    Glide.with(context)
                            .load(answer)
                            .into(holder.showAnswer);
                }else {
                    Toast.makeText(context,"暂没有答案",Toast.LENGTH_LONG).show();
                }
            }else if(!Constant.showAnswerFlag && CollectFragment.pos == position) {
                holder.showAnswer.setVisibility(View.GONE);
            }

            if ("true".equals(collectFlag)) {
                //holder.imgCollect.setImageResource(R.mipmap.task_cllect_true);
                holder.mTvLabel.setText("#收藏习题#");
                holder.mTvFollow.setTextColor(Color.parseColor("#FC2828"));
                holder.mTvFollow.setText("已关注");
            } else {
                //holder.imgCollect.setImageResource(R.mipmap.task_cllect);
                holder.mTvLabel.setText("#未收藏习题#");
                holder.mTvFollow.setText("关  注");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.mSelectTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myOnClickListener != null) {
                    int pos = holder.getLayoutPosition();
                    myOnClickListener.onViewClickListener(holder.mSelectTasks, pos);
                }
            }
        });
        holder.imgMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myOnClickListener != null) {
                    int pos = holder.getLayoutPosition();
                    myOnClickListener.onViewClickListener(holder.imgMsg, pos);
                }
            }
        });
        holder.mTvFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myOnClickListener != null) {
                    if ("true".equals(collectFlag)) {
                        holder.mTvFollow.setTextColor(Color.parseColor("#fc1e2327"));
                        holder.mTvFollow.setText("关  注");
                        //holder.imgCollect.setImageResource(R.mipmap.task_cllect);
                        collectFlag = "false";
                    } else {
                        holder.mTvFollow.setTextColor(Color.parseColor("#FC2828"));
                        holder.mTvFollow.setText("已关注");
                        //holder.imgCollect.setImageResource(R.mipmap.task_cllect_true);
                        collectFlag = "true";
                    }
                    int pos = holder.getLayoutPosition();
                    myOnClickListener.onViewClickListener(holder.mTvFollow, pos);
                }
            }
        });
        holder.btnShowAnswerImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myOnClickListener != null) {
                    int pos = holder.getLayoutPosition();
                    myOnClickListener.onViewClickListener(holder.btnShowAnswerImg, pos);
                }
            }
        });
        holder.btnWriteAnswerImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myOnClickListener != null) {
                    int pos = holder.getLayoutPosition();
                    myOnClickListener.onViewClickListener(holder.btnWriteAnswerImg, pos);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(onItemLongClickListen != null){
                    int pos = holder.getLayoutPosition();
                    onItemLongClickListen.onItemLongClickListener(holder.itemView,pos);
                }
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
        ImageView mImg,mSelectTasks;
        TextView mTvMsg;
        TextView mTvFollow;
        ImageView imgMsg;
        TextView mTvLabel;
        LinearLayout linearLayout;

        ImageView btnWriteAnswerImg,btnShowAnswerImg,showAnswer;


        public ViewHolder(View itemView) {
            super(itemView);
            mTv = itemView.findViewById(R.id.tv_task); //(R.id.oneContact);
            mImg = itemView.findViewById(R.id.task_img);
            mTvMsg = itemView.findViewById(R.id.tv_msg);
            mTvFollow = itemView.findViewById(R.id.tasks_follow);
            imgMsg = itemView.findViewById(R.id.img_msg);  //留言按钮
            mTvLabel = itemView.findViewById(R.id.tasks__label);
            mSelectTasks = itemView.findViewById(R.id.collect_select_tastk);
            linearLayout = itemView.findViewById(R.id.linearLayout_collect);

            btnWriteAnswerImg = itemView.findViewById(R.id.collect_write_answer);
            btnShowAnswerImg = itemView.findViewById(R.id.tasks_see_answer);//查看答案
            showAnswer = itemView.findViewById(R.id.collect_show_answer);
        }
    }

    /**
     * 设置回调监听
     *
     * @param listener
     */

    public void mySetOnClickListener(MyOnClickListener listener) {
        this.myOnClickListener = listener;
    }

    public interface MyOnClickListener {
        void onViewClickListener(View view, int position);
    }

    /**
     * 设置回调监听
     *
     * @param itemLongClickListener
     */
    public void setOnItemLongClickListener(CollectAdapter.OnItemLongClickListener itemLongClickListener){
        this.onItemLongClickListen = itemLongClickListener;
    }
    public interface OnItemLongClickListener{
        void onItemLongClickListener(View itemView,int position);
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


    public static Bitmap getInternetPicture(String urlpath) {
        Bitmap bitmap = null;
        // 1、确定网址
        // http://pic39.nipic.com/20140226/18071023_164300608000_2.jpg
        //String urlpath = UrlPath;
        // 2、获取Uri
        try {
            URL uri = new URL(urlpath);

            // 3、获取连接对象、此时还没有建立连接
            HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
            // 4、初始化连接对象
            // 设置请求的方法，注意大写
            connection.setRequestMethod("GET");
            // 读取超时
            connection.setReadTimeout(5000);
            // 设置连接超时
            connection.setConnectTimeout(5000);
            // 5、建立连接
            connection.connect();

            // 6、获取成功判断,获取响应码
            if (connection.getResponseCode() == 200) {
                // 7、拿到服务器返回的流，客户端请求的数据，就保存在流当中
                InputStream is = connection.getInputStream();
                // 8、从流中读取数据，构造一个图片对象GoogleAPI
                if (bitmap != null){
                    bitmap.recycle();
                }
                bitmap  = BitmapFactory.decodeStream(is);
                // 9、把图片设置到UI主线程
                // ImageView中,获取网络资源是耗时操作需放在子线程中进行,通过创建消息发送消息给主线程刷新控件；

                //Log.i("", "网络请求成功");

            } else {
                //Log.v("tag", "网络请求失败");
                bitmap  = null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap ;
    }
}
