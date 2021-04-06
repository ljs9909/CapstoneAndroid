package com.example.capstoneproject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ImageUpload extends AppCompatActivity {

    private final int GET_GALLERY_IMAGE = 200;
    private Button btnUpload, imgList;
    private ImageView imageView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);

        // 타이틀바 제거
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        imgList = findViewById(R.id.imgList);
        // 이미지 목록 보러 가기
        imgList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SavedData.class);
                startActivity(intent);
            }
        });

        imageView2 = (ImageView) findViewById(R.id.imageView2);
        // 이미지 클릭 시 갤러리 선택
        imageView2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent. setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        btnUpload = findViewById(R.id.btnUpload);

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            // 갤러리 데이터(사진) 불러오기
            Uri selectedImageUri = data.getData();
            imageView2.setImageURI(selectedImageUri);

            // 갤러리에서 이미지 업로드 시 버튼 활성화
            btnUpload.setBackgroundResource(R.drawable.background_rounding);
            btnUpload.setEnabled(true);
        }
    }
}