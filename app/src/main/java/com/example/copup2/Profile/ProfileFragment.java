package com.example.copup2.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.copup2.Models.LoggedUser;
import com.example.copup2.Models.User;
import com.example.copup2.R;
import com.example.copup2.Registry.LoginActivity;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileFragment extends Fragment {
    private static final String TAG = ProfileFragment.class.getName();

    private View mView;

    User loggedUser;


    @BindView(R.id.username)
    TextView mUsername;
    @BindView(R.id.profile_image)
    ImageView mProfileImage;
    @BindView(R.id.options)
    ImageView mOptions;
    @BindView(R.id.followers)
    TextView mFollowers;
    @BindView(R.id.following)
    TextView mFollowing;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (LoggedUser.getInstance() != null) {
            if (LoggedUser.getUser() != null) {
                loggedUser = LoggedUser.getUser();
            } else {
                Log.d(TAG, "onCreate: USER IS NULL");
            }
        } else {
            Log.d(TAG, "onCreate: USER IS NULL");
//            Intent loginIntent = new Intent(getContext(), LoginActivity.class);
//            getActivity().startActivity(loginIntent);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, mView);
        fetchUser();
        return mView;
    }

    @OnClick(R.id.options)
    void navigate() {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.placeholder, new EditProfileFragment());
        fragmentTransaction.addToBackStack(getString(R.string.profile_fragment));
        fragmentTransaction.commit();
    }

    private void fetchProducts() {

    }

    private void fetchUser() {
        if (loggedUser != null) {
            mUsername.setText(loggedUser.getUsername());
            if (loggedUser.getProfile_image() != null) {
                Picasso.get().load(loggedUser.getProfile_image()).into(mProfileImage);
            }
            if (loggedUser.getFollowers() == null) {
                mFollowers.setText(String.valueOf(0));
            }
            if (loggedUser.getFollowing() == null) {
                mFollowing.setText(String.valueOf(0));
            }
        } else {
            Log.d(TAG, "init: USER DOES NOT EXIST");
        }
    }

}
