package com.example.copup2.FirebaseModels;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.copup2.Explore.ExploreActivity;
import com.example.copup2.Models.LoggedUser;
import com.example.copup2.Models.Phone;
import com.example.copup2.Models.User;
import com.example.copup2.R;
import com.example.copup2.Registry.LoginActivity;
import com.example.copup2.Registry.RegisterUserActivity;
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
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
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
import java.util.concurrent.TimeUnit;


public class Authentication {
    /**
     * Use the builder pattern
     */
    private static final String TAG = Authentication.class.getName();
    private static TextView mTextView;
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

    public static void fetchUser(String password, String QueryField, String Query) {
        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection(mFragment.getString(R.string.users_collection));
        Query query = collectionReference.whereEqualTo(QueryField, Query);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override

            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Log.d(TAG, "onComplete: COMPLETED THE SEARCH ");
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete: USER FOUND");

//                    BaseUserClass baseUserClass = new BaseUserClass();
                    ArrayList<User> users = new ArrayList<>();
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                        Log.d(TAG, "onComplete: VALUES " + queryDocumentSnapshot.getData());
                        User user = queryDocumentSnapshot.toObject(User.class);
                        users.add(user);
//                        Log.d(TAG, "onComplete: user " + user.toString());
                    }
                    if (users.size() > 0) {
                        Log.d(TAG, "onComplete: USER FROM USERNAME " + users.get(0).toString());
                        User loggedUser = users.get(0);
                        if (loggedUser.getPhone() != null) {
                            Phone phone = loggedUser.getPhone();
                            if ((phone.getPassword().equals(password))) {
                                /**
                                 * Logged
                                 */
                                Log.d(TAG, "onComplete: THE USER IS LOGGED IN " + loggedUser.getUsername());
                                Log.d(TAG, "onComplete: THE USER IS LOGGED IN WITH " + loggedUser.getPhone());
                                LoggedUser.setUser(loggedUser);
                                Intent intent = new Intent(mFragment.getActivity(), ExploreActivity.class);
                                mFragment.getActivity().startActivity(intent);
                            } else {
                                BottomSheetHelper.setMessage(mFragment.getString(R.string.username_not_exists));
                                BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, mTextView);
                            }
                        } else {
                            Log.d(TAG, "onComplete: NO PHONE BUT USER IS  " + loggedUser);
                            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                            firebaseAuth.signInWithEmailAndPassword(loggedUser.getEmail(), password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        try {
                                            throw task.getException();
                                        } catch (FirebaseAuthInvalidUserException e) {
                                            BottomSheetHelper.setMessage(mFragment.getString(R.string.username_not_exists));
                                            BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, mTextView);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                                            LoggedUser.setUser(loggedUser);
                                            Intent intent = new Intent(mFragment.getActivity(), ExploreActivity.class);
                                            mFragment.getActivity().startActivity(intent);
                                        } else {
                                            BottomSheetHelper.setMessage(mFragment.getString(R.string.email_not_verified));
                                            BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, mTextView);

                                        }
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    BottomSheetHelper.setMessage(mFragment.getString(R.string.error));
                                    BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, mTextView);
                                }
                            });
                        }


                    } else {
                        BottomSheetHelper.setMessage(mFragment.getString(R.string.username_not_exists));
                        BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, mTextView);
                    }

                } else {
                    BottomSheetHelper.setMessage(mFragment.getString(R.string.username_not_exists));
                    BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, mTextView);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                BottomSheetHelper.setMessage(mFragment.getString(R.string.error));
                BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, mTextView);
            }
        });
    }

    /**
     * General  Method For Fetching Authentication Data
     *
     * @param QueryField
     * @param Query
     */
    public static void fetchUser(String QueryField, String Query) {
//
//        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection(mFragment.getString(R.string.users_collection));
//        Query query = collectionReference.whereEqualTo(QueryField, Query);
//        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        Log.d(TAG, "onComplete: USER ID " + firebaseAuth.getCurrentUser().getUid());
        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection(mFragment.getString(R.string.users_collection));
        Query query = collectionReference.whereEqualTo(QueryField, Query);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete: SUCCESS: " + task.getResult());
                    ArrayList<User> users = new ArrayList<>();
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                        users.add(queryDocumentSnapshot.toObject(User.class));
                        Log.d(TAG, "onComplete: USER " + queryDocumentSnapshot.getData());
                    }

                    if (users.size() > 0) {
                        User user = users.get(0);
                        LoggedUser.setUser(user);
                        Intent intent = new Intent(mFragment.getActivity(), ExploreActivity.class);
                        mFragment.getActivity().startActivity(intent);
                    } else {
                        BottomSheetHelper.setMessage(mFragment.getString(R.string.username_not_exists));
                        BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, mTextView);
                    }
                } else {
                    Log.d(TAG, "onComplete: SOMETHING WENT WRONG");
                }
            }
        }).

                addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        BottomSheetHelper.setMessage(mFragment.getString(R.string.error));
                        BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, mTextView);
                    }
                });
    }

    /**
     * This method will check if the username exists and allow the user to sign in using his username or phone number
     *
     * @param
     */
    public static void signIn(String userInput, String password) {
        /**
         * Query for username
         */
        if (StringManipulation.isValidUsername(userInput)) {
            String QueryField = mFragment.getString(R.string.user_username);
            fetchUser(password, QueryField, userInput);
        } else if (StringManipulation.isNumberValid(userInput)) {
            Log.d(TAG, "fetchInformation: QUERY NUMBER " + StringManipulation.getNumber(userInput));
            String Query = StringManipulation.getNumber(userInput);
            Log.d(TAG, "fetchInformation: NUMBER " + Query);
            String QueryField = mFragment.getString(R.string.user_phone_number);
            fetchUser(password, QueryField, Query);

        } else if (StringManipulation.isValidEmailAddress(userInput)) {
            /**
             * #todo VERIFIY EMAIL ADDRESS
             */
            Log.d(TAG, "signIn: EMAIL " + userInput);
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signInWithEmailAndPassword(userInput, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
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
                            Log.d(TAG, "onComplete: LOGIN PROBLEM " + e.toString());
                            progressBar.setVisibility(View.INVISIBLE);
                            e.printStackTrace();
                        }
                    } else {

                        if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                            String Query = firebaseAuth.getCurrentUser().getUid();
                            String QueryField = mFragment.getString(R.string.user_id);
                            fetchUser(QueryField, Query);
                        } else {
                            Log.d(TAG, "onComplete: EMAIL IS NOT VERIFIED");
                            BottomSheetHelper.setMessage(mFragment.getString(R.string.email_not_verified));
                            BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, mTextView);
                        }
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    BottomSheetHelper.setMessage(mFragment.getString(R.string.error));
                    BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, mTextView);
                }
            });
        } else {
            BottomSheetHelper.setMessage(mFragment.getString(R.string.input_error));
            BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, mTextView);
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

    public static void signInWithNumber(String otp, Phone phone, String username, String password) {
//        if(phone.getCode() )
        phone.setPassword(password);
//        Log.d(TAG, "signIn: CLASS PHONE OTP CODE " + phone.getCode());
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(phone.getVerificationCode(), otp);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    progressBar.setVisibility(View.INVISIBLE);
                    try {
                        throw new Exception();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        BottomSheetHelper.setMessage(mFragment.getString(R.string.otp_error));
                        BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, mTextView);
                    } catch (FirebaseAuthUserCollisionException e) {
                        BottomSheetHelper.setMessage(mFragment.getString(R.string.number_exists));
                        BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, mTextView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    Log.d(TAG, "onComplete: SUCCESS " + firebaseAuth.getCurrentUser().getUid());
                    User user = new User();
                    user.setUserID(firebaseAuth.getCurrentUser().getUid());
                    user.setUsername(username);
                    user.setPhone(phone);
                    Log.d(TAG, "onComplete: USER IS " + user.toString());
                    Log.d(TAG, "onComplete: PHONE IS " + phone.toString());
                    addUser(user);
//                            Log.d(TAG, "onComplete: RESULT 1" + task.getResult().getCredential().toString());
//                            Log.d(TAG, "onComplete: RESULT 2" + task.getResult().getUser().toString());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                BottomSheetHelper.setMessage(mFragment.getString(R.string.error));
                BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, mTextView);
            }
        });
        Log.d(TAG, "onVerificationCompleted: PHONE INFO " + phone.toString());
    }


    public static void sendVerificationCode(Phone phone) {
        Log.d(TAG, "sendVerificationCode: SENDING PHONE");
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phone.getNumber(), 60, TimeUnit.SECONDS, mFragment.getActivity(), new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Log.d(TAG, "onVerificationCompleted: " + phoneAuthCredential);
//                //Getting the code sent by SMS
                String code = phoneAuthCredential.getSmsCode();
//                signIn(phone.getCode(),);
                //Sometimes the code is detected automatically
                if (code != null) {
                    Log.d(TAG, "onVerificationCompleted: OTP CODE: " + code);
                    phone.setCode(code);
                    Intent intent = new Intent(mFragment.getContext(), RegisterUserActivity.class);
                    intent.putExtra(mFragment.getString(R.string.number_object), phone);
                    mFragment.getActivity().startActivity(intent);
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
                phone.setVerificationCode(s);

                Log.d(TAG, "onCodeSent: Token " + forceResendingToken);
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(String s) {
                super.onCodeAutoRetrievalTimeOut(s);
                progressBar.setVisibility(View.INVISIBLE);
                BottomSheetHelper.setMessage(mFragment.getString(R.string.phone_too_many_requests));
                BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, mTextView);
            }
        });
    }

//    private static PhoneAuthProvider.OnVerificationStateChangedCallbacks Callback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//        Phone phone = new Phone();
//
//        @Override
//        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
//            Log.d(TAG, "onVerificationCompleted: " + phoneAuthCredential);
//            //Getting the code sent by SMS
//            String code = phoneAuthCredential.getSmsCode();
//
//            //Sometimes the code is detected automatically
//            if (code != null) {
//                Log.d(TAG, "onVerificationCompleted: OTP CODE: " + code);
//                phone.setCode(code);
//                mPhone.setCode(code);
//                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mPhone.getVerificationCode(), mPhone.getCode());
//                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//                firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (!task.isSuccessful()) {
//
//                            try {
//                                throw new Exception();
//                            } catch (FirebaseAuthInvalidCredentialsException e) {
//                                BottomSheetHelper.setMessage(mFragment.getString(R.string.otp_error));
//                                BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, mTextView);
//                            } catch (FirebaseAuthUserCollisionException e) {
//                                BottomSheetHelper.setMessage(mFragment.getString(R.string.number_exists));
//                                BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, mTextView);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        } else {
//                            Log.d(TAG, "onComplete: SUCCESS " + firebaseAuth.getCurrentUser().getUid());
//
////                            Log.d(TAG, "onComplete: RESULT 1" + task.getResult().getCredential().toString());
////                            Log.d(TAG, "onComplete: RESULT 2" + task.getResult().getUser().toString());
//                        }
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        BottomSheetHelper.setMessage(mFragment.getString(R.string.error));
//                        BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, mTextView);
//                    }
//                });
//                Log.d(TAG, "onVerificationCompleted: PHONE INFO " + mPhone.toString());
//            }
//
//        }
//
//        @Override
//        public void onVerificationFailed(FirebaseException e) {
//            if (e instanceof FirebaseAuthInvalidCredentialsException) {
//                //Invalid Request
//                BottomSheetHelper.setMessage(mFragment.getString(R.string.phone_invalid_request));
//                BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, mTextView);
//            } else if (e instanceof FirebaseTooManyRequestsException) {
//                //The SMS quota for the project has been exceeded
//                BottomSheetHelper.setMessage(mFragment.getString(R.string.phone_too_many_requests));
//                BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, mTextView);
//            }
//
//        }
//
//        @Override
//        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//            super.onCodeSent(s, forceResendingToken);
//            Log.d(TAG, "onCodeSent: OTP " + s);
//            phone.setVerificationCode(s);
//            mPhone.setVerificationCode(s);
//            Log.d(TAG, "onCodeSent: Token " + forceResendingToken);
//        }
//
//        @Override
//        public void onCodeAutoRetrievalTimeOut(String s) {
//            super.onCodeAutoRetrievalTimeOut(s);
//            progressBar.setVisibility(View.INVISIBLE);
//            BottomSheetHelper.setMessage(mFragment.getString(R.string.phone_too_many_requests));
//            BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, mTextView);
//        }
//    };


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

    public static void fetchLoggedUser(String userID) {
        Log.d(TAG, "fetchLoggedUser: ATTEMPTING TO FETCH USER INFORMATION ");
        if (LoggedUser.getUser() == null) {
            FirebaseFirestore userCollection = FirebaseFirestore.getInstance();
            CollectionReference user = userCollection.collection(mContext.getString(R.string.users_collection));
            Query query = user.whereEqualTo(mContext.getString(R.string.user_id), userID);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "onComplete: USER FOUND ");
                        ArrayList<User> users = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            Log.d(TAG, "onComplete: USER IS " + documentSnapshot.getData());
                            User foundUser = documentSnapshot.toObject(User.class);
                            users.add(foundUser);
                        }
                        if (users.size() > 0) {
                            User loggedUser = users.get(0);
                            LoggedUser.setUser(loggedUser);
                        } else {
                            Log.d(TAG, "onComplete: USER DOES NOT EXIST ");
                            Intent intent = new Intent(mContext, LoginActivity.class);
                            mContext.startActivity(intent);
                        }
                    } else {
                        Log.d(TAG, "onComplete: TASK UNSUCCESSFUL");
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    BottomSheetHelper.setMessage(mContext.getString(R.string.error));
                    BottomSheetHelper.createBottomSheet(mBottomSheetBehavior, mTextView);
                }
            });
        } else if (LoggedUser.getUser().getUserID().equals(userID)) {
            Log.d(TAG, "fetchLoggedUser: USER IS ALREADY LOGGED IN ");
        }
    }
}
