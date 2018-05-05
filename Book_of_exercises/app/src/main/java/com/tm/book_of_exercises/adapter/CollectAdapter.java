package com.tm.book_of_exercises.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
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
    private MyOnClickListener myOnClickListener;

    private String collectFlag = "false";
    private String img;


    public CollectAdapter(ArrayList<JSONObject> data, int xmlItemId) {
        this.mData = data;
        this.xmlItem = xmlItemId;
    }

    public CollectAdapter(Context context, ArrayList<JSONObject> data, int xmlItemId, int textId, int imgId, int collect, int follow, int tvMsgId, int label, String f) {
        this.mData = data;
        this.context = context;
        this.xmlItem = xmlItemId;
        this.imgContextId = imgId;
        this.tvContextId = textId;
        this.tvMsgId = tvMsgId;
        this.tvLabelId = label;
        this.img_collectId = collect;
        this.flag = f;
        this.tvFollowId = follow;
    }

    public CollectAdapter(Context context, ArrayList<JSONObject> data, int xmlItemId, int textId, int imgId, int follow, int label, String f) {
        this.mData = data;
        this.context = context;
        this.xmlItem = xmlItemId;
        this.imgContextId = imgId;
        this.tvContextId = textId;
        this.tvLabelId = label;
        this.tvFollowId = follow;
        this.flag = f;
    }


    private void updateData(ArrayList<JSONObject> data) {
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

    private Handler mHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            // TODO Auto-generated method stub
            return false;
        }
    });

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // 绑定数据
        JSONObject jsonObject = mData.get(position);
        if ("all".equals(flag)) {
            try {
                collectFlag = jsonObject.getString("collectFlag");
                String urlpath = jsonObject.getString("contextImg");
                String content = "  " + (position + 1) + "、" + jsonObject.getString("context").replace("\\n", "\n");
                holder.mTv.setText(content);
                if (!"null".equals(jsonObject.getString("contextImg"))) {

                    Glide.with(context)
                            .load(urlpath)
                            .into(holder.mImg);

                    //不使用过度动画  .transition(BitmapTransitionOptions.withCrossFade())  // TransitionOptions是和你要加载的资源的类型绑定的，也就是说，如果你请求一张位图(Bitmap),你就需要使用BitmapTransitionOptions，而不是DrawableTransitionOptions。因此，你请求的这张位图，你需要用简单的淡入，而不能用 交叉淡入(DrawableTransitionOptions.withCrossFade())。如果既不是Bitmap也不是Drawable可以使用GenericTransitionOptions

//                    Handler handler = new Handler() {
//                        Bitmap bitmap1 = null;
//                        public void handleMessage(android.os.Message msg) {
//                            WindowManager wm = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
//                            int width = wm.getDefaultDisplay().getWidth();
//                            if(bitmap1 != null){
//                                bitmap1.recycle();
//                                bitmap1 = null;
//                            }
//                            bitmap1 = (Bitmap) msg.obj;
//                            int height = 0;
//                            if (bitmap1.getHeight() > bitmap1.getWidth()) {
//                                height = width * (bitmap1.getHeight() / bitmap1.getWidth());
//                            } else {
//                                height = width / (bitmap1.getWidth() / bitmap1.getHeight());
//                            }
//                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.mImg.getLayoutParams();
//                            params.height = height;
//                            holder.mImg.setLayoutParams(params);
//                            holder.mImg.setImageBitmap(bitmap1);
//                        }
//                    };
//                    new Thread(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            // TODO Auto-generated method stub
//                            Bitmap bitmap2 = null;
//                            if (bitmap2 != null){
//                                bitmap2 = null;
//                                bitmap2.recycle();
//                            }
//                            bitmap2 = getInternetPicture(urlpath);
//                            Message msg = new Message();
//                            // 把bm存入消息中,发送到主线程
//                            msg.obj = bitmap2;
//                            handler.sendMessage(msg);
//                        }
//                    }).start();
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
        } else if ("collect".equals(flag)) {
            try {
                collectFlag = jsonObject.getString("collectFlag");
                String urlpath = jsonObject.getString("contextImg");
                String content = "  " + (position + 1) + "、" + jsonObject.getString("context").replace("\\n", "\n");
                holder.mTv.setText(content);
                if (!"null".equals(jsonObject.getString("contextImg"))) {
                    Glide.with(context)
                            .load(urlpath)
                            .into(holder.mImg);
                    //主线程处理消息队列中的消息，并刷新相应UI控件
//                    Handler handler = new Handler() {
//                        public void handleMessage(Message msg) {
//                            Bitmap bm = null;
//                            WindowManager wm = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
//                            int width = wm.getDefaultDisplay().getWidth();
//                            if (bm != null){
//                                bm.recycle();
//                                bm = null;
//                            }
//                            bm = (Bitmap) msg.obj;
//                            int height = 0;
//                            if (bm.getHeight() > bm.getWidth()) {
//                                height = width * (bm.getHeight() / bm.getWidth());
//                            } else {
//                                height = width / (bm.getWidth() / bm.getHeight());
//                            }
//                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.mImg.getLayoutParams();
//                            params.height = height;
//                            holder.mImg.setLayoutParams(params);
//                            holder.mImg.setImageBitmap((Bitmap) msg.obj);
//                        }
//
//                        ;
//                    };
//                    //同时要注意网络操作需在子线程操作，以免引起主线程阻塞，影响用途体验，同时采用handler消息机制进行参数处理，刷新UI控件
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            // TODO Auto-generated method stub
//                            //String urlpath = "http://pic39.nipic.com/20140226/18071023_164300608000_2.jpg";
//                            Bitmap bm1 = null;
//                            if (bm1 != null){
//                                bm1.recycle();
//                                bm1 = null;
//                            }
//                            bm1 = getInternetPicture(urlpath);
//                            Message msg = new Message();
//                            // 把bm存入消息中,发送到主线程
//                            msg.obj = bm1;
//                            handler.sendMessage(msg);
//                        }
//                    }).start();
                }
                if (!"null".equals(jsonObject.getString("msg")) || !"".equals(jsonObject.getString("msg"))) {
                    holder.mTvMsg.setText(jsonObject.getString("msg"));
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
            mTvFollow = itemView.findViewById(tvFollowId);
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

    public void mySetOnClickListener(MyOnClickListener listener) {
        this.myOnClickListener = listener;
    }

    public interface MyOnClickListener {
        void onViewClickListener(View view, int position);
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
