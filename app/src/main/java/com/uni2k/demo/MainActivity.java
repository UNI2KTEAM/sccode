package com.uni2k.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.uni2k.demo.databinding.ActivityMainBinding;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'Uni2kCode' library on application startup.


    private ActivityMainBinding binding;
    private Button scan_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        scan_btn =binding.scan;
        UniCodeJNI.prepareModel(this);
        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,CameraActivity.class);
                startActivity(intent);
            }
        });
    }







    @Override
    protected void onDestroy() {
        super.onDestroy();
        UniCodeJNI.release();
    }
}