package com.example.user.afinal.Activity;

import android.app.WallpaperManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.example.user.afinal.R;

import java.io.File;

/**
 * Created by SHAJIB on 25/12/2015.
 */
public class GalleryPreview extends AppCompatActivity {

    ImageView GalleryPreviewImg;
    String path;

    //from  net

    ImageView thumb_imgview;
    ViewFlipper viewFlipper;
    Button b_wall;
    Button b_home;

    // Animation a,b;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;
    int j;

    WallpaperManager myWall;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_preview);
        Intent intent = getIntent();
        path = intent.getStringExtra("path");
        GalleryPreviewImg = (ImageView) findViewById(R.id.GalleryPreviewImg);
        try {
            Glide.with(GalleryPreview.this)
                    .load(new File(path)) // Uri of the picture
                    .into(GalleryPreviewImg);

        }catch (Exception ex){
            Toast.makeText(GalleryPreview.this,"Cannot load Image"+""+ ex.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }

    }
}
