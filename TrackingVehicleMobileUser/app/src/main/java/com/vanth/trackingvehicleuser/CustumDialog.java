package com.vanth.trackingvehicleuser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;

import dmax.dialog.SpotsDialog;

public class CustumDialog {
    private Activity activity;
    private AlertDialog dialog;
    private Context context;


    // constructor of dialog class
    // with parameter activity
//    CustumDialog(Activity myActivity) {
//        activity = myActivity;
//    }
    public CustumDialog(Context context)
    {
        this.context = context;
        dialog = new SpotsDialog(context,R.style.CustomProgress);
    }

    @SuppressLint("InflateParams")
    public void startLoadingdialog() {
/*
        // adding ALERT Dialog builder object and passing activity as parameter
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        // layoutinflater object and use activity to get layout inflater
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_progress_dialog, null));
        builder.setCancelable(true);

        dialog = builder.create(); */
        dialog.show();


    }

    // dismiss method
    public void dismissdialog() {
        dialog.dismiss();
    }
}
