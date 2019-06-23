package com.example.copup2.Utilities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

public class Permission {
    private static final String TAG = "Permission";
    private static final String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    public static boolean CheckPermissionsArray(Context mContext){
        Log.d(TAG, "CheckPermissionsArray: ATTEMPTING TO CHECK THE PERMISSION ARRAY");
        for(String permission:permissions){
            if(!checkPermission(permission,mContext)){
                return false;
            }
        }
        return true;
    }
    public static void verify(Context mContext){
        if(!CheckPermissionsArray(mContext)){
            VerifyPermissions(mContext);
        }
    }

    private static boolean checkPermission(String permission, Context mContext){
        Log.d(TAG, "checkPermission: ATTEMPTING TO CHECK THE SINGE PERMISSIONS");
        int permissionRequest = ActivityCompat.checkSelfPermission(mContext,permission);
        if(permissionRequest  != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "checkPermission: PERMISSION WAS NOT GRANTED ");
            return false;
        }else{
            Log.d(TAG, "checkPermission: PERMISSION WAS GRANTED");
            return true;
        }


    }
    public static void VerifyPermissions(Context mContext){
        Log.d(TAG, "VerifyPermissions: Verifying permission");
        ActivityCompat.requestPermissions((Activity) mContext,permissions,1);
    }


}
