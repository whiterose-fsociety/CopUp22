package com.example.copup2.Post;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.example.copup2.BaseAdapter.BaseActivity;
import com.example.copup2.MainActivity;
import com.example.copup2.Models.PostModels.Product;
import com.example.copup2.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import butterknife.BindView;

public class PostActivity extends BaseActivity implements SelectImageFragment.OnNextSelectedListener, SelectDetailsFragment.OnBackListener {

    private static final String TAG = PostActivity.class.getName();


    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_holder);
        initFragment();
    }


    /**
     * This method will launch the select image fragment
     */
    private void initFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.placeholder, new SelectImageFragment());
        fragmentTransaction.addToBackStack(getString(R.string.select_image_fragment));
        fragmentTransaction.commit();
    }


    @Override
    public void onNextClicked(Product product) {
        SelectDetailsFragment selectDetailsFragment = new SelectDetailsFragment();
        Bundle bundle = new Bundle();

        bundle.putParcelable(getString(R.string.product_parcelable), product);
        selectDetailsFragment.setArguments(bundle);


        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.placeholder, selectDetailsFragment);
        fragmentTransaction.addToBackStack(getString(R.string.select_details_fragment));
        fragmentTransaction.commit();
    }

    @Override
    public void navigateBack() {
        Log.d(TAG, "navigateBack: GOING BACK");
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.placeholder, new SelectImageFragment());
        fragmentTransaction.addToBackStack(getString(R.string.select_image_fragment));
        fragmentTransaction.commit();
    }
}

