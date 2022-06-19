package com.example.knu_widget.Authentic;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.knu_widget.GetData;
import com.example.knu_widget.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kakao.auth.Session;
import com.kakao.sdk.user.UserApiClient;

public class LogInActivity extends AppCompatActivity {
    private long clickTime = 0;
    private String Uid = null;
    private FirebaseFirestore mstore = FirebaseFirestore.getInstance();
    private SharedPreferences preferences;
    ImageView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        login = findViewById(R.id.login);
        preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        Uid = preferences.getString("userid", null);

        if (Uid == null) {
            Log.e("###", "Uid null");
            UserApiClient.getInstance().loginWithKakaoAccount(LogInActivity.this, (oAuthToken, throwable) -> {
                if (throwable != null) {
                    Log.e("###", "로그인 실패 : " + throwable);
                } else {
                    requestMe();
                }
                return null;
            });
        }else{
            Intent intent =new Intent(LogInActivity.this, GetData.class);
            startActivity(intent);
            finish();
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserApiClient.getInstance().loginWithKakaoAccount(LogInActivity.this, (oAuthToken, throwable) -> {
                    if (throwable != null) {
                        Log.e("###", "로그인 실패 : " + throwable);
                    } else {
                        requestMe();
                    }
                    return null;
                });
            }
        });
    }

    private void requestMe() {
        UserApiClient.getInstance().me(((user, throwable) -> {
            if(throwable!=null){
                Log.e("###", "사용자 정보 요청 실패"+throwable);
            }else{
                String Uid = String.valueOf(user.getId());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("userid", Uid);
                //항상 commit & apply 를 해주어야 저장이 된다.
                editor.apply();

                Intent intent =new Intent(LogInActivity.this, GetData.class);
                startActivity(intent);
                finish();
            }
            return null;
        }));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(Session.getCurrentSession().handleActivityResult(requestCode,resultCode,data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    // 뒤로가기 버튼 2번 누를 시에 앱 종료
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            if(SystemClock.elapsedRealtime() - clickTime < 2000) {
                finish();
                overridePendingTransition(0,0);
                return true;
            }
            clickTime = SystemClock.elapsedRealtime();
            Toast.makeText(getApplication(),"한번 더 클릭하시면 앱을 종료합니다",Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}