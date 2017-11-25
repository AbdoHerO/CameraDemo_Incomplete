package com.example.abdohero.camerademo;

import android.content.Intent;
import android.hardware.Camera;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback{
    Camera camera;
    SurfaceView surfaceView;
    FloatingActionButton capture;
    SurfaceHolder surfaceHolder;
    Camera.ShutterCallback shutterCallback;
    Camera.PictureCallback pictureCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        capture=(FloatingActionButton)findViewById(R.id.cameracapture);
        surfaceView=(SurfaceView)findViewById(R.id.surfaceview);

        surfaceHolder=surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        capture.setOnClickListener(new FloatingActionButton.OnClickListener(){
            @Override
            public void onClick(View view) {
                cameraImage();
            }
        });
        pictureCallback=new Camera.PictureCallback() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                FileOutputStream fileOutputStream = null;
                File file = getDisc();
                if (!file.exists() && !file.mkdirs()) {
                    Toast.makeText(MainActivity.this, "Can't create disctory to save your image", Toast.LENGTH_LONG).show();
                    return;
                }
                //comt
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String date = simpleDateFormat.format(new Date());
                String name = "Img" + date + ".jpg";
                String filaName = file.getAbsolutePath() + "/" + name;
                File newFile = new File(filaName);
                try {
                    fileOutputStream = new FileOutputStream(newFile);
                    Toast.makeText(MainActivity.this, "Save image Success", Toast.LENGTH_LONG).show();
                    fileOutputStream.write(data);
                    fileOutputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                refreshGallery(newFile);
                refreshCamera();
            }
        };

    }

    public void refreshCamera(){
        if(surfaceHolder.getSurface()==null){
            return;
        }
        try {
            camera.stopPreview();
        }catch (Exception e){

        }
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void refreshGallery(File file){
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        sendBroadcast(intent);
    }
    @NonNull
    private File getDisc(){
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        return new File(file,"Image Demo ");
    }

    public void cameraImage(){

        camera.takePicture(null,null,pictureCallback);
    }



    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try{
            camera=Camera.open();
        }catch (RuntimeException ex){

        }
        Camera.Parameters parameters;
        parameters=camera.getParameters();
        parameters.setPreviewFrameRate(20);
        parameters.setPictureSize(352,288);
        camera.setParameters(parameters);
        camera.setDisplayOrientation(90);
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        refreshCamera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        camera.stopPreview();
        camera.release();
        camera=null;
    }
}
    /*
    public void startSave(ImageView img) {
            FileOutputStream fileOutputStream = null;
            File file = getDisc();
            if (!file.exists() && !file.mkdirs()) {
                Toast.makeText(this, "Can't create disctory to save your image", Toast.LENGTH_LONG).show();
                return;
            }
            //comt
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String date = simpleDateFormat.format(new Date());
            String name = "Img" + date + ".jpg";
            String filaName = file.getAbsolutePath() + "/" + name;
            File newFile = new File(filaName);
            try {
                fileOutputStream = new FileOutputStream(newFile);
                Toast.makeText(this, "Save image Success", Toast.LENGTH_LONG).show();
                fileOutputStream.write(data);
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            refreshGallery(newFile);
    }
    public void refreshGallery(File file){
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        sendBroadcast(intent);
    }
    private File getDisc(){
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        return new File(file,"Image Demo ");
    }
     */