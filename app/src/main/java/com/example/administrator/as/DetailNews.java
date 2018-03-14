package com.example.administrator.as;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2018/2/6.
 */

public class DetailNews extends News {
    private String mBody;
    private String mImageSource;
    //private Bitmap mbitmap;
    //private byte[] imageByte;
    private String mShareUrl;
    private String mJs;
    private int mType;
    private String mCss;

    public DetailNews(int id, String title,byte[] bytes, String date, String body, String imageSource, String shareUrl, String js, int type, String css) {
        super(id, title,bytes, date);
       // mbitmap = bitmap;
        mBody = body;
        mImageSource = imageSource;
        mShareUrl = shareUrl;
        mJs = js;
        mType = type;
        mCss = css;
    }

    public String getBody() {
        return mBody;
    }

    public String getImageSource() {
        return mImageSource;
    }

    public String getShareUrl() {
        return mShareUrl;
    }

    public String getJs() {
        return mJs;
    }

    public int getType() {
        return mType;
    }

    public String getCss() {
        return mCss;
    }
}

