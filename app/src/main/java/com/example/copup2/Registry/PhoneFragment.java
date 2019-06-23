package com.example.copup2.Registry;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.copup2.FirebaseModels.Authentication;
import com.example.copup2.Models.Phone;
import com.example.copup2.R;
import com.example.copup2.Utilities.BottomSheetHelper;
import com.example.copup2.Utilities.StringManipulation;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhoneFragment extends Fragment {
    private static final String TAG = PhoneFragment.class.getName();

    private View mView;

    /**
     * Bind Views
     */
    @BindView(R.id.next)
    Button mRegister;

    @BindView(R.id.register_link_to_login)
    TextView mLink;

    @BindView(R.id.register_phone)
    EditText mPhone;

    @BindView(R.id.bottom_sheet)
    LinearLayout mBottomSheet;

    @BindView(R.id.bottom_sheet_text)
    TextView Message;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private BottomSheetBehavior mBottomSheetBehavior;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_phone_register, container, false);
        ButterKnife.bind(this, mView);
        initValues();
        return mView;
    }

    /**
     * Boilerplate code to initialise values
     */
    private void initValues() {
        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
        Authentication.setContext(getContext());
        Authentication.setFragment(this);
        Authentication.setBottomSheet(mBottomSheetBehavior, Message);
        Authentication.setProgressBar(progressBar);
    }

    @OnClick(R.id.register_link_to_login)
    public void moveToLogin() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        getActivity().startActivity(intent);
    }

    @OnClick(R.id.next)
    public void signUp() {
        progressBar.setVisibility(View.VISIBLE);
        String phoneNumber = mPhone.getText().toString().trim();
        if (StringManipulation.isNumberValid(phoneNumber)) {
            /**
             * Send Phone Authentication
             * Create Progress Bar
             * Send Message that link has been found
             * If Success
             * Move to Next Page
             * Else
             * Go Back and Ask them to Try Again
             *
             *
             * Keep the Stack Names so that you can go back without old information
             //             */

//            BottomSheetHelper.setMessage(getString(R.string.phone_input_success));
//            BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, Message);
//            Authentication.signUpWithNumber(phoneNumber);

////            #TODO send the number over the intent - Send the intent inside the Authentication
            Phone phone = new Phone();
            String n = StringManipulation.getNumber(phoneNumber);
            Log.d(TAG, "signUpWithNumber: VERIFICATION NUMBER " + n);
            phone.setNumber(n);
            Authentication.sendVerificationCode(phone);

        } else if (phoneNumber.equals("")) {
            progressBar.setVisibility(View.INVISIBLE);
            BottomSheetHelper.setMessage(getString(R.string.phone_input_empty));
            BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, Message);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            BottomSheetHelper.setMessage(getString(R.string.phone_input_error));
            BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, Message);
        }
    }
}
