package com.example.copup2.FirebaseModels;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.copup2.Models.User;
import com.example.copup2.R;
import com.example.copup2.Registry.LoginActivity;
import com.example.copup2.Utilities.BottomSheetHelper;
import com.example.copup2.Utilities.StringManipulation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.MissingFormatArgumentException;
import java.util.concurrent.TimeUnit;


public class FirebaseHelper {
    /**
     * Use the builder pattern
     */
    private static final String TAG = FirebaseHelper.class.getName();
    private static TextView mTextView;
    private static Context mContext;
    private static Fragment mFragment;
    private static ProgressBar progressBar;
    private static BottomSheetBehavior mBottomSheetBehavior;

    public static void setFragment(Fragment fragment) {
        mFragment = fragment;
    }

    public static void setProgressBar(ProgressBar bar) {
        progressBar = bar;
    }

    public static void setBottomSheet(BottomSheetBehavior bottomSheet, TextView textView) {
        mTextView = textView;
        mBottomSheetBehavior = bottomSheet;
    }

    public static void setContext(Context context) {
        mContext = context;
    }

    public static void signUpWithEmail(String email, String password, String username) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth != null) {
            Log.d(TAG, "signUpWithEmail: AUTH " + mAuth.toString());
            Log.d(TAG, "signUpWithEmail: USERNAME AND PASSWORD " + username + " " + password);
            CollectionReference collectionReference = FirebaseFirestore.getInstance().collection(mFragment.getString(R.string.users_collection));
            Query query = collectionReference.whereEqualTo(mFragment.getString(R.string.user_username), username);
            query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    Log.d(TAG, "onSuccess: QUERY " + queryDocumentSnapshots.getDocuments().toString());
                    List<User> userList = new ArrayList<>();
                    for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
//                    Log.d(TAG, "onSuccess: QUERIES ARE: " + queryDocumentSnapshot.getData());
                        User user = queryDocumentSnapshot.toObject(User.class);
                        userList.add(user);
//                    Log.d(TAG, "onSuccess: USERS ARE: " + user.toString());
                    }
                    Log.d(TAG, "onSuccess: SIZE " + userList.size());
                    if (userList.size() > 0) {
                        String sharedUsername = userList.get(0).getUsername();
                        Log.d(TAG, "onSuccess: USERNAME ALREADY TAKEN: " + sharedUsername);
                        progressBar.setVisibility(View.INVISIBLE);
                        BottomSheetHelper.setMessage(mFragment.getString(R.string.username_exists));
                        BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, mTextView);
                    } else {
                        Log.d(TAG, "onSuccess: USERNAME DOES NOT EXIST");
                        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {

                                    try {
                                        throw task.getException();
                                    } catch (FirebaseAuthEmailException e) {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        BottomSheetHelper.setMessage(mFragment.getString(R.string.wrong_email));
                                        BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, mTextView);
                                    } catch (FirebaseAuthUserCollisionException e) {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        BottomSheetHelper.setMessage(mFragment.getString(R.string.email_exists));
                                        BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, mTextView);
                                    } catch (Exception e) {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        e.printStackTrace();
                                    }
                                } else {
                                    Log.d(TAG, "onComplete: SIGN UP SUCCESS");
                                    String userID = mAuth.getCurrentUser().getUid();
                                    Log.d(TAG, "onComplete: USER ID " + userID);
                                    verifyEmail(email, username, userID);
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: SOMETHING WENT WRONG");
                                BottomSheetHelper.setMessage(mFragment.getString(R.string.error));
                                BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, mTextView);
                            }
                        });
                    }


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: SOMETHING WENT WRONG");
                }
            });

        } else {
            Log.d(TAG, "signUpWithEmail: FIREBASE IS NULL");
        }
    }

    public static void sendPassword(String email) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthUserCollisionException e) {
                        BottomSheetHelper.setMessage(mFragment.getString(R.string.email_exists));
                        BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, mTextView);
                        progressBar.setVisibility(View.INVISIBLE);
                    } catch (FirebaseAuthEmailException e) {
                        BottomSheetHelper.setMessage(mFragment.getString(R.string.wrong_email));
                        BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, mTextView);
                        progressBar.setVisibility(View.INVISIBLE);
                    } catch (Exception e) {
                        e.printStackTrace();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                } else {
                    BottomSheetHelper.setMessage(mFragment.getString(R.string.password));
                    BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, mTextView);
                    progressBar.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(mFragment.getActivity(), LoginActivity.class);
                    mFragment.getActivity().startActivity(intent);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: SOMETHING WENT WRONG");
                BottomSheetHelper.setMessage(mFragment.getString(R.string.error));
                BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, mTextView);
            }
        });

    }

    public static void signUpWithNumber(String number) {
//        String n = "+27798186388";
        String n = StringManipulation.getNumber(number);
        Log.d(TAG, "signUpWithNumber: VERIFICATION NUMBER " + n);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(n, 60, TimeUnit.SECONDS, mFragment.getActivity(), Callback);
    }

    private static PhoneAuthProvider.OnVerificationStateChangedCallbacks Callback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            Log.d(TAG, "onVerificationCompleted: " + phoneAuthCredential);
            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();
            //Sometimes the code is detected automatically
            if (code != null) {
                Log.d(TAG, "onVerificationCompleted: OTP CODE: " + code);
            }

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                //Invalid Request
                BottomSheetHelper.setMessage(mFragment.getString(R.string.phone_invalid_request));
                BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, mTextView);
            } else if (e instanceof FirebaseTooManyRequestsException) {
                //The SMS quota for the project has been exceeded
                BottomSheetHelper.setMessage(mFragment.getString(R.string.phone_too_many_requests));
                BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, mTextView);
            }

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            Log.d(TAG, "onCodeSent: OTP " + s);
            Log.d(TAG, "onCodeSent: Token " + forceResendingToken);
        }
    };


    public static void verifyEmail(String email, String username, String userID) {
        Log.d(TAG, "verifyEmail: ATTEMPTING TO SEND A VERIFICATION EMAIL");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d(TAG, "onComplete: COMPLETE");

                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete: EMAIL VERIFICATION SENT TO ID: " + userID);
                    BottomSheetHelper.setMessage(mFragment.getString(R.string.email_verification_success));
                    BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, mTextView);
//                    User user = new User(email, null, null, null, userID, username, null,null);
                    User user = new User(email, userID, username);
                    addUser(user);
                } else {
                    /**
                     * If there's an error then delete the user object from the database
                     */
                    BottomSheetHelper.setMessage(mFragment.getString(R.string.email_verification_error));
                    BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, mTextView);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                BottomSheetHelper.setMessage(mFragment.getString(R.string.email_verification_error));
                BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, mTextView);
            }
        });

    }

    public static void addUser(User user) {
        Log.d(TAG, "addUser: ATTEMPTING TO ADD THE USER");
        /**
         * Remember that it's asynchronous
         */


        /**
         * Check if user does not already exist
         * Query the database
         *
         * If there's an error in adding the user then rollback the authentication and send the user back
         */

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection(mFragment.getString(R.string.users_collection))
                .document(user.getUserID()).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: USER SUCCESSFULLY ADDED " + user.toString());
                        BottomSheetHelper.setMessage(mFragment.getString(R.string.user_success));
                        BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, mTextView);
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(mFragment.getContext(), LoginActivity.class);
                        mFragment.getActivity().startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                /**
                 * Rollback changes
                 */
                BottomSheetHelper.setMessage(mFragment.getString(R.string.error));
                BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, mTextView);
            }
        });
    }

}
