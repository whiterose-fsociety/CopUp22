package com.example.copup2.Profile;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

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

//        bottomNavigationViewEx.enableShiftingMode(false);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.placeholder, new ProfileFragment());
        fragmentTransaction.addToBackStack(getString(R.string.profile_fragment));
        fragmentTransaction.commit();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_holder);
        init();
    }
}
