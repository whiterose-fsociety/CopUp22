package com.example.copup2.Profile;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.copup2.Models.LoggedUser;
import com.example.copup2.Models.User;
import com.example.copup2.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditProfileFragment extends Fragment {
    private static final String TAG = EditProfileFragment.class.getName();
    private static final int GALLERY_REQUEST_CODE = 144;


    private View mView;

    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.bottom_sheet)
    LinearLayout mBottomSheet;
    @BindView(R.id.bottom_sheet_text)
    TextView Message;
    @BindView(R.id.save)
    ImageView Save;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;


    @BindView(R.id.username)
    EditText mUsername;
    @BindView(R.id.location)
    EditText mLocation;
    @BindView(R.id.fullname)
    EditText mFullname;
    @BindView(R.id.profile_image)
    ImageView mProfileImage;
    @BindView(R.id.change_photo)
    TextView mChangePhoto;


    private Uri mImageUri;
    private User loggedUser;

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        ButterKnife.bind(this, mView);
        return mView;
    }


    @OnClick(R.id.back)
    void back() {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.placeholder, new ProfileFragment());
        fragmentTransaction.addToBackStack(getString(R.string.edit_profile_fragment));
        fragmentTransaction.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data.getData() != null) {
                mImageUri = data.getData();
                Picasso.get().load(mImageUri).centerCrop().fit().into(mProfileImage);
                /**
                 * Upload to Firebase Storage
                 */
            } else {

            }
        }
    }

    @OnClick(R.id.save)
    void save() {
        String username = mUsername.getText().toString().trim();
        String location = mLocation.getText().toString().trim();
        String fullname = mFullname.getText().toString().trim();
        String uri = (mImageUri == null) ? null : mImageUri.toString();
        String imageUrl = (loggedUser.getProfile_image() == null) ? uri : loggedUser.getProfile_image();
        User user = new User();
        user.setUsername(username);
        user.setLocation(location);
        user.setFullname(fullname);
        user.setProfile_image(imageUrl);
        Log.d(TAG, "save: USER " + user.toString());

    }

    @OnClick(R.id.change_photo)
    void chooseFromGallery() {
        Log.d(TAG, "chooseFromGallery: CHOOSING FROM GALLERY");
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        galleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }


}
