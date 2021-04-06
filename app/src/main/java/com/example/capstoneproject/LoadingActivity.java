package com.example.capstoneproject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        // 타이틀바 제거
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // 애니메이션 적용
        ImageView backgroundImageView = findViewById(R.id.imageView3);
        Glide.with(this).load(R.raw.lodaing2).into(backgroundImageView);

        // 2초간 실행
        Handler timer = new Handler();
        timer.postDelayed(new Runnable() {
            public void run(){
                finish();
            }
        }, 1300);
    }
}