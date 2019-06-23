package com.example.copup2.Registry;

import android.os.Bundle;
import android.util.Log;
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

import com.example.copup2.FirebaseModels.Authentication;
import com.example.copup2.Models.Phone;
import com.example.copup2.R;
import com.example.copup2.Utilities.BottomSheetHelper;
import com.example.copup2.Utilities.StringManipulation;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterUserFragment extends Fragment {
    private static final String TAG = RegisterUserFragment.class.getName();

    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.register_username)
    EditText mUsername;
    @BindView(R.id.register_password)
    EditText mPassword;
    @BindView(R.id.register_btn)
    Button mRegister;
    @BindView(R.id.bottom_sheet_text)
    TextView Message;
    @BindView(R.id.bottom_sheet)
    LinearLayout mBottomSheet;
    @BindView(R.id.register_otp)
    EditText mOTP;
    private BottomSheetBehavior mBottomSheetBehavior;

    private FirebaseAuth mAuth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(getContext());
        mAuth = FirebaseAuth.getInstance();
        if (mAuth != null) {
            Log.d(TAG, "onCreate: NOT NULL");
        } else {
            Log.d(TAG, "onCreate: NULL");
        }
    }

    private View mView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_user_register, container, false);
        ButterKnife.bind(this, mView);
        init();

        return mView;
    }

    /**
     * This method initialises values
     */
    private void init() {
        if (getActivity().getIntent().hasExtra(getString(R.string.number_object))) {
            mOTP.setVisibility(View.VISIBLE);
        }
        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
        Authentication.setContext(getContext());
        Authentication.setFragment(this);
        Authentication.setProgressBar(progressBar);
        Authentication.setBottomSheet(mBottomSheetBehavior, Message);
    }

    @OnClick(R.id.back)
    public void back() {
        getActivity().finish();
    }


    @OnClick(R.id.register_btn)
    public void addUser() {
        progressBar.setVisibility(View.VISIBLE);
        String username = mUsername.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        if (!username.equals("") && !password.equals("")) {
            if (StringManipulation.isValidUsername(username) && StringManipulation.isValidPassword(password)) {
                /**
                 * Create a progress bar
                 * Create a new User Object
                 * Log out User
                 * Take them to Login
                 */
                String registerInput = "";
                if (getActivity().getIntent().hasExtra(getString(R.string.email))) {
                    registerInput = getActivity().getIntent().getStringExtra(getString(R.string.email));
                    Log.d(TAG, "onCreateView: EMAIL " + registerInput);
                    if (!registerInput.equals("")) {
                        Authentication.signUpWithEmail(registerInput, password, username);
                    } else {
                        BottomSheetHelper.setMessage(getString(R.string.register_error_message));
                        BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, Message);
                    }

                } else if (getActivity().getIntent().hasExtra(getString(R.string.number_object))) {
                    if (getActivity().getIntent().getParcelableExtra(getString(R.string.number_object)) != null) {
                        String otp = mOTP.getText().toString().trim();
                        Phone phone = getActivity().getIntent().getParcelableExtra(getString(R.string.number_object));
                        Log.d(TAG, "addUser: PHONE OBJECT " + phone.toString());
                        Authentication.signInWithNumber(otp, phone, username, password);
                    }
                }

            } else {

                BottomSheetHelper.setMessage(getString(R.string.input_error));
                BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, Message);

            }
        } else {
            BottomSheetHelper.setMessage(getString(R.string.input_empty));
            BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, Message);
        }
//        progressBar.setVisibility(View.INVISIBLE);
    }
}
