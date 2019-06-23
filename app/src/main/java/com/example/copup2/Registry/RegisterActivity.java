package com.example.copup2.Registry;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.example.copup2.BaseAdapter.BaseActivity;
import com.example.copup2.R;
import com.example.copup2.Utilities.SectionsPageAdapter;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends BaseActivity {
    private static final String TAG = RegisterActivity.class.getCanonicalName();

    @BindView(R.id.container)
    ViewPager mViewPager;

    @BindView(R.id.tabs)
    TabLayout mTabs;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        setupTabs();
    }


    private void setupTabs() {
        SectionsPageAdapter sectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        sectionsPageAdapter.addFragment(new EmailFragment());
        sectionsPageAdapter.addFragment(new PhoneFragment());
        mViewPager.setAdapter(sectionsPageAdapter);
        mTabs.setupWithViewPager(mViewPager);
        mTabs.getTabAt(0).setIcon(R.drawable.ic_email);
        mTabs.getTabAt(1).setIcon(R.drawable.ic_mobile_number);

    }
}

