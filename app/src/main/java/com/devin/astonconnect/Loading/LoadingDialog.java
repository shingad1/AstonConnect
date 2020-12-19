package com.devin.astonconnect.Loading;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;

import com.devin.astonconnect.R;

public class LoadingDialog {

    private Activity activity;
    private AlertDialog alertDialog;
    private int layoutResource;

    public LoadingDialog(Activity myActivity, int layoutResource){
        activity = myActivity;
        this.layoutResource = layoutResource;
    }

    public void startLoadingAnimation(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(layoutResource, null));

        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }

    public void dismissDialog(){
        alertDialog.dismiss();
    }
}
