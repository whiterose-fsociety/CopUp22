package com.example.copup2.Registry;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.copup2.FirebaseModels.FirebaseHelper;
import com.example.copup2.R;
import com.example.copup2.Utilities.BottomSheetHelper;
import com.example.copup2.Utilities.StringManipulation;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgotPasswordFragment extends Fragment {
    private static final String TAG = ForgotPasswordFragment.class.getName();

    @BindView(R.id.email)
    EditText mEmail;
    @BindView(R.id.send_link)
    Button mSendLink;
    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.bottom_sheet)
    LinearLayout mBottomSheet;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.bottom_sheet_text)
    TextView Message;


    private BottomSheetBehavior mBottomSheetBehavior;
    private View mView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        ButterKnife.bind(this, mView);
        init();
        return mView;
    }

    @OnClick(R.id.back)
    public void back() {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.placeholder, new LoginFragment());
        fragmentTransaction.addToBackStack(getString(R.string.login_fragment));
        fragmentTransaction.commit();
    }


    private void init() {
        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
        FirebaseHelper.setFragment(this);
        FirebaseHelper.setContext(getContext());
        FirebaseHelper.setProgressBar(progressBar);
        FirebaseHelper.setBottomSheet(mBottomSheetBehavior, Message);
    }

    @OnClick(R.id.send_link)
    public void sendLink() {
        progressBar.setVisibility(View.VISIBLE);
        String email = mEmail.getText().toString().trim();
        if (StringManipulation.isValidEmailAddress(email)) {
            /**
             * Use FirebaseHelper to send link to email
             * If successful move back to the login screen
             */
            FirebaseHelper.sendPassword(email);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            BottomSheetHelper.setMessage(getString(R.string.email_input_error));
            BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, Message);
        }
    }
}
