package com.example.copup2.Utilities;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.copup2.Explore.ExploreActivity;
import com.example.copup2.Notifications.NotificationActivity;
import com.example.copup2.Post.PostActivity;
import com.example.copup2.Profile.ProfileActivity;
import com.example.copup2.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewInner;

public class BottomNavigationViewHelper {
    private static final String TAG = BottomNavigationViewHelper.class.getName();

    public static void setupBottomNavigation(BottomNavigationViewInner bottomNavigationViewEx) {
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(false);
    }

    public static void setItem(BottomNavigationViewInner bottomNavigationViewEx, int activityNumber) {
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(activityNumber);
        menuItem.setChecked(true);
    }

    public static void changeActivity(BottomNavigationViewInner bottomNavigationViewEx, final Context context, Class<? extends AppCompatActivity> activity) {
        if (activity.getClass().getName().equals(context.getClass().getName())) {
            Log.d(TAG, "changeActivity: SAME ACTIVITY");
        } else {
            bottomNavigationViewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.id_explore:
                            Intent exploreIntent = new Intent(context, ExploreActivity.class);
                            context.startActivity(exploreIntent);
                            break;
                        case R.id.id_post:
                            Intent postIntent = new Intent(context, PostActivity.class);
                            context.startActivity(postIntent);
                            break;
                        case R.id.id_notifications:
                            Intent notificationIntent = new Intent(context, NotificationActivity.class);
                            context.startActivity(notificationIntent);
                            break;
                        case R.id.id_profile:
                            Intent profileIntent = new Intent(context, ProfileActivity.class);
                            context.startActivity(profileIntent);
                            break;
                    }
                    return true;
                }
            });
        }
    }

    /**
     * #TODO handle case where you tap the icon of the activity you are in
     *
     * @param bottomNavigationViewEx
     * @param context
     * @param activityNumber
     * @param activity
     */
    public static void initNavigation(BottomNavigationViewInner bottomNavigationViewEx, Context context, int activityNumber, Class<? extends AppCompatActivity> activity) {
        setupBottomNavigation(bottomNavigationViewEx);
        setItem(bottomNavigationViewEx, activityNumber);
        changeActivity(bottomNavigationViewEx, context, activity);
    }

}
