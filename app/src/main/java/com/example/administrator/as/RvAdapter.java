package com.example.administrator.as;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.Target;
import com.example.administrator.as.R;
import com.example.administrator.as.NewsDetailActivity;
import com.example.administrator.as.News;
import com.example.administrator.as.Jsonutil;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class RvAdapter extends RecyclerView.Adapter{
    private static final String TAG = "RvAdapter";
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_DATE = 1;
    public static final int TYPE_NORMAL = 2;
private Context context;
    private News[] mNews ;
    private News[] mTopNews;

    public RvAdapter(News[] news, News[] topNews, Context mcontext) {
        mNews = news;
        mTopNews = topNews;
        mcontext = context;

    }



    @Override
    public int getItemViewType(int position) {
        if (position == 0) return TYPE_HEADER;
        else if (mNews[position - 1] == null) return TYPE_DATE;
        else return TYPE_NORMAL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case TYPE_NORMAL:
                holder = new NewsViewHolder(inflater.inflate(R.layout.news_item, parent, false));
                break;

            case TYPE_DATE:
                holder = new DateViewHolder(inflater.inflate(R.layout.date_item, parent, false));
                break;

            case TYPE_HEADER:
                holder = new Header(inflater.inflate(R.layout.header, parent, false));
                break;
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_NORMAL:
                NewsViewHolder newsViewHolder = (NewsViewHolder) holder;
                newsViewHolder.setNewsItem(position - 1);
                //newsViewHolder.mImageView.setImageResource();
                break;

            case TYPE_DATE:
                ((DateViewHolder) holder).setDateItem(position - 1);
                break;

            case TYPE_HEADER:
                ((Header) holder).setHeader();
                break;
        }
    }

    public News[] getNews() {
        return mNews;
    }
    @Override
    public int getItemCount() {
        if(getNews() == null)return 0;
        else return mNews.length;
    }




    private class NewsViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;
        private ImageView mImageView;
        private int mCurrentPosition;

        NewsViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.img2);
            mTextView = (TextView) itemView.findViewById(R.id.text2);
            setOnClickListener(itemView);
        }

        private void setOnClickListener(View view) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), NewsDetailActivity.class);
                    intent.putExtra("position", mCurrentPosition);
                    ArrayList<Integer> ids = new ArrayList<>();
                    for (int i = 0; i < mNews.length; i++) {
                        if (mNews[i] != null)
                            ids.add(mNews[i].getId());
                    }
                    intent.putExtra("newsIds",ids);
                    view.getContext().startActivity(intent);
                }
            });
        }

        void setNewsItem(int position) {

            mCurrentPosition = position;
            mTextView.setText(mNews[position].getTitle());

            byte[] bytes = mNews[position].getImgBytes();
            if(bytes == null)
                Log.d(TAG, "setNewsItem");

                Bitmap bitmap = mNews[position].getBitmap();


           // Matrix matrix = new Matrix();
           // matrix.setScale(0.7f, 0.7f);
            //bitmap = Bitmap.createBitmap( bitmap, 0, 0,  bitmap.getWidth(), bitmap.getHeight(), matrix, false);
            //if(bitmap ==null) {
                //bitmap = BitmapFactory.decodeResource(MyApplication.getContext().getResources(), R.drawable.start0);
           // }
           //Bitmap bitmap =mNews[position].getImageBitmap();
            if(bitmap != null)
                try{
            mImageView.setImageBitmap(bitmap);
            // Glide.with(context).load(mNews[position].getImgBytes())
                   // .into(mImageView);
                } catch (OutOfMemoryError e) {
                    Log.d(TAG, "内存错误");
                }
        }
    }

    private class DateViewHolder extends RecyclerView.ViewHolder {
        private TextView mDate;

        DateViewHolder(View itemView) {
            super(itemView);
            mDate = (TextView) itemView.findViewById(R.id.date);
        }

        void setDateItem(int position) {
            mDate.setText(mNews[position + 1].getData());
        }
    }

    private class Header extends RecyclerView.ViewHolder {
        private ViewPager mViewPager;
        @SuppressLint("HandlerLeak")
        private final Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0x123) {
                    int position = mViewPager.getCurrentItem();
                    if(mTopNews.length != 0)
                    mViewPager.setCurrentItem((position + 1) % mTopNews.length, true);
                }
            }
        };

        public Header(View itemView) {
            super(itemView);
            mViewPager = (ViewPager) itemView.findViewById(R.id.view_pager1);
        }

        void setHeader() {
            TopNewsVpAdapter vpAdapter = new TopNewsVpAdapter(mTopNews);
            mViewPager.setAdapter(vpAdapter);

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mHandler.sendEmptyMessage(0x123);
                }
            }, 0, 5 * 1000);
        }
    }
}
