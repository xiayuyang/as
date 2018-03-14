package com.example.administrator.as;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Toast;

import com.example.administrator.as.R;
import com.example.administrator.as.DetailNews;
import com.example.administrator.as.Netutil;
import com.example.administrator.as.Jsonutil;
import com.example.administrator.as.News;

import org.json.JSONException;

import java.io.IOException;


public class NewsDetailFragment extends Fragment {
    private WebView mWebView;
    private DetailNews mDetailedNews;
    public Context mContext;
    private Jsonutil jsonutil = new Jsonutil();

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x123) {
                String css = mDetailedNews.getCss();
                String body = mDetailedNews.getBody();
                mWebView.loadDataWithBaseURL("", css+body, "text/html", "utf-8", "");
            } else if (msg.what == 0x124)
                Toast.makeText(mContext, "无法连接网络", Toast.LENGTH_LONG).show();
        }
    };

    public NewsDetailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_news_pager_item, container, false);
        mWebView = (WebView) view.findViewById(R.id.web_view);
        mContext = view.getContext();
        return view;
    }

    public void showDetailNews(final int mid) {



                mDetailedNews = null;
                if (Netutil.isNetWorkAvailable(mContext))
                    Httputil.sendHttpRequest(Httputil.detail.replace("id",""+mid), new Httputil.Callback() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                mDetailedNews = jsonutil.parseDetailNews(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    //mDetailedNews = new Jsonutil().getDetailNews(id);
                else {
                    DetailNews[] detailedNewses = OfflineNews.getDetailedNews(mContext);
                    for (int i = 0; i < detailedNewses.length; i++) {
                        if (detailedNewses[i].getId() == mid)
                            mDetailedNews = detailedNewses[i];
                    }
                }
                if (mDetailedNews != null)
                    mHandler.sendEmptyMessage(0x123);
                else
                    mHandler.sendEmptyMessage(0x124);
            }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mDetailedNews != null)
            OfflineNews.saveDetailedNews(mContext, mDetailedNews);
    }
}
