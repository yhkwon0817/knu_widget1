package com.example.knu_widget.Authentic;

import android.util.Log;

import com.kakao.auth.ISessionCallback;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.Profile;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.exception.KakaoException;

public class SessionCallBack implements ISessionCallback {
    @Override
    public void onSessionOpened() {
        requestMe();
    }

    @Override
    public void onSessionOpenFailed(KakaoException exception) {
    }

    public void requestMe() {
        // 사용자정보 요청 결과에 대한 Callback
        UserManagement.getInstance().me(new MeV2ResponseCallback() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
            }

            // 사용자 정보 요청 실패
            @Override
            public void onFailure(ErrorResult errorResult) {
            }

            @Override
            public void onSuccess(MeV2Response result) {

                String id = String.valueOf(result.getId());
                UserAccount kakaoAccount = result.getKakaoAccount();
                if (kakaoAccount != null) {

                    // 이메일
                    String email = kakaoAccount.getEmail();
                    if (email != null) {
                        Log.e("SessionCallback :: ", "onSuccess:email " + email);
                    }

                    // 프로필
                    Profile _profile = kakaoAccount.getProfile();

                    if (_profile != null) {

                        Log.e("SessionCallback :: ", "nickname: " + _profile.getNickname());

                    }
                    Log.e("###", "여기까지 옴3");
                } else {
                    Log.i("KAKAO_API", "onSuccess: kakaoAccount null");
                }
            }
        });
    }
}
