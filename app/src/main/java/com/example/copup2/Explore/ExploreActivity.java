package com.example.copup2.Explore;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.example.copup2.BaseAdapter.BaseActivity;
import com.example.copup2.FirebaseModels.Authentication;
import com.example.copup2.R;
import com.example.copup2.Registry.LoginActivity;
import com.example.copup2.Utilities.BottomNavigationViewHelper;
import com.example.copup2.Utilities.SectionsPageAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewInner;


public class ExploreActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "ExploreActivity";
    private static final int ACTIVITY_NUMBER = 0;
    private LinearLayout bottomSheet;
    private BottomSheetBehavior sheetBehavior;
    private BottomNavigationViewInner bottomNavigationViewEx;
    private TextView Message;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Context context = this;


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {
            String userID = firebaseAuth.getCurrentUser().getUid();
            Authentication.fetchLoggedUser(userID);

        }
    }

    private void init() {
        /**
         * #TODO DISABLE BACK BUTTON FROM LOGIN ACTIVITY
         */
        Message = findViewById(R.id.bottom_sheet_text);
        bottomSheet = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomNavigationViewEx = findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.initNavigation(bottomNavigationViewEx, context, ACTIVITY_NUMBER, ExploreActivity.class);
        viewPager = findViewById(R.id.container);
        tabLayout = findViewById(R.id.tabs);
        Authentication.setContext(this);
        Authentication.setBottomSheet(sheetBehavior, Message);
        /**
         * #TODO IF LOGGEDUSER.GETUSER() IS NULL THEN FETCH INFORMATION AND SET USER
         */

//        bottomNavigationViewEx.enableShiftingMode(false);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);
        init();
        setupTab();

    }

    private void setupTab() {
        SectionsPageAdapter pageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        pageAdapter.addFragment(new HomeFragment());
        pageAdapter.addFragment(new FilterFragment());
        viewPager.setAdapter(pageAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_explore);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_filter);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }


}
