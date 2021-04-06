package com.example.capstoneproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kakao.auth.ApiErrorCode;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;

public class LoginActivity extends AppCompatActivity {
    Balloon balloon;
    static String strNickname;
    String strProfile;
    TextView img_up;
    Button btnStart, btnStop;
    ImageView imgHelp;

    private DrawerLayout drawerLayout;
    private View drawerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 타이틀바 제거
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);

        // 실행 버튼
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 애니메이션 적용
                ImageView backgroundImageView = findViewById(R.id.imageView);
                Glide.with(getApplicationContext()).load(R.raw.loading).into(backgroundImageView);

                btnStart.setEnabled(false);
                btnStart.setBackgroundResource(R.drawable.button_style_enabled_false);

                btnStop.setEnabled(true);
                btnStop.setBackgroundResource(R.drawable.background_rounding);

                Toast.makeText(getApplicationContext(), "침입자 감지를 시작합니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // 종료 버튼
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(LoginActivity.this)
                        .setMessage("중지하시겠습니까?")
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                btnStop.setEnabled(false);
                                btnStop.setBackgroundResource(R.drawable.button_style_enabled_false);

                                btnStart.setEnabled(true);
                                btnStart.setBackgroundResource(R.drawable.background_rounding);

                                // 애니메이션 종료
                                ImageView backgroundImageView = findViewById(R.id.imageView);
                                Glide.with(getApplicationContext()).load(R.drawable.loadingstop).into(backgroundImageView);

                                Toast.makeText(getApplicationContext(), "작동을 중지하였습니다.", Toast.LENGTH_SHORT).show();

                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerView = (View) findViewById(R.id.drawerView);
        drawerLayout.setDrawerListener(listener);

        TextView tvNickname = findViewById(R.id.tvNickname);
        ImageView ivProfile = findViewById(R.id.ivProfile);
        TextView btnLogout = findViewById(R.id.btnLogout);
        TextView btnSignout = findViewById(R.id.btnSignout);

        img_up = findViewById(R.id.img_up);

        // 이미지 업로드 하러가기
        img_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ImageUpload.class);
                startActivity(intent);
            }
        });

        // 도움말 구현
        imgHelp = findViewById(R.id.imgHelp);
        imgHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                balloon = new Balloon.Builder(getApplicationContext())
                        .setArrowSize(10)
                        .setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.Dark_gray))
                        .setArrowOrientation(ArrowOrientation.BOTTOM)
                        .setArrowPosition(0.5f)
                        .setArrowVisible(true)
                        .setWidthRatio(0.5f)
                        .setHeight(65)
                        .setTextSize(12f)
                        .setCornerRadius(6f)
                        .setAlpha(0.9f)
                        .setText("사진을 등록하면 지인 방문 시 자동으로 어플을 종료할 수 있습니다.")
                        .setBalloonAnimation(BalloonAnimation.FADE)
                        .setAutoDismissDuration(3000L)
                        .build();

                balloon.showAlignTop(imgHelp);
            }
        });


        // 이름, 프로필 이미지 Url 가져오기
        Intent intent = getIntent();
        strNickname = intent.getStringExtra("name");
        strProfile = intent.getStringExtra("profile");

        tvNickname.setText(strNickname);

        // 프로필 이미지 업로드
        if(strProfile == null){
            Glide.with(this).load(strProfile).load(R.drawable.icon); // 기본 프로필인 경우 기본 이미지 업로드 (엉엉 드디어 성공했다ㅠㅠㅠㅠ)
        }else{
            Glide.with(this).load(strProfile).into(ivProfile); // 카톡 프로필 사진 업로드
        }

        // 로그아웃 함수
        btnLogout.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "정상적으로 로그아웃되었습니다.", Toast.LENGTH_SHORT).show();

                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
            }
        });

        // 회원탈퇴 함수
        btnSignout.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(LoginActivity.this)
                        .setMessage("탈퇴하시겠습니까?")
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                UserManagement.getInstance().requestUnlink(new UnLinkResponseCallback() {
                                    @Override
                                    public void onFailure(ErrorResult errorResult) {
                                        int result = errorResult.getErrorCode();

                                        if(result == ApiErrorCode.CLIENT_ERROR_CODE) {
                                            Toast.makeText(getApplicationContext(), "네트워크 연결이 불안정합니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "회원탈퇴에 실패했습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onSessionClosed(ErrorResult errorResult) {
                                        Toast.makeText(getApplicationContext(), "로그인 세션이 닫혔습니다. 다시 로그인해 주세요.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onNotSignedUp() {
                                        Toast.makeText(getApplicationContext(), "가입되지 않은 계정입니다. 다시 로그인해 주세요.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onSuccess(Long result) {
                                        Toast.makeText(getApplicationContext(), "회원탈퇴에 성공했습니다.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });

                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
    }

    // 사이드바 구현 리스너
    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {
        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {
        }

        @Override
        public void onDrawerStateChanged(int newState) {
        }
    };
}