package com.example.copup2.Post;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.copup2.Explore.ExploreActivity;
import com.example.copup2.Models.LoggedUser;
import com.example.copup2.Models.PostModels.Product;
import com.example.copup2.R;
import com.example.copup2.Utilities.BottomSheetHelper;
import com.example.copup2.Utilities.Permission;
import com.example.copup2.Utilities.StringManipulation;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.io.IOError;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectImageFragment extends Fragment {
    private static final String TAG = SelectImageFragment.class.getName();
    private static final int GALLERY_REQUEST_CODE = 144;
    /**
     * This interface will allow the user to move from select image to select details
     */
    private OnNextSelectedListener listener;

    public interface OnNextSelectedListener {
        void onNextClicked(Product product);
    }

    @BindView(R.id.bottom_sheet_text)
    TextView Message;
    @BindView(R.id.bottom_sheet)
    LinearLayout mBottomSheet;


    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.post_image)
    ImageView mImage;
    @BindView(R.id.post_description)
    EditText mDescription;
    @BindView(R.id.post_brand)
    EditText mBrand;
    @BindView(R.id.post_location)
    EditText mLocation;


    private Uri mImageUri;

    /**
     * Store the listener (activity) that will have events fired once the fragment is attached
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNextSelectedListener) {
            listener = (OnNextSelectedListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement OnNextSelectedListener.OnNextClicekd");
        }
    }

    @BindView(R.id.post_next)
    Button mNext;

    private BottomSheetBehavior mBottomSheetBehavior;

    private View mView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_select_product, container, false);
        ButterKnife.bind(this, mView);
        init();
        return mView;
    }


    @OnClick(R.id.back)
    public void back() {
        getActivity().finish();
    }

    @OnClick(R.id.post_next)
    public void next() {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (userID != null) {
            String description = mDescription.getText().toString().trim();
            String brand = mBrand.getText().toString().trim();
            String location = mLocation.getText().toString().trim();

            if (!description.isEmpty() && !brand.isEmpty() && !location.isEmpty()) {
                if (StringManipulation.isValidProductTitle(brand)) {
                    Product product = new Product.Builder(mImageUri.toString())
                            .hasDescription(description)
                            .hasTitle(brand)
                            .hasLocation(location)
                            .hasUserID(userID)
                            .build();

                    listener.onNextClicked(product);
                } else {
                    BottomSheetHelper.setMessage(getString(R.string.input_error));
                    BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, Message);
                }
            } else {
                BottomSheetHelper.setMessage(getString(R.string.input_empty));
                BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, Message);
            }
        } else {
            BottomSheetHelper.setMessage(getString(R.string.error));
            BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, Message);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data.getData() != null) {
                mImageUri = data.getData();
                Picasso.get().load(mImageUri).centerCrop().fit().into(mImage);
                /**
                 * Upload to Firebase Storage
                 */
            } else {

            }
        }
    }

    @OnClick(R.id.post_image)
    public void chooseFromGallery() {
        Log.d(TAG, "chooseFromGallery: ATTEMPTING TO GET AN IMAGE FROM GALLERY");
        //Create an Intent with action as ACTION_PICK
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        //Sets the type as image/*. This ensures only only components of type image are selected
        galleryIntent.setType("image/*");

        String[] mimeTypes = {"image/jpeg", "image/png"};
        galleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        //Launch the Intent
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);

    }

    private String getfileExtension(Uri uri) {
        String extension;
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        extension = mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
        return extension;
    }

    /**
     * #TODO ADD A "TRADE" SECTION WHERE USERS CAN TRADE CLOTHING WITHOUT CASH
     */
    private void init() {
        Log.d(TAG, "init: ATTEMPTING TO INITIALISE VALUES");
        Permission.verify(getContext());
        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
        if (LoggedUser.getUser() != null) {
            Log.d(TAG, "init: USER " + LoggedUser.getUser().toString());
        } else {
            Log.d(TAG, "init: USER IS NULL ");
        }
    }
}
