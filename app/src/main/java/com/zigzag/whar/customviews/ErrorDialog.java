package com.zigzag.whar.customviews;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;

import com.zigzag.whar.R;

public class ErrorDialog extends Dialog {

    public ErrorDialog(Activity activity) {
        super(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_error);
    }
}