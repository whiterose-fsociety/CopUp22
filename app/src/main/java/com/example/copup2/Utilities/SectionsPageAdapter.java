package com.example.copup2.Utilities;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SectionsPageAdapter extends FragmentPagerAdapter {
    private static final String TAG = "SectionsPageAdapter";
    private List<Fragment> fragmentList = new ArrayList<>();

    public void addFragment(Fragment fragment) {
        fragmentList.add(fragment);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public SectionsPageAdapter(FragmentManager fm) {
        super(fm);
    }
}
