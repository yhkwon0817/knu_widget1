package com.example.knu_widget;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;

public class LogInActivity extends AppCompatActivity {
    private SessionCallBack mSessionCallback;
    private long clickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        //카카오톡 자동로그인
        if (Session.getCurrentSession().checkAndImplicitOpen()) {
            UserManagement.getInstance().me(new MeV2ResponseCallback() {
                @Override
                public void onSuccess(MeV2Response result) {
                    Log.e("###", "kakao automatic log in");
                    Intent intent = new Intent(LogInActivity.this, GetData.class);
                    startActivity(intent);
                    finish();
                }
                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                }
            });
        }

        //카카오톡 로그인
        mSessionCallback = new SessionCallBack() {
            @Override
            public void onSessionOpened() {
                // 로그인 요청
                UserManagement.getInstance().me(new MeV2ResponseCallback() {
                    @Override
                    public void onFailure(ErrorResult errorResult) {
                        // 로그인 실패
                        Toast.makeText(LogInActivity.this, "로그인에 실패하였습니다.. 다시 시도해주세요", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSessionClosed(ErrorResult errorResult) {
                        // 로그인 세션이 닫힘
                    }

                    @Override
                    public void onSuccess(MeV2Response result) {
                        // 로그인 성공
                        Log.e("###", "id : "+result.getId());
                        Intent intent=new Intent(LogInActivity.this,GetData.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }

            @Override
            public void onSessionOpenFailed(KakaoException exception) {

            }
        };
        Session.getCurrentSession().addCallback(mSessionCallback);
        Session.getCurrentSession().checkAndImplicitOpen();
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
        Session.getCurrentSession().removeCallback(mSessionCallback);
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