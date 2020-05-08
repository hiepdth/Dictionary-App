package com.hiepdt.dicitonaryapp.search.result;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.hiepdt.dicitonaryapp.R;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private ArrayList<Fragment> mListFragment;
    private ResultViewpagerAdapter mAdapter;

    private ImageView btnBack, btnMenu;
    private TextView tvWord;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        getSupportActionBar().hide();

        init();
        action();

    }

    private void init() {
        mViewPager = findViewById(R.id.mViewPager);
        mTabLayout = findViewById(R.id.mTabLayout);
        mListFragment = new ArrayList<>();
        mListFragment.add(new ContentFragment());
        mAdapter = new ResultViewpagerAdapter(getSupportFragmentManager(), mListFragment);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(0);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.getTabAt(0).setText("Nội dung");

        btnBack = findViewById(R.id.btnBack);
        btnMenu = findViewById(R.id.btnMenu);
        tvWord = findViewById(R.id.tvWord);

    }

    private void action() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tvWord.setText(getIntent().getExtras().getString("word", ""));
    }
}
