package com.example.quizapp.Alert;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.fragment.app.FragmentManager;

import com.example.quizapp.Fragments.SummaryScreen;
import com.example.quizapp.R;

public class Alerts {
    public static void alertDialogShow(Context context, FragmentManager fragmentManager) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage("Are you sure you want to Submit the test?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SummaryScreen summaryScreen=new SummaryScreen();
                        fragmentManager.beginTransaction().replace(R.id.main_activity,summaryScreen).commit();
                    }
                });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialogBuilder.show();
    }
}