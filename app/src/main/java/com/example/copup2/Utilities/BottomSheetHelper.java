package com.example.copup2.Utilities;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class BottomSheetHelper {
    private static String TAG = BottomSheetHelper.class.getName();
    private static String Message;

    public static void setMessage(String message) {
        Message = message;
    }

    public static void createBottomSheet(final BottomSheetBehavior sheetBehavior, final TextView textView) {
        textView.setText(Message);
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        sheetBehavior.setHideable(true);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (i == BottomSheetBehavior.STATE_EXPANDED) {
                    try {
                        Thread.sleep(1000);
                        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

    }

    public static void createBottomSheet(final BottomSheetBehavior sheetBehavior) {
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        sheetBehavior.setHideable(true);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (i == BottomSheetBehavior.STATE_EXPANDED) {
                    try {
                        Thread.sleep(1000);
                        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }
}
