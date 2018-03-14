package com.example.administrator.as;

/**
 * Created by Administrator on 2018/2/6.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import static com.example.administrator.as.R.drawable.start0;


public class News {
    private final static String TAG = "News";
    private int mId;
    private String mTitle = "";
    private byte[] mImgBytes = null;
    private String mData;
    private Bitmap mbitmap;

    public News(int  id,String title,byte[] imgBytes, String data){
       mId = id;
       mTitle = title;
       mImgBytes = imgBytes;
       mData = data;
       //mbitmap = bitmap;
    }
    //BitmapFactory.Options op = new BitmapFactory.Options();
   // op.inSampleSize = 2;

    public void setmId(int mId) {
        this.mId = mId;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setmImgBytes(byte[] mImgBytes) {
        this.mImgBytes = mImgBytes;
    }

    public void setmData(String mData) {
        this.mData = mData;
    }

    public void setMbitmap(Bitmap mbitmap) {
        this.mbitmap = mbitmap;
    }

    public int getId() {
        return mId;
    }

    public byte[] getImgBytes() {
        return mImgBytes;
    }

    public String getTitle() {
        return mTitle;
    }
    public String getData(){
        return mData;
    }


   public Bitmap getBitmap() {
        if(getImgBytes() != null) {
            try {
                return BitmapFactory.decodeByteArray(getImgBytes(), 0, mImgBytes.length);
            } catch (OutOfMemoryError e) {
                Log.d(TAG, "getBitmap: 内存");
            }
        }
        else{
            Log.d(TAG, "getBitmap: null");
            return null;
        }
        return null;
    }
   // public  Bitmap getImageBitmap(){
       // return mbitmap;
    //}




}
