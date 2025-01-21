package com.uni2k.demo.config;

import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;

/**
 * created by：lynn.shao on 2021/12/16.
 */
public class CameraConfig {
    public CameraConfig(){

    }

    @NonNull
    public Preview options(@NonNull Preview.Builder builder){
        return builder.build();
    }

    @NonNull
    public CameraSelector options(@NonNull CameraSelector.Builder builder){
        return builder.build();
    }

    @NonNull
    public ImageAnalysis options(@NonNull ImageAnalysis.Builder builder){
        return builder.build();
    }

    @NonNull
    public ImageCapture options(@NonNull ImageCapture.Builder builder){
        return builder.build();
    }

}
