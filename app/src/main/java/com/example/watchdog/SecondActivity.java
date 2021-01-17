package com.example.watchdog;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        if(MainActivity.wsharpref.getString(MainActivity.Watch_Dog,null)!=null){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Watch Dog Alert !.........");
            alertDialogBuilder.setMessage("This App sensed a Watch Dog,\nIllegally tried to open this App.\nCheck your Gallery to identify the watch dog");
            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        SharedPreferences preferences = getSharedPreferences("WatchDogs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.apply();
                    }catch (Exception e){
                        Toast.makeText(SecondActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }
}