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
import com.tm.book_of_exercises.main.otherPage.TasksActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * 作者：T M on 2018/5/7 00:04
 * 邮箱：xxx@163.com
 */
public class AllTasksAdapter extends RecyclerView.Adapter<AllTasksAdapter.ViewHolder> {
    private Context context;
    private ArrayList<JSONObject> mData;
    /**
     * 事件回调监听
     */
    private AllTasksAdapter.MyOnClickListener myOnClickListener;

    private String collectFlag = "false";
    private String img;
    private AllTasksAdapter.OnItemLongClickListener onItemLongClickListen;


    public AllTasksAdapter(Context context ,ArrayList<JSONObject> data) {
        this.mData = data;
        this.context = context;
    }

    public void updateData(ArrayList<JSONObject> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public AllTasksAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的View
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_tasks, parent, false); //item 布局文件xml
        //parent.removeAllViews();//去除重复数据
        // 实例化 ViewHolder
        AllTasksAdapter.ViewHolder viewHolder = new AllTasksAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AllTasksAdapter.ViewHolder holder, int position) {
        if(Constant.showSelectFlag){
            holder.linearLayout.setVisibility(View.VISIBLE);
        }else {
            holder.linearLayout.setVisibility(View.GONE);
        }
        // 绑定数据
        JSONObject jsonObject = mData.get(position);
        try {
            collectFlag = jsonObject.getString("collectFlag");
            String urlpath = jsonObject.getString("contextImg");
            String answerUri = jsonObject.getString("answer");
            String content = "  " + (position + 1) + "、" + jsonObject.getString("context").replace("\\n", "\n");
            if (Constant.showAnswerFlag && position == TasksActivity.pos){
                holder.showAnswer.setVisibility(View.VISIBLE);
                if("".equals(answerUri) || answerUri != null || "null".equals(answerUri)){
                    Toast.makeText(context,"暂没正确答案",Toast.LENGTH_LONG).show();
                }else {
                    Glide.with(context)
                            .load(answerUri)
                            .into(holder.showAnswer);
                }
            }else {
                holder.showAnswer.setVisibility(View.GONE);
            }
            holder.mTv.setText(content);
            if (!"null".equals(jsonObject.getString("contextImg"))) {

                Glide.with(context)
                        .load(urlpath)
                        .into(holder.mImg);
            }
            if ("true".equals(collectFlag)) {
                holder.mTvLabel.setText("#收藏习题#");
                holder.mTvFollow.setTextColor(Color.parseColor("#FC2828"));
                holder.mTvFollow.setText("已关注");
            }
            if ("false".equals(collectFlag)) {
                holder.mTvLabel.setText("#未收藏习题#");
                holder.mTvFollow.setText("关  注");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.mTvFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myOnClickListener != null) {
                    if ("true".equals(collectFlag)) {
                        holder.mTvFollow.setTextColor(Color.parseColor("#fc1e2327"));
                        holder.mTvFollow.setText("关  注");
                        collectFlag = "false";
                    } else if ("false".equals(collectFlag)) {
                        holder.mTvFollow.setTextColor(Color.parseColor("#FC2828"));
                        holder.mTvFollow.setText("已关注");
                        collectFlag = "true";
                    }
                    int pos = holder.getLayoutPosition();
                    myOnClickListener.onViewClickListener(holder.mTvFollow, pos);
                }
            }
        });
        holder.seeAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getLayoutPosition();
                myOnClickListener.onViewClickListener(holder.seeAnswer, pos);
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
        TextView mTvFollow;
        ImageView seeAnswer;
        TextView mTvLabel;
        LinearLayout linearLayout;
        ImageView showAnswer;


        public ViewHolder(View itemView) {
            super(itemView);
            mTv = itemView.findViewById(R.id.all_task_tv); //(R.id.oneContact);
            mImg = itemView.findViewById(R.id.all_tasks_img);
            mTvFollow = itemView.findViewById(R.id.all_tasks_follow);
            mTvLabel = itemView.findViewById(R.id.all_tasks_label);
            seeAnswer = itemView.findViewById(R.id.all_tasks_see_answer);
            showAnswer = itemView.findViewById(R.id.all_answer_img);
            mSelectTasks = itemView.findViewById(R.id.all_tasks_select_tastk);
            linearLayout = itemView.findViewById(R.id.linearLayout_all_tasks);
        }
    }

    /**
     * 设置回调监听
     *
     * @param listener
     */

    public void mySetOnClickListener(AllTasksAdapter.MyOnClickListener listener) {
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
    public void setOnItemLongClickListener(AllTasksAdapter.OnItemLongClickListener itemLongClickListener){
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
