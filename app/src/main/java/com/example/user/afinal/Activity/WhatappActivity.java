package com.example.user.afinal.Activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.user.afinal.Fragments.homeFragment;
import com.example.user.afinal.Fragments.importantFragment;
import com.example.user.afinal.Fragments.photoFragment;
import com.example.user.afinal.R;
import com.example.user.afinal.Utility.MyApplication;
import com.example.user.afinal.Utility.ZoomOutPageTransformer;

import java.util.ArrayList;
import java.util.List;

public class WhatappActivity extends AppCompatActivity {
    private TabLayout tablayout;
    private ViewPager viewpager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whatapp);
        init();
        initcallback();

    }
    private void init() {
        try {

            tablayout =(TabLayout)findViewById(R.id.tabbar);

            viewpager= (ViewPager)findViewById(R.id.viewpager);

            tablayout.addTab(tablayout.newTab().setText("Home"));
            tablayout.addTab(tablayout.newTab().setText("Logger"));
            tablayout.addTab(tablayout.newTab().setText("Setting"));


        }catch (Exception ex){
            Log.e("init whatapp",ex.getMessage().toString());
        }
    }
    private void initcallback() {
        try {
            PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
            adapter.addFragment(new homeFragment());
            adapter.addFragment(new photoFragment());
            adapter.addFragment(new importantFragment());
            viewpager.setPageTransformer(true, new ZoomOutPageTransformer());
            viewpager.setAdapter(adapter);

            viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout));
            tablayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewpager));
        }catch (Exception ex){
            Log.e("initcallback whatapp",ex.getMessage().toString());
        }
    }


    class PagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragments = new ArrayList<>();


        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment){
            mFragments.add(fragment);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            String result = data.getStringExtra("SCAN_RESULT");
        //    MyApplication.getInstance().getBartext().setText(result);
        }


    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(WhatappActivity.this,DashboardActivity.class);
        startActivityForResult(i,500);
    }
}
