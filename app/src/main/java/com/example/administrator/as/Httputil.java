package com.example.administrator.as;

import android.accounts.NetworkErrorException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import static android.system.Os.read;


/**
 * Created by Administrator on 2018/2/5.
 */
public class Httputil {


    //public Httputil() throws IOException {


    //}




    public static void sendHttpRequest(final String address, final Callback callback){
       // final android.os.Handler handler = new
                //android.os.Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection httpURLConnection = null;

                try {
                    URL url = new URL(address);
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    //httpURLConnection.connect();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(8000);
                    httpURLConnection.setReadTimeout(8000);
                    if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK)
                        throw new IOException(httpURLConnection.getResponseMessage() + ": with " + address);
                    else {
                        InputStream in = httpURLConnection.getInputStream();
                        final byte[] bytes = read(in);
                       // handler.post(new Runnable() {
                           // @Override
                           // public void run() {
                                String string = null;
                               try {
                                    string = new String(bytes,"UTF-8");
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                callback.onResponse(string);}




            /*ByteArrayOutputStream out = new ByteArrayOutputStream();

            int bytesRead;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer, 0, 1024)) > 0)
                out.write(buffer, 0, bytesRead);
            out.close();
            return out.toByteArray();
            */

                } catch (MalformedURLException e) {
                    e.printStackTrace();

                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (httpURLConnection != null) httpURLConnection.disconnect();
                }

            }
        }).start();

    }

    /*public static String getDataString(String address) throws IOException {
        byte[] bytes = getDataBytes(address);
        if (bytes != null) return Arrays.toString(bytes);
        else return "";

    }
    */
    private static byte[] read(InputStream is) throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];
        int len;
        while((len = is.read(bytes))!=-1)
            outputStream.write(bytes,0,len);
        is.close();
        return outputStream.toByteArray();
    }

    public interface Callback{
        void onResponse(String response);
    }
    public interface ImageCallback{
        void onResponse(byte[] bytes);
    }

    public static void sendImageHttpRequest(final String address, final ImageCallback callback){
         //final android.os.Handler handler = new
        //android.os.Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection httpURLConnection = null;

                try {
                    URL url = new URL(address);
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    //httpURLConnection.connect();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(8000);
                    httpURLConnection.setReadTimeout(8000);
                    if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK)
                        throw new IOException(httpURLConnection.getResponseMessage() + ": with " + address);
                    else {
                        InputStream in = httpURLConnection.getInputStream();

                         byte[] bytes = read(in);
                         //handler.post(new Runnable() {
                          //@Override
                          //public void run() {
                        callback.onResponse(bytes);
                         //}
                         //});

                    }


                } catch (MalformedURLException e) {
                    e.printStackTrace();

                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (httpURLConnection != null) httpURLConnection.disconnect();
                }

            }
        }).start();

    }








    public  static final String latest = "https://news-at.zhihu.com/api/4/news/latest";
    public static final String detail ="https://news-at.zhihu.com/api/4/news/id";
    //后面的id要换
    public static final String before = "https://news-at.zhihu.com/api/4/news/before/20180228";
    public static final String theme = "https://news-at.zhihu.com/api/4/themes";
    public static final String themed = "https://news-at.zhihu.com/api/4/theme/11";

}


