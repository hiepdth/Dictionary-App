package com.hiepdt.dicitonaryapp.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.hiepdt.dicitonaryapp.R;
import com.hiepdt.dicitonaryapp.bookmark.BookmarkFragment;
import com.hiepdt.dicitonaryapp.history.HistoryFragment;
import com.hiepdt.dicitonaryapp.search.SearchFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ViewPagerAdapter mainViewPagerAdapter;
    ArrayList<Fragment> mListFragment;
    ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        mViewPager = findViewById(R.id.mViewPager);


        mListFragment = new ArrayList<>();
        mListFragment.add(new HistoryFragment());
        mListFragment.add(new SearchFragment());
        mListFragment.add(new BookmarkFragment());


        //------------Set ViewPager --------------------//
        mainViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mListFragment);
        mViewPager.setAdapter(mainViewPagerAdapter);
        mViewPager.setCurrentItem(1);
    }
}
