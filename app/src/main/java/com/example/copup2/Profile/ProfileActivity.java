package com.example.copup2.Profile;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.copup2.BaseAdapter.BaseActivity;
import com.example.copup2.R;
import com.example.copup2.Utilities.BottomNavigationViewHelper;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewInner;

public class ProfileActivity extends BaseActivity {
    private static final String TAG = ProfileActivity.class.getName();
    private static final int ACTIVITY_NUMBER = 3;
    private LinearLayout bottomSheet;
    private BottomSheetBehavior sheetBehavior;
    private BottomNavigationViewInner bottomNavigationViewEx;
    private Context context = this;


    private void init() {
        Log.d(TAG, "init: INIITIALISING VALUES ");
        bottomSheet = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomNavigationViewEx = findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.initNavigation(bottomNavigationViewEx, context, ACTIVITY_NUMBER, ProfileActivity.class);
//        bottomNavigationViewEx.enableShiftingMode(false);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();
    }
}
