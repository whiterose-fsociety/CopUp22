package com.example.copup2.FirebaseModels;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.copup2.Explore.ExploreActivity;
import com.example.copup2.Models.Phone;
import com.example.copup2.Models.PostModels.Product;
import com.example.copup2.R;
import com.example.copup2.Utilities.BottomSheetHelper;
import com.example.copup2.Utilities.StringManipulation;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.api.LogDescriptor;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Post {
    private static final String TAG = Post.class.getName();
    private static TextView Message;
    private static Context mContext;
    private static Fragment mFragment;
    private static ProgressBar progressBar;
    private static BottomSheetBehavior mBottomSheetBehavior;
    private static Phone mPhone;

    public static void setFragment(Fragment fragment) {
        mFragment = fragment;
    }

    public static void setProgressBar(ProgressBar bar) {
        progressBar = bar;
    }

    public static void setBottomSheet(BottomSheetBehavior bottomSheet, TextView textView) {
        Message = textView;
        mBottomSheetBehavior = bottomSheet;
    }

    public static void setContext(Context context) {
        mContext = context;
    }


    public static void uploadImageFromGallery(Product product) {
        Uri imageUri = Uri.parse(product.getImage());
        Log.d(TAG, "uploadImageFromGallery: ATTEMPTING TO UPLOAD IMAGE TO STORAGE");
        if (imageUri != null) {
            final StorageReference storageReference = FirebaseStorage.getInstance().getReference("Products/");
            String fileStorage = product.getUserID() + "_" + product.getTitle() + "_" + System.currentTimeMillis() + "." + StringManipulation.getFileExtension(imageUri, mFragment.getContext());
            StorageReference reference = storageReference.child(fileStorage);
            UploadTask uploadTask = reference.putFile(imageUri);
            final Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    progressBar.setVisibility(View.VISIBLE);
                    Log.d(TAG, "onComplete: SUCCESSFUL UPLOAD TO THE STORAGE CLOUD ");
                    if (task.isSuccessful()) {
                        Log.d(TAG, "onComplete: UPLOAD SUCCESSFULL ");
                        Uri uploadUri = task.getResult();
                        product.setImage(uploadUri.toString());

                        /**
                         * #TODO ADD TO FIRESTORE - ROLLBACK ERRORS BY DELETING THE IMAGE IN THE STORAGE
                         */
                        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                        firebaseFirestore.collection(mFragment.getString(R.string.posts_collection))
                                .add(product)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Log.d(TAG, "onSuccess: DOCUMENT written with ID: " + documentReference.getId());
                                        Intent exploreIntent = new Intent(mFragment.getActivity(), ExploreActivity.class);
                                        mFragment.getActivity().startActivity(exploreIntent);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                /**
                                 * #TODO DELETE THE IMAGE IN STORAGE
                                 */
                                BottomSheetHelper.setMessage(mFragment.getString(R.string.error));
                                BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, Message);
                            }
                        });
                        Log.d(TAG, "onComplete: FULL PRODUCT UPLOAD " + product.toString());
                    } else {
                        BottomSheetHelper.setMessage(mFragment.getString(R.string.upload_error));
                        BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, Message);
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    BottomSheetHelper.setMessage(mFragment.getString(R.string.upload_error));
                    BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, Message);
                }
            });

        } else {
            BottomSheetHelper.setMessage(mFragment.getString(R.string.post_empty_uri));
            BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, Message);
        }

    }
}
