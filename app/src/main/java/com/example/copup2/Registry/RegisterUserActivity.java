package com.example.copup2.Registry;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.example.copup2.BaseAdapter.BaseActivity;
import com.example.copup2.R;

public class RegisterUserActivity extends BaseActivity {
    private static final String TAG = RegisterUserActivity.class.getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_holder);
        initFragment();
    }


    /**
     * This method will launch the register user fragment
     */
    private void initFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.placeholder, new RegisterUserFragment());
        fragmentTransaction.addToBackStack(getString(R.string.register_user_fragment));
        fragmentTransaction.commit();
    }
}
