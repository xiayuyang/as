package com.example.administrator.as;

/**
 * Created by Administrator on 2018/2/1.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


//固定
    public class Netutil {
        public static boolean isNetWorkAvailable(Context context) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isAvailable();
        }
    }

