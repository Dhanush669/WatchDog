package com.example.watchdog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class MainActivity extends AppCompatActivity {
    EditText password;
    Button button;
    CameraPreview cameraPreview;
    static SharedPreferences sharpref;
    static SharedPreferences wsharpref;
    TextView textView;
    SharedPreferences.Editor editor;
    static SharedPreferences.Editor weditor;
    public static final String sharpref_Key="password";
    public static final String Watch_Dog="watchdog";
    Camera mcamera;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        password=findViewById(R.id.password);
        button=findViewById(R.id.passbtn);
        textView=findViewById(R.id.textview);
        sharpref=this.getPreferences(MODE_PRIVATE);
        wsharpref=this.getSharedPreferences("WatchDogs",MODE_PRIVATE);
        try {
            mcamera=Camera.open(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        if(sharpref.getString(sharpref_Key,null)!=null){
            button.setText("Go");
            textView.setText("Enter your password ");
        }
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(sharpref.getString(sharpref_Key,null)==null){
                    editor=sharpref.edit();
                    editor.putString(sharpref_Key,password.getText().toString());
                    editor.apply();
                    Toast.makeText(MainActivity.this, "Password saved", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getApplicationContext(),SecondActivity.class);
                    startActivity(intent);
                }
        else {
            try {
                mcamera.startPreview();
            }catch (Exception e){
                //
            }

                    if(sharpref.getString(sharpref_Key, null).equals(password.getText().toString())){
                        Intent intent=new Intent(getApplicationContext(),SecondActivity.class);
                        startActivity(intent);
                        mcamera.release();
                    }
                    else{
                        cameraPreview = new CameraPreview(MainActivity.this, mcamera);
                        FrameLayout preview = findViewById(R.id.layoutt);
                        preview.addView(cameraPreview);
                        Camera.PictureCallback mPicture = new Camera.PictureCallback() {

                            @Override
                            public void onPictureTaken(byte[] data, Camera camera) {
                                File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
                                if (pictureFile == null){
                                   // Log.d(TAG, "Error creating media file, check storage permissions");
                                    return;
                                }

                                try {
                                    FileOutputStream fos = new FileOutputStream(pictureFile);
                                    fos.write(data);
                                    fos.close();
                                } catch (FileNotFoundException e) {
                                    //Log.d(TAG, "File not found: " + e.getMessage());
                                } catch (IOException e) {
                                    //Log.d(TAG, "Error accessing file: " + e.getMessage());
                                }
                           }
                        };
                        try {
                            mcamera.takePicture(null, null, mPicture);
                            weditor=wsharpref.edit();
                            weditor.putString(Watch_Dog,"Watch Dog Alert");
                            weditor.apply();
                        }catch (Exception e){
                            //Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                        Toast.makeText(MainActivity.this, "Please Wait", Toast.LENGTH_LONG).show();
                        Toast.makeText(MainActivity.this, "Incorrect Password!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });
    }
    public static final int MEDIA_TYPE_IMAGE = 1;

    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        }
        else {
            return null;
        }
        return mediaFile;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mcamera.release();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mcamera.release();
    }
}