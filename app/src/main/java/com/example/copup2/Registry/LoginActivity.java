package com.example.copup2.Registry;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.example.copup2.BaseAdapter.BaseActivity;
import com.example.copup2.Models.User;
import com.example.copup2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends BaseActivity {
    private static final String TAG = LoginActivity.class.getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_holder);
        initFragment();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            Log.d(TAG, "onCreate: USER ID " + firebaseAuth.getCurrentUser().getUid());
        } else {
            Log.d(TAG, "onCreate: USER IS NULL");
        }
        search();
    }

    private void search() {


    }

    /**
     * This method will launch the login fragment
     */
    private void initFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.placeholder, new LoginFragment());
        fragmentTransaction.addToBackStack(getString(R.string.login_fragment));
        fragmentTransaction.commit();
    }
}
