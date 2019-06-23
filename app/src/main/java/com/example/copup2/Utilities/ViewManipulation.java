package com.example.copup2.Utilities;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.copup2.R;

public class ViewManipulation {
    private Context context;

    public ViewManipulation(Context context) {
        this.context = context;
    }

    public void initSpinner(String[] strings,Spinner spinner) {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, R.layout.spinner_item, strings);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(spinnerArrayAdapter);
    }
}
