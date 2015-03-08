package com.example.EECS_581.ecc_android_app;

import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import java.util.ArrayList;
import java.util.List;

import adapter.MyPagerAdapter;


public class MainActivity extends SlidingFragmentActivity{

    //The ViewPager content
    private ViewPager mPager;

    //Tab list
    private List<View> listViews;

    private ImageView cursor;

    private TextView t1,t2,t3;

    private int offset=0;

    private  int currIndex=0;

    private int bmpW;
    
    private SlidingMenu leftContent;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setBehindContentView(R.layout.leftmenu);
        initSlidingMenu();
        InitImageView();
        InitTextView();
        InitViewPager();

    }


    public void initSlidingMenu(){
        leftContent=new SlidingMenu(this);
        leftContent.setMode(SlidingMenu.LEFT);
        leftContent.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        leftContent.setShadowWidthRes(R.dimen.shadow_width);
        leftContent.setShadowDrawable(R.drawable.shadow);
        leftContent.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        leftContent.setFadeDegree(0.35f);
        //setContentView(R.layout.activity_main);
        //setBehindContentView(R.layout.leftmenu);
        leftContent.attachToActivity(this,SlidingMenu.SLIDING_CONTENT);
        leftContent.setMenu(R.layout.leftmenu);

        //leftMenu=new SlidingMenu(this);
    }


    private void InitTextView(){
        t1 = (TextView) findViewById(R.id.text1);
        t2 = (TextView) findViewById(R.id.text2);
        t3 = (TextView) findViewById(R.id.text3);
        t1.setOnClickListener(new MyOnClickListener(0));
        t2.setOnClickListener(new MyOnClickListener(1));
        t3.setOnClickListener(new MyOnClickListener(2));
    }

    private void InitViewPager() {
        mPager = (ViewPager) findViewById(R.id.vPager);
        listViews = new ArrayList<View>();
        LayoutInflater mInflater = getLayoutInflater();
        listViews.add(mInflater.inflate(R.layout.activity_company_list, null));
        listViews.add(mInflater.inflate(R.layout.activity_map, null));
        listViews.add(mInflater.inflate(R.layout.activity_compose_note, null));
        mPager.setAdapter(new MyPagerAdapter(listViews));
        mPager.setCurrentItem(0);
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    private void InitImageView() {
        cursor = (ImageView) findViewById(R.id.cursor);
        bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.a)
                .getWidth();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        offset = (screenW / 3 - bmpW) / 2;
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        cursor.setImageMatrix(matrix);
    }

    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            mPager.setCurrentItem(index);
        }
    };

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        int one = offset * 2 + bmpW;
        int two = one * 2;
        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
                case 0:
                    if (currIndex == 1) {
                        animation = new TranslateAnimation(one, 0, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, 0, 0, 0);
                    }
                    break;
                case 1:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, one, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, one, 0, 0);
                    }
                    break;
                case 2:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, two, 0, 0);
                    } else if (currIndex == 1) {
                        animation = new TranslateAnimation(one, two, 0, 0);
                    }
                    break;
            }
            currIndex = arg0;
            animation.setFillAfter(true);
            animation.setDuration(300);
            cursor.startAnimation(animation);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }
}