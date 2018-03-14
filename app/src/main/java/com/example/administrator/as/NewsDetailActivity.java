package com.example.administrator.as;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.administrator.as.R;
import com.example.administrator.as.DetailNewsVpAdapter;

public class NewsDetailActivity extends AppCompatActivity {
    private ImageButton mBack;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        mBack = (ImageButton) findViewById(R.id.back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        DetailNewsVpAdapter adapter = new DetailNewsVpAdapter(getSupportFragmentManager(), getIntent().getIntegerArrayListExtra("newsIds"), this);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(getIntent().getIntExtra("position", 0), false);
    }
}
