package com.kazim.textextractor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.googlecode.leptonica.android.GrayQuant;
import com.googlecode.leptonica.android.Pix;

import helper.OtsuThresholder;

public class BinarizationActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    public static Bitmap umbralization;
    private Pix pix;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binarization);

        img = (ImageView) findViewById(R.id.croppedImage);
        FloatingActionButton mFab = (FloatingActionButton) findViewById(R.id.nextStep);
        mFab.setOnClickListener(this);
        pix = com.googlecode.leptonica.android.ReadFile.readBitmap(CropActivity.croppedImage);

        OtsuThresholder otsuThresholder = new OtsuThresholder();
        int threshold = otsuThresholder.doThreshold(pix.getData());
        /* Increasing value of threshold is better */
        threshold += 20;
        umbralization = com.googlecode.leptonica.android.WriteFile.writeBitmap
                (GrayQuant.pixThresholdToBinary(pix, threshold));
        img.setImageBitmap(umbralization);
        AppCompatSeekBar seekBar = (AppCompatSeekBar) findViewById(R.id.umbralization);
        seekBar.setProgress((50 * threshold) / 254);
        seekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        umbralization = com.googlecode.leptonica.android.WriteFile.writeBitmap(
                GrayQuant.pixThresholdToBinary(pix, ((254 * seekBar.getProgress()) / 50)));
        img.setImageBitmap(umbralization);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.nextStep) {
            startActivity(new Intent(BinarizationActivity.this, RecognizerActivity.class));
        }
    }
}
