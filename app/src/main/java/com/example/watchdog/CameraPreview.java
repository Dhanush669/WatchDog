package com.example.watchdog;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.io.IOException;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    Camera mcamera;
    SurfaceHolder holder;
    Context context;
    public CameraPreview(Context context,Camera mcamera) {
        super(context);
        this.mcamera=mcamera;
        holder= getHolder();
        holder.addCallback(this);
        this.context=context;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mcamera.setPreviewDisplay(holder);
            mcamera.startPreview();
        } catch (Exception e) {
            //Toast.makeText(context, "camera error", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mcamera.release();

    }
}
