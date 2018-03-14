package com.example.administrator.as;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.as.R;
import com.example.administrator.as.RvAdapter;
import com.example.administrator.as.Netutil;
import com.example.administrator.as.News;
import com.example.administrator.as.Jsonutil;
import com.example.administrator.as.OfflineNews;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;



public class NewsActivity extends AppCompatActivity implements Runnable {
    private static final String TAG = "aa";
    private static final int REFRESHED = 0x126;
    private static final int MORE_NEWS = 0x127;
    private static final int START_NEWS = 0x128;

    //recyclerView相关
    private SwipeRefreshLayout mRefreshLayout;
    private LinearLayoutManager mLayoutManager;
    private RvAdapter mRvAdapter;
    private RecyclerView mRecyclerView;
    private int currentPosition;
    private boolean loading = false;

    //toolbar相关
    private TextView mTitle;
    public Jsonutil jsonutil = new Jsonutil();
    private GregorianCalendar mCalendar;
    private  News[] mTopNews;
    private  News[] mNews;

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case REFRESHED:
                    mRefreshLayout.setRefreshing(false);
                    mCalendar = new GregorianCalendar();
                    mCalendar.add(Calendar.DATE, 1);
//获得前一天的日期

                           /* mTopNews = Jsonutil.getTopNews();
                            mNews = Jsonutil.getNewsBefore(new GregorianCalendar());
                            RvAdapter adapter = new RvAdapter(mNews, mTopNews);
                            mRecyclerView.setAdapter(adapter);
                            */
                           mRvAdapter.notifyDataSetChanged();
                            //mRecyclerView.setAdapter(adapter);

                    //mRvAdapter.notifyDataSetChanged();
                    //mRecyclerView.setAdapter(mRvAdapter);

                    break;
               case START_NEWS:
                    mRecyclerView.setAdapter(mRvAdapter);
                    break;

                case MORE_NEWS:
                    mRecyclerView.scrollToPosition(currentPosition);
                    mCalendar.add(GregorianCalendar.DATE, 1);
                    loading = false;
                    mRvAdapter.notifyDataSetChanged();
                    //mRecyclerView.setAdapter(mRvAdapter);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        //里面是建数据库
        //new NewData(this, "detailedNews.db", null, 1).getWritableDatabase();
        //new NewData(this, "News.db", null, 1).getWritableDatabase();
        //new NewData(this, "topNews.db", null, 1).getWritableDatabase();
        // Jsonutil mJsonutil = new Jsonutil();
        //new Thread(new Runnable() {
        // @Override
        // public void run() {
        // Jsonutil.getNewsBefore(new GregorianCalendar(2018,2,13));
        //}
        // }).start();

          /* new Thread(new Runnable() {
               @Override
               public void run() {
                   News[] news = mJsonutil.getNewsBefore(new GregorianCalendar(2018,2,14));
                   OfflineNews.saveNews(getApplicationContext(),OfflineNews.NEWS,news);

               }
           }).start();
           */
        new NewData(this, "News.db", null, 1).getWritableDatabase();


        mCalendar = new GregorianCalendar();

        mTitle = (TextView) findViewById(R.id.title);

        setUpRecyclerView();
        setUpSwipeRefreshLayout();

    }

    private void setUpSwipeRefreshLayout() {
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_rl);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(NewsActivity.this).start();
            }
        });


    }

    private void setUpRecyclerView() {
        //final News[] mNews = new News[];
        //final News[] mTopNews = new News[];
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        mLayoutManager = new LinearLayoutManager(NewsActivity.this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        //Intent intent = getIntent();

        Httputil.sendHttpRequest(Httputil.latest, new Httputil.Callback() {
            @Override
            public void onResponse(String response) {
                try {
                    mNews= jsonutil.parseNews(response, "stories");
                    //mTopNews = jsonutil.parseTopNews(response);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        Httputil.sendHttpRequest(Httputil.latest, new Httputil.Callback() {
            @Override
            public void onResponse(String response) {
                try {
                    //mNews= jsonutil.parseNews(response, "stories");
                    mTopNews = jsonutil.parseTopNews(response);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        if(mNews == null)
            Log.d(TAG, "setUpRecyclerView");

        //try {
        // mNews = jsonutil.parseNews(intent.getStringExtra("data"),"stories");
        // mTopNews = jsonutil.parseTopNews(intent.getStringExtra("data"));
        //}
        //catch (IOException e) {
        //   e.printStackTrace();
        //} catch (JSONException e) {
        //   e.printStackTrace();
        //}
        //new Thread(this).start();
        //第二种方法
        // new Thread(new Runnable() {
        //@Override
        // public void run() {
        // mTopNews = Jsonutil.getTopNews();
        //mNews = Jsonutil.getNewsBefore(new GregorianCalendar());


        //new ArrayList<?>().addAll(mTopNews);
        //new ArrayList<?>().addAll(mNews);
        //if(mTopNews != null&&mNews != null)
        mRvAdapter = new RvAdapter(mNews, mTopNews, NewsActivity.this);
        //mRvAdapter.notifyDataSetChanged();
        //mRecyclerView.setAdapter(mRvAdapter);
        mHandler.sendEmptyMessage(START_NEWS);

        //}).start();


        //adapter.notifyDataSetChanged();
        //设置滚动监听器
        addRecyclerViewOnScrollListener();
    }

    private void addRecyclerViewOnScrollListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();

                if (firstVisibleItemPosition == 0)
                    mTitle.setText("首页");
                else if (mRvAdapter.getItemViewType(firstVisibleItemPosition) == RvAdapter.TYPE_NORMAL)
                    mTitle.setText(mRvAdapter.getNews()[firstVisibleItemPosition - 1].getData());
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == 0 && !loading) {
                    //判断是否滑动到底部
                    int totalItemCount = mLayoutManager.getItemCount();
                    if (mLayoutManager.findLastCompletelyVisibleItemPosition() >= totalItemCount - 1)
                        if (Netutil.isNetWorkAvailable(NewsActivity.this))
                            loadMoreNews();
                        else
                            Toast.makeText(NewsActivity.this, "请连接网络后加载", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void loadMoreNews() {
        currentPosition = mLayoutManager.findFirstVisibleItemPosition() + 1;
        loading = true; //防止加载时线程被多次启动
        new Thread(new Runnable() {
            @Override
            public void run() {



                        News[] old = mRvAdapter.getNews();
                        News[] current = jsonutil.getNewsBefore((GregorianCalendar)mCalendar.clone());
                        //assert old != null;
                        //assert current != null;
                        News[] all = new News[old.length + current.length];
                        System.arraycopy(old, 0, all, 0, old.length);
                        System.arraycopy(current, 0, all, old.length, current.length);
                        mNews = all;
                       // mRvAdapter.notifyDataSetChanged();
                        //mRvAdapter = new RvAdapter(mNews, mTopNews, NewsActivity.this);
                        //mRvAdapter.notifyDataSetChanged();
                        mHandler.sendEmptyMessage(MORE_NEWS);




            }
        }).start();


        //News[] current = Jsonutil.getNewsBefore((GregorianCalendar) mCalendar.clone());
        ;
    }



    @Override
    public void run() {
        //if (Netutil.isNetWorkAvailable(this)) {

          //  mTopNews = Jsonutil.parseTopNews();
           // mNews = Jsonutil.getNewsBefore(new GregorianCalendar());
        //} else {
            mTopNews = OfflineNews.getNews(this,OfflineNews.TOP_NEWS);
            mNews = OfflineNews.getNews(this,OfflineNews.NEWS);
       // }
        //mRvAdapter.notifyDataSetChanged();
        //mRvAdapter = new RvAdapter(mNews, mTopNews,NewsActivity.this);

        mHandler.sendEmptyMessage(REFRESHED);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        OfflineNews.saveNews(this, OfflineNews.NEWS, mNews);
        OfflineNews.saveNews(this, OfflineNews.TOP_NEWS, mTopNews);
    }
}
