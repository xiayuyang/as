package com.example.administrator.as;

/**
 * Created by Administrator on 2018/2/11.
 */

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v4.view.PagerAdapter;
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

import java.util.ArrayList;

public class TopNewsVpAdapter extends PagerAdapter {
    private Context context;
private static final String TAG = "Top";
    private News[] mTopNews;

    public TopNewsVpAdapter(News[] news) {
        mTopNews = news;

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View view = inflater.inflate(R.layout.top_news_pager_item, null);
        setOnClickListener(view, position);
        //设置新闻标题
        TextView tv = (TextView) view.findViewById(R.id.title);
        tv.setText(mTopNews[position].getTitle());
        //设置图片
        ImageView imageView = (ImageView) view.findViewById(R.id.img);
        //byte[] bytes = mTopNews[position].getImgBytes();
        Bitmap bitmap = mTopNews[position].getBitmap();
        //Matrix matrix = new Matrix();
        //matrix.setScale(0.7f, 0.7f);
        //if(bitmap == null){
          //  bitmap = BitmapFactory.decodeResource(MyApplication.getContext().getResources(),R.drawable.start2);
       // }
        //bitmap = Bitmap.createBitmap( bitmap, 0, 0,  bitmap.getWidth(), bitmap.getHeight(), matrix, false);

        //Bitmap bitmap = mTopNews[position].getImageBitmap();
if(bitmap != null)
    try{
        imageView.setImageBitmap(bitmap);
    } catch (OutOfMemoryError e) {
        Log.d(TAG, "内存错误");
    }

        // Glide.with(context).load(mTopNews[position].getImgBytes())
                //.into(imageView);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //ImageView imageView = (ImageView) object;
        //if (imageView == null)
          //  return;
        //Glide.clear(imageView);
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mTopNews.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (View) object;
    }

    private void setOnClickListener(View itemView, final int position) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), NewsDetailActivity.class);
                intent.putExtra("position", position);
                ArrayList<Integer> ids = new ArrayList<>();
                for (int i = 0; i < mTopNews.length; i++) {
                    ids.add(mTopNews[i].getId());
                }
                intent.putExtra("newsIds", ids);
                view.getContext().startActivity(intent);
            }
        });
    }
}
