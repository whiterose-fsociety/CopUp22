package com.example.copup2.Registry;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.copup2.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginFragment extends Fragment {
    private static final String TAG = LoginFragment.class.getName();


    private View mView;
    @BindView(R.id.bottom_sheet_text)
    TextView Message;
    @BindView(R.id.bottom_sheet)
    LinearLayout mBottomSheetView;
    @BindView(R.id.login_forgot_password)
    TextView mForgotPassword;
    @BindView(R.id.login_link_to_register)
    TextView mLink;
    BottomSheetBehavior mBottomSheetBehavior;

    /**
     * Called before the views are created
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Called First
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private void init() {
        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheetView);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, mView);
        return mView;
    }

    @OnClick(R.id.login_link_to_register)
    public void changeActivity() {
        Log.d(TAG, "changeActivity: changing activity");
        Intent intent = new Intent(getContext(), RegisterActivity.class);
        getActivity().startActivity(intent);
    }

    @OnClick(R.id.login_forgot_password)
    public void forgotPassword() {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.placeholder, new ForgotPasswordFragment());
        fragmentTransaction.addToBackStack(getString(R.string.forgot_password));
        fragmentTransaction.commit();
    }

}
