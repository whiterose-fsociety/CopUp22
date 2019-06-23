package com.example.copup2.Registry;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.copup2.R;
import com.example.copup2.Utilities.BottomSheetHelper;
import com.example.copup2.Utilities.StringManipulation;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EmailFragment extends Fragment {
    private static final String TAG = "EmailFragment";

    private View mView;

    @BindView(R.id.register_email_address)
    EditText mEmail;

    @BindView(R.id.next)
    Button mNext;

    @BindView(R.id.register_link_to_login)
    TextView mLink;

    @BindView(R.id.bottom_sheet)
    LinearLayout mBottomSheet;

    @BindView(R.id.bottom_sheet_text)
    TextView Message;

    private BottomSheetBehavior mBottomSheetBehavior;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_email_register, container, false);
        ButterKnife.bind(this, mView);
        init();
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * This method is responsible for initialising values
     */
    private void init() {
        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
    }

    @OnClick(R.id.register_link_to_login)
    public void moveToLogin() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        getActivity().startActivity(intent);
    }

    @OnClick(R.id.next)
    public void register() {
        String email = mEmail.getText().toString().trim();
        if (StringManipulation.isValidEmailAddress(email)) {
            /**
             * Send Email
             * Create Progress Bar
             * Send Message that link has been found`x
             * If Success
             * Move to Next Page
             * Else
             * Go Back and Ask them to Try Again
             *
             *
             * Keep the Stack Names so that you can go back without old information
             */
//            BottomSheetHelper.setMessage(getString(R.string.phone_input_success));
//            BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, Message);
            Intent intent = new Intent(getContext(), RegisterUserActivity.class);
            /**
             * #TODO send the email over the intent - Send the intent inside the Authentication
             */
            intent.putExtra(getString(R.string.email), email);
            getActivity().startActivity(intent);
        } else if (email.equals("")) {
            BottomSheetHelper.setMessage(getString(R.string.email_input_empty));
            BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, Message);

        } else {
            BottomSheetHelper.setMessage(getString(R.string.email_input_error));
            BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, Message);
        }
    }


    private void changeFragment() {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.placeholder, new LoginFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


}
