package com.example.administrator.as;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.administrator.as.News;
import com.example.administrator.as.NewData;



import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;


/**
 * Created by Administrator on 2018/2/6.
 */

public class Jsonutil {
private static byte[] imgBytes;
private static String css;
//private static byte[] mbytes;
private static News[] news ;
private static final String TAG = "ss";

    /*public void parseLatestJson(String latestaddress) throws JSONException {
            JSONObject jsonObject = new JSONObject(latestaddress);
            String data = jsonObject.getString("data");
            JSONArray jsonArray1 = jsonObject.getJSONArray("stories");
            JSONArray jsonArray2 = jsonObject.getJSONArray("top_stories");
            for(int i = 0;i<jsonArray1.length();i++){
               JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
               String title = jsonObject1.getString("title");
               String ga_prefix = jsonObject1.getString("ga_prefix");
               String image = jsonObject1.getString("imsge");
               int type = jsonObject1.getInt("type");
               int id = jsonObject1.getInt("id");
            }
            for(int j = 0;j <jsonArray2.length();j++){
                JSONObject jsonObject2 = jsonArray2.getJSONObject(j);
                String image1 = jsonObject2.getString("image");
                String ga_prefix1 = jsonObject2.getString("ga_prefix");
                String title2 = jsonObject2.getString("title");
                int type1 = jsonObject2.getInt("type");
                int id1 = jsonObject2.getInt("id");
            }
        }

    }//
    */
   /* @Nullable
    public static News[] getTopNews() {
        try {
            String aa  = Httputil.getDataString(Httputil.latest);
            JSONObject object = new JSONObject(aa);
            return parseTopNews(object);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
*/
    @Nullable
    public static News[] getNewsBefore(GregorianCalendar date) {
        String strUrl = getNewsStrUrl(date);
         //News[] news;
       // final ArrayList<News> newsList = new ArrayList<>();
        Httputil.sendHttpRequest(strUrl, new Httputil.Callback() {
            @Override
            public void onResponse(String response) {

        try {

               news = parseNews(response, "stories");
        } catch (JSONException | IOException e) {
            e.printStackTrace();

        }

            }
        });

        return news;
    }
/*
    @Nullable
    public  DetailNews getDetailNews(long newsId) {
        try {
            String strUrl = Httputil.detail.replace("id", ""+newsId);
            JSONObject object = new JSONObject(Httputil.getDataString(strUrl));
            return parseDetailNews(object);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }*/


    @NonNull
    public  DetailNews parseDetailNews(String data) throws IOException, JSONException {
        JSONObject object = new JSONObject(data);
        String body = object.getString("body");
        String imageSource = object.getString("image_source");
        String shareUrl = object.getString("share_url");
        JSONArray jsArray = object.getJSONArray("js");
        String js = "";
        if (jsArray.length() != 0)
            js = jsArray.getString(0);
        int type = object.getInt("type");
        //final String[] css = new String[1];
        Httputil.sendHttpRequest(object.getJSONArray("css").getString(0), new Httputil.Callback() {
            @Override
            public void onResponse(String response) {
                 css ="<style>"+response+ "</style>";
            }
        });
       // String css = "<style>"+Httputil.getDataString(object.getJSONArray("css").getString(0))+"</style>";
        String title = object.getString("title");
        int id = object.getInt("id");
        String date = getStrDate(new GregorianCalendar());

        body = body.replace("<div class=\"img-place-holder\"></div>", "<div><img src=\""+object.getString("image")+"\"></div>");
        return new DetailNews(id, title,null , date, body, imageSource, shareUrl, js, type, css);
    }

    @NonNull
    public static News[] parseTopNews(String data) throws IOException, JSONException {
        ArrayList<News> newsList = new ArrayList<>();
        newsList.addAll(Arrays.asList(parseNews(data, "top_stories")));
        newsList.remove(0);
        return newsList.toArray(new News[]{});//指定类型
    }

    @NonNull
    public  static News[] parseNews(String data, final String newsType) throws IOException, JSONException {
        byte[] mbytes = new byte[1000000];
        JSONObject object = new JSONObject(data);
        final JSONArray storyArray = object.getJSONArray(newsType);
        ArrayList<News> newsList = new ArrayList<>();

        newsList.add(null); //用作recyclerView的分段标记

        //解析日期
        String date = object.getString("date");
        if (date.equals(getStrDate(new GregorianCalendar())))
            date = "今日新闻";
        else
            date = date.substring(0, 4) + "年" + Integer.parseInt(date.substring(4, 6)) + "月" + Integer.parseInt(date.substring(6, 8)) + "日";

        //解析id、标题、图片
        for (int i = 0; i < storyArray.length(); i++) {
            int id = storyArray.getJSONObject(i).getInt("id");
            String title = storyArray.getJSONObject(i).getString("title");
           // final byte[][] imgBytes = new byte[1][1];
            if(newsType.equals("top_stories")) {
                String string = storyArray.getJSONObject(i).getString("image");
                final byte[] finalMbytes1 = mbytes;
                Httputil.sendImageHttpRequest(string, new Httputil.ImageCallback() {
                    @Override
                    public void onResponse(byte[] bytes) {
                        //byte[] mbytes = new byte[];
                          //mbytes = new byte[bytes.length];
                        //mbytes = bytes ;
                        System.arraycopy(bytes,0, finalMbytes1,0,bytes.length);
                    }
                });


            }
            if(newsType.equals("stories")){
                final byte[] finalMbytes = mbytes;
                Httputil.sendImageHttpRequest(storyArray.getJSONObject(i).getJSONArray("images").getString(0), new Httputil.ImageCallback() {
                    @Override
                    public void onResponse(byte[] bytes) {
                        //mbytes = bytes;
                        // mbytes = new byte[];
                         //mbytes = new byte[bytes.length];

                        System.arraycopy(bytes,0, finalMbytes,0,bytes.length);
                    }
                });



                    }

if (mbytes ==null){
    Log.d(TAG, "parseNews: ");
            }
            //byte[] imgBytes = newsType.equals("top_stories") ? parseTopNewsImage(storyArray.getJSONObject(i)) : parseNewsImage(storyArray.getJSONObject(i));
            newsList.add(new News(id, title, mbytes, date));
        }


        return newsList.toArray(new News[]{});
    }
/*
   private static byte[] parseTopNewsImage(String data) throws JSONException, IOException {
      //return Httputil.(object.getString("image"));
       final byte[] mbytes;
Httputil.sendImageHttpRequest(data, new Httputil.ImageCallback() {
    @Override
    public void onResponse(byte[] bytes) {
        mbytes = bytes;
    }
});


}

   }

    private  static byte[] parseNewsImage(JSONObject object) throws JSONException, IOException {
        return Httputil.getDataBytes(object.getJSONArray("images").getString(0));
    }//一般只有一张图
    */

    private  static String getStrDate(GregorianCalendar calendar) {
        int year = calendar.get(GregorianCalendar.YEAR);
        int month = calendar.get(GregorianCalendar.MONTH) + 1;
        int date = calendar.get(GregorianCalendar.DATE);
        String strDate = "" + year;
        if (month < 10)
            strDate += "0";
        strDate += month;

        if (date < 10)
            strDate += "0";
        strDate += (date-1);
        return strDate;
    }

    private  static String getNewsStrUrl(GregorianCalendar calendar) {
        calendar.add(GregorianCalendar.DATE, 1);
        return Httputil.before.replace("date", getStrDate(calendar));
    }


}

