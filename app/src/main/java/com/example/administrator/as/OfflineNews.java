package com.example.administrator.as;

/**
 * Created by Administrator on 2018/2/7.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


public class OfflineNews{
    public static final String NEWS = "news";
    public static final String TOP_NEWS = "topNews";

    public static DetailNews[] getDetailedNews(Context context) {
        SQLiteDatabase db = new NewData(context, "News.db", null, 1).getWritableDatabase();
        Cursor cursor = db.query("detailedNews", null, null, null, null, null, null);
        ArrayList<DetailNews> newsList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String css = cursor.getString(cursor.getColumnIndex("css"));
                String body = cursor.getString(cursor.getColumnIndex("body"));
                newsList.add(new DetailNews(id, null, null, null, body, null, null, null, 0, css));
            } while (cursor.moveToNext());
        }

        return newsList.toArray(new DetailNews[]{});
    }

    @NonNull
    public static News[] getNews(Context context, String newsType) {
        SQLiteDatabase db = new NewData(context, "News.db", null, 1).getWritableDatabase();
        Cursor cursor = db.query(newsType, null, null, null, null, null, null);
        ArrayList<News> newsList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("newsId"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                byte[] image = cursor.getBlob(cursor.getColumnIndex("image"));
                //Bitmap bitmap = BitmapFactory.decodeByteArray(image,0,image.length);
                 //= cursor.getBlob(cursor.getColumnIndex("bitmap"));
                newsList.add(new News(id, title,image , date));
            } while (cursor.moveToNext());
        }

        return newsList.toArray(new News[]{});
    }

    public static void saveNews(Context context, String newsType, News[] news) {
        SQLiteDatabase db = new NewData( context, "News.db", null, 1).getWritableDatabase();
        ContentValues values = new ContentValues();

        if (newsType.equals("topNews"))
            db.delete("topNews",null,null);

        for (News newsTemp : news) {
            if (newsTemp == null) continue;
            values.put("newsId", newsTemp.getId());
            values.put("date", newsTemp.getData());
            values.put("title", newsTemp.getTitle());
          //  byte[] bytes = img(newsTemp.getImgBytes());
            values.put("image",newsTemp.getImgBytes());
            //values.put("bitmap", newsTemp.getImageBitmap());
            db.insertWithOnConflict(newsType, null, values, SQLiteDatabase.CONFLICT_REPLACE);
            values.clear();
        }
    }

    public static void saveDetailedNews(Context context, DetailNews detailedNews) {
        SQLiteDatabase db = new NewData(context, "News.db", null, 1).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", detailedNews.getId());
        values.put("css", detailedNews.getCss());
        values.put("body", detailedNews.getBody());
        db.insertWithOnConflict("detailedNews", "id", values, SQLiteDatabase.CONFLICT_REPLACE);
    }
   /* public  static byte[] img(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(id)).getBitmap();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
    */
}
