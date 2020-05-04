package com.hiepdt.dicitonaryapp.search.result;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ResultViewpagerAdapter  extends FragmentPagerAdapter {
    private ArrayList<Fragment> mListFragment;

    public ResultViewpagerAdapter(FragmentManager fm, ArrayList<Fragment> mListFragment) {
        super(fm);
        this.mListFragment = mListFragment;
    }

    @Override
    public Fragment getItem(int position) {
        return this.mListFragment.get(position);
    }

    @Override
    public int getCount() {
        return this.mListFragment.size();
    }
}
