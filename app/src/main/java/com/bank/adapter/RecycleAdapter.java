package com.bank.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.bank.R;
import com.bank.model.IndexModel;
import com.bank.util.BitmapCache;
import com.bank.util.MyApplication;

import java.util.List;

/**
 * Created by yanwe on 2016/4/26.
 */
public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.MyViewHolder> {

    //    private final ArrayList<Integer> mHeights;
    private List<IndexModel> list;
    private Context context;
    private ImageLoader imageLoader = new ImageLoader(MyApplication.queues,
            new BitmapCache());

    public RecycleAdapter(List<IndexModel> list, Context context) {
        this.list = list;
        this.context = context;
//        //初始化一些随机高度 模拟瀑布流
//        mHeights = new ArrayList<Integer>();
//        for (int i = 0; i < list.size(); i++)
//        {
//            mHeights.add( (int) (100 + Math.random() * 300));
//        }
    }

    //由于没有提供ItemClickListener所以自己实现
    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickListener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recycle, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        //模拟瀑布流
//        ViewGroup.LayoutParams lp = holder.tv.getLayoutParams();
//        lp.height = mHeights.get(position);
//        holder.tv.setLayoutParams(lp);
//        holder.tv.setText(list.get(position));
        IndexModel indexModel = list.get(position);
        holder.index_title.setText(indexModel.getIndex_title());
        holder.index_uptime.setText(indexModel.getIndex_uptime_format());
//        OkHttpUtils
//                .get()//
//                .url(indexModel.getIndex_preview_image_url())//
//                .build()//
//                .execute(new BitmapCallback() {
//                    @Override
//                    public void onError(Call call, Exception e) {
//
//                    }
//
//                    @Override
//                    public void onResponse(Bitmap response) {
//                        holder.index_preview_image.setImageBitmap(response);
//                    }
//                });

        ImageLoader.ImageListener imageListener = ImageLoader
                .getImageListener(
                        holder.index_preview_image,
                        R.drawable.common_ic_googleplayservices,
                        R.drawable.common_ic_googleplayservices);// 设置默认图片和错误图片
        imageLoader.get(indexModel.getIndex_preview_image_url(), imageListener);

        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView index_title, index_uptime;
        ImageView index_preview_image;

        public MyViewHolder(View view) {
            super(view);
            index_title = (TextView) view.findViewById(R.id.index_title);
            index_uptime = (TextView) view.findViewById(R.id.index_uptime);
            index_preview_image = (ImageView) view.findViewById(R.id.index_preview_image);
        }
    }

//    public void addData(int position) {
//
//        list.add(position, "Insert One");
//        mHeights.add( (int) (100 + Math.random() * 300));
//        notifyItemInserted(position);
//    }
//
//    public void removeData(int position) {
//        list.remove(position);
//        mHeights.remove(position);
//        notifyItemRemoved(position);
//    }
}
