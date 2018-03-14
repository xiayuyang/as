package com.example.administrator.as;

/**
 * Created by Administrator on 2018/2/11.
 */


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.administrator.as.NewsDetailFragment;

import java.util.ArrayList;


public class DetailNewsVpAdapter extends FragmentStatePagerAdapter {
    private ArrayList<Integer> mIds;
    private Context mContext;

    public DetailNewsVpAdapter(FragmentManager fm, ArrayList<Integer> ids, Context context) {
        super(fm);
        mIds = ids;
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        NewsDetailFragment fragment = new NewsDetailFragment();
        fragment.mContext = mContext;
        fragment.showDetailNews(mIds.get(position));
        return fragment;
    }

    @Override
    public int getCount() {
        return mIds.size();
    }
}
