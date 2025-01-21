package com.uni2k.demo.config;

import android.app.Activity;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Size;

import androidx.annotation.NonNull;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;

/**
 * created by：lynn.shao on 2021/12/16.
 */
public class ResolutionCameraConfig extends CameraConfig {

    private Size mTargetSize;
    private final int LIMIT_SIZE=1080;//1080
    private final double RATIO_4_3_VALUE = 4.0 / 3.0;
    private final double RATIO_16_9_VALUE = 16.0 / 9.0;
    int width ;
    int height ;


    public ResolutionCameraConfig(Activity context) {
        super();



        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
          Rect rect = context.getWindowManager().getCurrentWindowMetrics().getBounds();
          width=rect.width();
          height=rect.height();
        }else {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            width=displayMetrics.widthPixels;
            height=displayMetrics.heightPixels;
        }

      //  Toasty.warning(context,String.format("displayMetrics:%d x %d",width,height)).show();
        //因为为了保持流畅性和性能，限制在1080p，在此前提下尽可能的找到屏幕接近的分辨率
        if(width < height){
            int size = Math.min(width, LIMIT_SIZE);
            float ratio =  width / (float)height;
            if(ratio > 0.7){//一般应用于平板
                mTargetSize = new Size(size, (int)(size / 3.0f * 4.0f));
            }else{
                mTargetSize = new Size(size, (int)(size / 9.0f * 16.0f));
            }
        }else{
            int size = Math.min(height, LIMIT_SIZE);
            float ratio = height / (float)width;
            if(ratio > 0.7){//一般应用于平板
                mTargetSize = new Size((int)(size / 3.0f * 4.0f), size);
            }else{
                mTargetSize = new Size((int)(size / 9.0f * 16.0), size);
            }
        }

    }
    private Integer aspectRatio(int width,int height){
        double previewRatio = (double) Math.max(width, height) / Math.min(width, height);
        if (Math.abs(previewRatio - RATIO_4_3_VALUE) <= Math.abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3;
        }
        return AspectRatio.RATIO_16_9;
    }


    @NonNull
    @Override
    public Preview options(@NonNull Preview.Builder builder) {
        return super.options(builder);
    }

    @NonNull
    @Override
    public CameraSelector options(@NonNull CameraSelector.Builder builder) {
        return super.options(builder);
    }

    @NonNull
    @Override
    public ImageAnalysis options(@NonNull ImageAnalysis.Builder builder) {
        return super.options(builder);
    }
}
