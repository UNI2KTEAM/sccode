package com.uni2k.demo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;
import com.uni2k.demo.config.ResolutionCameraConfig;
import com.uni2k.demo.databinding.ActivityCameraBinding;
import com.uni2k.demo.utils.ImageUtils;


import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.core.resolutionselector.AspectRatioStrategy;
import androidx.camera.core.resolutionselector.ResolutionSelector;
import androidx.camera.core.resolutionselector.ResolutionStrategy;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * created by：lynn.shao on 2024/12/30.
 */
public class CameraActivity extends AppCompatActivity {
    private ActivityCameraBinding binding;
    private PreviewView previewView;
    private GraphicOverlay viewfinderView;
    private  CameraManager cameraManager;
    private ListenableFuture<ProcessCameraProvider> mCameraProviderFuture;
    private static Camera mCamera;
    private ResolutionCameraConfig mCameraConfig;
    private  Preview preview;

    private int screenWith;
    private int screenHeight;

    private int cropHeight;
    private int cropWith;
    private double limit;
    private Gson gson=new Gson();

    private boolean analysis=true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCameraBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        previewView=binding.previewView;
        viewfinderView=binding.baseBg;

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenWith=displayMetrics.widthPixels;
        screenHeight=displayMetrics.heightPixels;
        limit= (double) getCropWidth(3)/getDimensionForVersionHeight();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        hideSystemUI();
        initView();
        if(checkPermission(this, Manifest.permission.CAMERA)){
            startCamera();
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},101);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==101){
            if(grantResults.length!=0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                startCamera();
            }else {
                Toast.makeText(this,"get camera permission fail",Toast.LENGTH_SHORT).show();
            }

        }else {
            Toast.makeText(this,"get camera permission fail",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        release();
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        decorView.setSystemUiVisibility(uiOptions);
    }
    private void initView(){
        previewView.setImplementationMode(PreviewView.ImplementationMode.COMPATIBLE);
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
    }


    public void startCamera(){
        if(mCameraConfig == null){
            mCameraConfig = new ResolutionCameraConfig(this);
        }
        mCameraProviderFuture = ProcessCameraProvider.getInstance(this);
        mCameraProviderFuture.addListener(() -> {
            try{
                //相机选择器
                CameraSelector cameraSelector = mCameraConfig.options(new CameraSelector.Builder());

                ResolutionSelector resolutionSelector = new ResolutionSelector.Builder()
                        .setAspectRatioStrategy(AspectRatioStrategy.RATIO_16_9_FALLBACK_AUTO_STRATEGY)
                        .setAllowedResolutionMode(ResolutionSelector.PREFER_CAPTURE_RATE_OVER_HIGHER_RESOLUTION )
                        .setResolutionStrategy(ResolutionStrategy.HIGHEST_AVAILABLE_STRATEGY)
                        .build();

                preview=new Preview.Builder()
                        .setResolutionSelector(resolutionSelector)
                        .build();

                preview.setSurfaceProvider(previewView.getSurfaceProvider());
                preview.setTargetRotation(Surface.ROTATION_90);//mPreviewView.getDisplay().getRotation()

                //图像分析
                ImageAnalysis.Builder builder=new ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST);

                builder.setResolutionSelector(resolutionSelector);

                ImageAnalysis imageAnalysis = mCameraConfig.options(builder);
                imageAnalysis.setTargetRotation(Surface.ROTATION_90);


                imageAnalysis.setAnalyzer(Executors.newSingleThreadExecutor(), image -> {
                    //解析图片
                    imageAnalysis(image);
                    image.close();
                });

                if(mCamera != null){
                    mCameraProviderFuture.get().unbindAll();
                }
                //绑定到生命周期
                mCamera = mCameraProviderFuture.get().bindToLifecycle(this, cameraSelector, preview, imageAnalysis);
                //注意 强烈建议设置为0.2-0.25，解码效果更好
                mCamera.getCameraControl().setLinearZoom(0.25f);
            }catch (Exception e){
                e.printStackTrace();
            }

        }, ContextCompat.getMainExecutor(this));

    }

    // deal pic with unicode lib;
    //The image must be cut according to the size of the unicode scanning frame and then sent to the native
    private void imageAnalysis(ImageProxy imageProxy){
        byte[] nv21data=getNv21Date(imageProxy);
        double scal1=(double) imageProxy.getWidth()/screenWith;
        double scal2=(double)imageProxy.getHeight()/screenHeight;
        double scal=Math.min(scal1,scal2);///(1.2*1.2f);
        cropHeight= (int) (viewfinderView.getRectHeight()*scal)+120;
        cropWith= (int) (cropHeight*limit);

        byte[] cutData;
        int   x = (int) (imageProxy.getWidth() / 2 - cropWith/ 2);
        int   y = (int) (imageProxy.getHeight() / 2 - cropHeight/ 2);
        Rect rect=new Rect();
        rect.top=y;
        rect.left=x;
        rect.right=x+cropWith;
        rect.bottom=y+cropHeight;
        cutData= ImageUtils.nv21Clip(nv21data,imageProxy.getWidth(),imageProxy.getHeight(),rect);
        if(cutData!=null&&cutData.length>0){
            UniCodeHintType uniCodeHitType = new UniCodeHintType();
            uniCodeHitType.setEC_LEVEL("H");
            uniCodeHitType.setMARGIN(2);//最少是2 就是无边距
            uniCodeHitType.setSECURE_KEY("UNI2K");//key
            try {
                UniCodeHintType outType= UniCodeJNI.ScDetectAndDecode(cutData,rect.width(),rect.height(),uniCodeHitType);
                analysis=false;
                //TODO 解码结果  自定义处理码内容
                Log.e("test","result："+gson.toJson(outType));
            }catch ( Exception e){
                Log.e("test","result：解码失败");
            }

        }
    }

    @SuppressLint("UnsafeOptInUsageError")
    private byte[] getNv21Date(ImageProxy imageProxy){
        ByteBuffer nv21Buffer = ImageUtils.yuv420ThreePlanesToNV21(Objects.requireNonNull(imageProxy.getImage()).getPlanes(), imageProxy.getWidth(), imageProxy.getHeight());
        nv21Buffer.rewind();
        byte[] imageInBuffer = new byte[nv21Buffer.limit()];
        nv21Buffer.get(imageInBuffer, 0, imageInBuffer.length);
        return imageInBuffer;
    }

    public void release() {

        if(mCameraProviderFuture != null){
            try {
                mCameraProviderFuture.get().unbindAll();
            }catch (Exception e){
               e.printStackTrace();
            }
        }

    }
    private int getCropWidth(int model){
        return  160+( model-1)*149+4;
    }
    private int getDimensionForVersionHeight()
    {
        return  108+4;
    }
    public static boolean checkPermission(@NonNull Context context, @NonNull String permission){
        return ActivityCompat.checkSelfPermission(context,permission) == PackageManager.PERMISSION_GRANTED;
    }
}
